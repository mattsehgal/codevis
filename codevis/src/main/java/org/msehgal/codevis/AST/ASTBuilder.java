package org.msehgal.codevis.AST;

import org.msehgal.codevis.AST.nodes.*;
import org.msehgal.codevis.AST.nodes.statements.expressions.AssignmentNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.AtomNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.ExpressionNode;
import org.msehgal.codevis.antlr.Java9Parser;
import org.msehgal.codevis.antlr.Java9Parser.AssignmentExpressionContext;
import org.msehgal.codevis.antlr.Java9Parser.ClassDeclarationContext;
import org.msehgal.codevis.antlr.Java9Parser.LocalVariableDeclarationStatementContext;
import org.msehgal.codevis.antlr.Java9Parser.OrdinaryCompilationContext;
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

    public void buildAST(){
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
    }

    private String getTextFromXPath(String xpath, ParseTree root){
        return getNodeFromXPath(xpath, root).getText();
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
        ParseTree compUnitTree = getNodeFromXPath(XPaths.CU.x, root);
        compUnit.setId(this.path.getFileName().toString());
        compUnit.setClassDeclaration(getClassDeclaration(compUnit, compUnitTree));
        // ...
        return compUnit;
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
            blocks.add(block);
            if(block.getType() == BlockType.STATEMENT){
                testAssn(block, blockTree);
            }
        }
        return blocks;
    }

    private BlockType getBlockType(ParseTree blockTree){
        ParseTree blockType = getNodeFromXPath(XPaths.BLOCK_TYPE.x, blockTree);
        if(blockType instanceof LocalVariableDeclarationStatementContext){
            return BlockType.VAR_DEC;
        } else if(blockType instanceof ClassDeclarationContext){
            return BlockType.CLASS_DEC;
        } else {
            return BlockType.STATEMENT;
        }
    }

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
        System.out.println("TESTING EXPR: "+res);

    }

    
}
