package org.msehgal.codevis.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.msehgal.codevis.AST.nodes.BlockNode;
import org.msehgal.codevis.AST.nodes.CompilationUnit;
import org.msehgal.codevis.AST.nodes.FieldNode;
import org.msehgal.codevis.AST.nodes.ImportNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.AtomNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.ExpressionNode;

@SuppressWarnings("rawtypes")
public class ParamResolver {

    private String JAVA_LANG = "java.lang.";

    private BlockNode parent;

    private Map<String, Class> candidates = new HashMap<>();

    private Map<String, Class> nameSpace = new HashMap<>();

    /**
     * Traverses AST to identify parameter class, 
     * searches imports and java.lang to find
     * the qualified name of the parameter.
     * @param parent parent AST node
     * @param cu root AST node
     */
    public ParamResolver(BlockNode parent, CompilationUnit cu){
        this.parent = parent;
        init(cu);
    }

    /**
     * Initialize candidate classes and the name space of the AST.
     * @param cu root AST node
     */
    private void init(CompilationUnit cu){
        initCandidateClassMap(cu);
        initNameSpace(cu);
    }

    /**
     * Initializes the candidate classes from imports.
     * @param cu root AST node
     */
    private void initCandidateClassMap(CompilationUnit cu){
        for(ImportNode i : cu.getImports()){
            String qName = i.getQualifiedName();
            String name = qName.substring(qName.lastIndexOf('.')+1, qName.length());
            try {
                candidates.put(name, Class.forName(qName));
            } catch (ClassNotFoundException e) {}
        }
    }

    /**
     * Initializes the namespace with fields and any variables declared in scope.
     * Searches candidate classes before assuming the class belongs to java.lang.
     * @param cu root AST node
     */
    private void initNameSpace(CompilationUnit cu){
        //TODO note that foreign field resolution will require foreign class import list
        for(FieldNode f : cu.getClassDeclaration().getFields()){
            String name = f.getId(); //ref name
            String className = f.getType();
            //is java.lang or import
            //import in map: Node, qname.Node
            if(!nameSpace.containsKey(name)){
                if(candidates.containsKey(className))
                    nameSpace.put(name, candidates.get(className));
                else
                    try{
                    nameSpace.put(name, Class.forName(JAVA_LANG+className));
                    } catch (ClassNotFoundException e) {}
            }
        }
    }

    /**
     * Entry for parameter resolution. Parses parameters
     * before resolving
     * @param params parameters string
     * @return AtomNode[]
     */
    public AtomNode[] resolve(String params){
        //anon to classinst
        //std to atom
        //literal to atom
        //nodes are a data structure dummy, confirm you dont need this^
        //parseParams `->
        String[] parameters = parseParams(params);
        AtomNode[] nodes = new AtomNode[parameters.length];
        //resolveParams `->
        Class[] paramClasses = resolveParams(parameters);
        //paramsToNodes?
        for(int i=0; i<parameters.length; i++){
            AtomNode param = new AtomNode(this.parent, parameters[i], paramClasses[i]);
            nodes[i] = param;
        }
        return nodes;
    }

    /**
     * Parses each parameter from parameters string. 
     * Splits on commas (not within parens; handles anonymous objects).
     * @param params parameters string
     * @return String[] of each parameter
     */
    private String[] parseParams(String params){
        //check for anon classes
        //split on commas not inside parens
        String[] parsedParams = params.split(",(?![^()]*+\\))");
        //split on ,
        return parsedParams;
    }

    /**
     * Entry for resolution to class, pre-parses anonymous objects.
     * @param param parameter string
     * @return parameter class
     */
    public Class resolveParam(String param){
        String className = param;
        //id anon obj, parse out class name
        if(param.contains("new")){
            int newIdx = param.indexOf("new")+3;
            int parenIdx = param.indexOf("(");
            className = param.substring(newIdx, parenIdx);
        }
        Class clazz = resolveParamClass(className, param);
        //ret class[]?
        return clazz;
    }

    /**
     * Plural method of resolveParam.
     * @param params parameter String[]
     * @return parameter Class[]
     */
    public Class[] resolveParams(String... params){
        Class[] classes = new Class[params.length];
        for(int i=0; i<params.length; i++){
            classes[i] = resolveParam(params[i]);
        }
        return classes;
    }

    /**
     * Resolves the parameter class via namespace, anonymous object, or literal type.
     * @param className string name of the class
     * @param param parameter string
     * @return parameter class
     */
    private Class resolveParamClass(String className, String param){
        //check&return if direct ref (THIS CHECK IS PROBABLY WRONG!!!)
        //if(param.equals(className)){
        if(nameSpace.containsKey(param)){
            return nameSpace.get(param);
        }
        //}
        //anon class
        //NEEDS CHECK SO LITERAL DOESNT RUN THROUGH THIS    
        if(param.contains("new") && param.contains(")")){
            int lp = param.indexOf("(")+1;
            int rp = param.indexOf(")");
            String[] anonParams = param.strip()
                                        .substring(lp, rp)
                                        .split(",");
            Class[] classes = resolveParams(anonParams);
            //Class[] classes = resolveAnonParamsClasses(anonParams);
            System.out.println(classes);
            //^ handle array of param classes
        }

        return resolveLiteralType(param);
    }

    /**
     * Final resolution method, returns a literal class as the java.lang wrapper.
     * ints and longs become Integer, floats and doubles become Double.
     * @param param parameter string
     * @return literal class types: null, String, Boolean, Integer, Double
     */
    private Class resolveLiteralType(String param){
        //don't handle char as they are string atoms?
        try{
        if(param.equals("null")){
            //ret null
            return null;
        } else if(param.contains("\"")){
            //ret string
            //TODO string should be caught earlier since it's not literal
            return Class.forName("java.lang.String");
        } else if(param.equals("true") || param.equals("false")){
            //ret bool
            return Class.forName("java.lang.Boolean");
        } else {
            try{
                Integer.parseInt(param);
                //ret int
                return Class.forName("java.lang.Integer");
            } catch (NumberFormatException intE){
                try{
                    Double.parseDouble(param);
                    //ret double
                    return Class.forName("java.lang.Double");
                } catch (NumberFormatException doubleE){
                    //ret null
                }
            }
        }
        } catch (ClassNotFoundException e){

        }   
        //ret null
        return null;
    }

    private AtomNode[] paramsToAtom(Class... classes){
        //TODO implement
        return null;
    }
}
