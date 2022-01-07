package org.msehgal.codevis.AST;

import org.msehgal.codevis.AST.nodes.*;
import org.msehgal.codevis.AST.nodes.statements.StatementNode;
import org.msehgal.codevis.AST.nodes.statements.VariableDeclarationNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.AssignmentNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.AtomNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.ExpressionNode;
import org.msehgal.codevis.antlr.Java9Parser;
import org.msehgal.codevis.antlr.Java9Parser.AssignmentExpressionContext;
import org.msehgal.codevis.antlr.Java9Parser.ClassDeclarationContext;
import org.msehgal.codevis.antlr.Java9Parser.ExpressionStatementContext;
import org.msehgal.codevis.antlr.Java9Parser.LocalVariableDeclarationStatementContext;
import org.msehgal.codevis.antlr.Java9Parser.OrdinaryCompilationContext;
import org.msehgal.codevis.antlr.Java9Parser.StatementContext;
import org.msehgal.codevis.antlr.Java9Parser.StatementWithoutTrailingSubstatementContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.antlr.v4.runtime.tree.xpath.XPath;
import org.msehgal.codevis.antlr.Java9Lexer;

public class ASTBuilder {
    
    private Path path;

    private Java9Parser parser;

    private ParseTree tree;

    private CompilationUnit currentRoot;
     
    public ASTBuilder(Path path){
        this.path = path;
        setParser();
    }

    private void setParser() {
        try {
            CharStream input = CharStreams.fromPath(this.path);
            Java9Lexer lexer = new Java9Lexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            this.parser = new Java9Parser(tokens);
            this.tree = this.parser.compilationUnit();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public AST buildAST(){
        CompilationUnit cu = getCompilationUnit(this.tree);
        System.out.println("CU: "+cu.getId());
        ClassNode clazz = cu.getClassDeclaration();
        System.out.println("\tCLASS: "+clazz.getId());
        List<FieldNode> fields = clazz.getFields();
        for(FieldNode field : fields){
            System.out.println("\t\tFIELD: "+field.getId());
            System.out.println("\t\t\ttype: "+field.getType());
        }
        if(fields.isEmpty()) System.out.println("\t\tNO FIELDS");
        List<MethodNode> methods = clazz.getMethods();
        for(MethodNode method : methods){
            System.out.println("\t\tMETHOD: "+method.getId());
            System.out.println("\t\t\treturn: "+method.getResult());
            System.out.println("\t\t\tbody: "+method.getBody().getContent());
            for(BlockNode block : method.getBody().getBlocks()){
                System.out.println("\t\t\t\tblock: "+block.getText());
                System.out.println("\t\t\t\t\ttype: "+block.getType().toString());
            }
        }
        if(methods.isEmpty()) System.out.println("\t\tNO METHODS");
        return new AST(cu);
    }

    private String getTextFromXPath(String xpath, ParseTree root){
        return getNodeFromXPath(xpath, root)!=null ? 
                getNodeFromXPath(xpath, root).getText() :
                null;
    }

    private ParseTree getNodeFromXPath(String xpath, ParseTree root){
        List<ParseTree> list = new ArrayList<>();
        Collection<ParseTree> coll = XPath.findAll(root, xpath, parser);
        for(ParseTree t : coll){
            list.add(t);
        }
        //return XPath.findAll(root, xpath, parser).toArray(new ParseTree[1])[0];
        return list.isEmpty() ? null : list.get(0);
    }

    private List<ParseTree> getNodesFromXPath(String xpath, ParseTree root){
        List<ParseTree> nodes = new ArrayList<>();
        for(ParseTree pt : XPath.findAll(root, xpath, this.parser)){
            nodes.add(pt);
        }
        return nodes;
    }

    private CompilationUnit getCompilationUnit(ParseTree root){
        CompilationUnit compUnit = new CompilationUnit();
        this.currentRoot = compUnit;
        ParseTree compUnitTree = getNodeFromXPath(XPaths.CU.x, root);
        compUnit.setId(this.path.getFileName().toString());
        compUnit.setPackageDeclaration(getPackage(compUnit, compUnitTree));
        compUnit.setImports(getImports(compUnit, compUnitTree));
        compUnit.setClassDeclaration(getClassDeclaration(compUnit, compUnitTree));

        
        // ...
        return compUnit;
    }

    private PackageNode getPackage(CompilationUnit parent, ParseTree compUnitTree){
        PackageNode pack = new PackageNode(parent);
        ParseTree packageTree = getNodeFromXPath(XPaths.CU_PACKAGE.x, compUnitTree);
        pack.setName(packageTree.getText().replace("package", ""));
        return pack;
    }

    private List<ImportNode> getImports(CompilationUnit parent, ParseTree compUnitTree){
        List<ImportNode> imports = new ArrayList<>();
        List<ParseTree> importTrees = getNodesFromXPath(XPaths.CU_IMPORTS.x, compUnitTree);
        for(ParseTree importTree : importTrees){
            String qName = importTree.getText();
            qName = qName.substring(0,qName.length()-1).replaceAll("import|;", "");
            ImportNode imprt = new ImportNode(parent);
            imprt.setQualifiedName(qName);
            imports.add(imprt);
            System.out.println("ASTBUILDER: IMPORT: "+qName);
        }
        return imports;
    }

    private ClassNode getClassDeclaration(CompilationUnit parent, ParseTree compUnitTree){
        ClassNode classDec = new ClassNode(parent);
        // //typeDec/classDec/normalClassDec
        ParseTree classTree = getNodeFromXPath(XPaths.CLASS.x, compUnitTree);
        //classMod needs more thought on ModNode and current XPATH strategy
        
        // classTree path: //compUnit/ordinaryCompilation//typeDeclaration/classDeclaration/normalClassDeclaration
        classDec.setId(getTextFromXPath(XPaths.CLASS_ID.x, classTree));
        classDec.addFields(getFields(classDec, classTree));
        classDec.addMethods(getMethods(classDec, classTree));

        return classDec;
    }

    private List<FieldNode> getFields(ClassNode parent, ParseTree classTree){
        List<FieldNode> fields = new ArrayList<>();
        // fieldTree path: classTree + /normalClassDeclaration/classBody/classBodyDec//classMemberDec//fieldDec
        for(ParseTree fieldTree : getNodesFromXPath(XPaths.FIELD.x, classTree)){
            FieldNode field = new FieldNode(parent);
            field.setId(getTextFromXPath(XPaths.FIELD_ID.x, fieldTree));
            field.setType(getTextFromXPath(XPaths.FIELD_TYPE.x, fieldTree));
            fields.add(field);
        }

        return fields;
    }

    private List<MethodNode> getMethods(ClassNode parent, ParseTree classTree){
        List<MethodNode> methods = new ArrayList<>();
        // methodTree path: classTree + /normalClassDeclaration/classBody/classBodyDec//classMemberDec//methodDec
        for(ParseTree methodTree : getNodesFromXPath(XPaths.METHOD.x, classTree)){
            MethodNode method = new MethodNode(parent);
            // /methodDec/methodHead/methodDec-tor/id
            method.setId(getTextFromXPath(XPaths.METHOD_ID.x, methodTree));
            //body nodes need work
            method.setBody(getBody(method, methodTree));
            method.setResult(getTextFromXPath(XPaths.METHOD_RETURN.x, methodTree));
            methods.add(method);
        }

        return methods;
    }

    private BodyNode getBody(MethodNode parent, ParseTree methodTree){
        BodyNode body = new BodyNode(parent);
        // bodyTree path: methodTree + /methodBody
        ParseTree bodyTree = getNodeFromXPath(XPaths.METHOD_BODY.x, methodTree);
        body.setContent(bodyTree.getText());
        body.addBlocks(getBlocks(body, bodyTree));
        
        return body;
    }

    private List<BlockNode> getBlocks(BodyNode parent, ParseTree bodyTree){
        List<BlockNode> blocks = new ArrayList<>();
        // blockTree path: bodyTree + /block//blockStatements
        for(ParseTree blockTree : getNodesFromXPath(XPaths.BLOCKS.x, bodyTree)){
            BlockNode block = new BlockNode(parent, blockTree.getText());
            block.setType(getBlockType(blockTree));
            block.setStatement(getStatement(blockTree, block));
            blocks.add(block);
            if(block.getType() == BlockType.EXPR_ASSN){
                testAssn(block, blockTree);
            }
        }
        return blocks;
    }

    private BlockType getBlockType(ParseTree blockTree){
        ParseTree blockType = getNodeFromXPath(XPaths.BLOCK_TYPE.x, blockTree);
        // if(blockType instanceof LocalVariableDeclarationStatementContext){
        //     return BlockType.VAR_DEC;
        // } else if(blockType instanceof ClassDeclarationContext){
        //     return BlockType.CLASS_DEC;
        // } else if(blockType instanceof StatementContext){
        //     blockType = blockType.getChild(0);
        //     if(blockType instanceof StatementWithoutTrailingSubstatementContext){
        //         blockType = blockType.getChild(0);
        //         if(blockType instanceof ExpressionStatementContext){

        //         }
        //     }
        // }else {
        //     return BlockType.STATEMENT;
        // }

        while(blockType != null){
            String[] name = blockType.getClass().getName().split("\\.");
            name = name[name.length-1].split("\\$");
            switch(name[1]){
                case "LocalVariableDeclarationStatementContext":
                    return BlockType.VAR_DEC;
                case "ClassDeclarationContext":
                    return BlockType.CLASS_DEC;
                case "StatementContext":
                
                // below are 2nd+ lvl
                case "StatementWithoutTrailingSubstatementContext":
                
                case "ExpressionStatementContext":
                
                case "StatementExpressionContext":
        
                case "AssignmentContext":
                    return BlockType.EXPR_ASSN;
                case "MethodInvocationContext":
                    return BlockType.EXPR_METHINVOC;
                case "classInstanceCreationExpressionContext":
                    return BlockType.EXPR_CLASSINST;
            }
            blockType = blockType.getChild(0);
        }
        return BlockType.STATEMENT;
    }

    public StatementNode getStatement(ParseTree blockTree, BlockNode block){
        ParseTree statementTree;
        BlockType type = block.getType();
        switch(type){
            case VAR_DEC:
                statementTree = getNodeFromXPath(XPaths.BLOCK_TYPE.x, blockTree);
                return varDecHandler(statementTree, block);
                //break;
            case CLASS_DEC:
                statementTree = getNodeFromXPath(XPaths.BLOCK_TYPE.x, blockTree);
                break;
            case EXPR_ASSN:
                statementTree = getNodeFromXPath(XPaths.ASSN_EXPR.x, blockTree);
                return assignmentHandler(statementTree, block);
                //break;
            case EXPR_ATOM:
            case EXPR_CLASSINST:
                statementTree = getNodeFromXPath(XPaths.CLASSINST_EXPR.x, blockTree);
                break;
            case EXPR_METHINVOC:
                statementTree = getNodeFromXPath(XPaths.METHINVOC_EXPR.x, blockTree);
                break;
            default:
                break;

        }
        StatementNode statement = new StatementNode(block);
        
        
        return statement;
    }

    private AssignmentNode assignmentHandler(ParseTree assignmentTree, BlockNode block){
        AssignmentNode node = new AssignmentNode(block);
        if(assignmentTree == null){return node;}
        if(assignmentTree.getChildCount() == 3){
            node.setLeftHandSide(new AtomNode(node, assignmentTree.getChild(0).getText()));
            node.setOperator(new AtomNode(node, assignmentTree.getChild(1).getText()));
            node.setExpression(new AtomNode(node, assignmentTree.getChild(2).getText()));
            for(int i=0; i<assignmentTree.getChildCount(); i++){
                System.out.println("ASSN: "+assignmentTree.getChild(i).getText());
            }
        }
        return node;
    }

    private VariableDeclarationNode varDecHandler(ParseTree varDecTree, BlockNode block){
        VariableDeclarationNode node = new VariableDeclarationNode(block);
        if(varDecTree == null){return node;}
        varDecTree = varDecTree.getChild(0);
        //unann type context
        String className = getQualifiedNameFromImports(varDecTree.getChild(0).getText());
        node.setClassName(new AtomNode(node, className));
        //vardeclist > vardeclatorctx
        varDecTree = varDecTree.getChild(1).getChild(0);
        node.setName(new AtomNode(node, varDecTree.getChild(0).getText()));
        //TODO parse params
        //TODO idk where to put this but think about ast ordering(op, l, r)
        String param = (getTextFromXPath("//variableInitializer//argumentList", varDecTree)!=null) ? 
                        getTextFromXPath("//variableInitializer//argumentList", varDecTree) : 
                        getTextFromXPath("//expression", varDecTree);
        node.setParams(new AtomNode(node, param.replace("\"","")));
        System.out.println("VARDECHANDLER: "+node.getClassName()+" "+node.getName());
        return node;
    }

    private String getQualifiedNameFromImports(String name){
        for(ImportNode imprt : currentRoot.getImports()){
            if(name.equals(imprt.getName())){
                return imprt.getQualifiedName();
            }
        }
        return name;
    }

    //called in getblocks
    private void testAssn(BlockNode block, ParseTree blockTree){
        String xpath = "blockStatement/statement/statementWithoutTrailingSubstatement/expressionStatement/statementExpression/assignment";
        AssignmentNode assn = new AssignmentNode(block);
        for(ParseTree expr : getNodesFromXPath(xpath, blockTree)){
            ParseTree one = expr.getChild(0);
            assn.setLeftHandSide(new AtomNode(assn, one.getText()));
            assn.setOperator(new AtomNode(assn, expr.getChild(1).getText()));
            assn.setExpression(new AtomNode(assn, expr.getChild(2).getText())); 
        }
        String res = assn.toString();
        System.out.println("ASTBUILDER: TESTING EXPR: "+res);

    }

    
}
