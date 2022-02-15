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

    public ParamResolver(BlockNode parent, CompilationUnit cu){
        this.parent = parent;
        init(cu);
    }

    private void init(CompilationUnit cu){
        initCandidateClassMap(cu);
        initNameSpace(cu);
    }

    private void initCandidateClassMap(CompilationUnit cu){
        for(ImportNode i : cu.getImports()){
            String qName = i.getQualifiedName();
            String name = qName.substring(qName.lastIndexOf('.')+1, qName.length());
            try {
                candidates.put(name, Class.forName(qName));
            } catch (ClassNotFoundException e) {}
        }
    }

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

    //anon to classinst
    //std to atom
    //literal to atom
    public AtomNode[] resolve(String params){
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

    private String[] parseParams(String params){
        //check for anon classes
        //split on commas not inside parens
        String[] parsedParams = params.split(",(?![^()]*+\\))");
        //split on ,
        return parsedParams;
    }

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

    //PLURAL OF ABOVE^
    public Class[] resolveParams(String... params){
        Class[] classes = new Class[params.length];
        for(int i=0; i<params.length; i++){
            classes[i] = resolveParam(params[i]);
        }
        return classes;
    }

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

    private Class resolveLiteralType(String param){
        //don't handle char as they are string atoms?
        try{
        if(param.equals("null")){
            //ret null
            return null;
        } else if(param.contains("\"")){
            //ret string
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
