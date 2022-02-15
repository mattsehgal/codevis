package org.msehgal.codevis.AST;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.msehgal.codevis.antlr.Java9Lexer;
import org.msehgal.codevis.antlr.Java9Parser;

import org.msehgal.codevis.AST.nodes.*;
import org.msehgal.codevis.antlr.Java9BaseListener;
import org.msehgal.codevis.antlr.Java9Parser.*;

public class ASTListener extends Java9BaseListener {
    private CompilationUnit compilationUnit = null;
    private Path path;
    private ClassOrInterfaceNode root = null;

    //TODO PRIO2 - needs to implement expressions, statements, and whatever in AST
    /**
     * A parse tree listener implementing ANTLR4's {@link Java9BaseListener}. 
     * Builds an Abstract Syntax Tree from the source file,
     * @param rootName
     */
    public ASTListener(Path path){
        this.path = path;
    }


    @Override
    public void enterCompilationUnit(CompilationUnitContext ctx) {
        if(this.compilationUnit == null){
            this.compilationUnit = new CompilationUnit(this.path.getFileName().toString());
        }
    }

    @Override
    public void enterPackageDeclaration(PackageDeclarationContext ctx) {
        String name = ctx.getText().replaceFirst("package", "");
        this.compilationUnit.setPackageDeclaration(new PackageNode(this.root, name));
    }

    public static void qtest(String name, String body){
        System.out.println("\nENTERING:"+name);
        System.out.println("\t"+body);
    }

    @Override
    public void enterBlockStatement(BlockStatementContext ctx){
        // qtest(name, body);
        //printChildContexts(ctx);
        //printContextText(ctx);
    }

    @Override
    public void enterLocalVariableDeclarationStatement(LocalVariableDeclarationStatementContext ctx){
        printContextText(ctx);
    }

    // @Override
    // public void enterStatement(StatementContext ctx){
    //     printContextText(ctx);
    // }

    // @Override 
    // public void enterMethodInvocation(MethodInvocationContext ctx){
    //     String name = "methodInvoc";
    //     String body = ctx.getText();
    //     qtest(name, body);
    //     RuleContext rctx = ctx;
    //     while(rctx.parent != null){
    //         System.out.println(rctx.parent.getClass().getSimpleName()+" "+rctx.parent.getChildCount());
    //         rctx = rctx.parent;
    //     }
    // }

    public void printContextText(ParseTree ctx){
        for(int i=0; i<ctx.getChildCount(); i++){
            ParseTree child = ctx.getChild(i);
            System.out.println("ctx: "+child.getClass().getSimpleName()+
            "\n\tcontent: "+child.getText()+
            "\n\t\tparent: "+child.getParent().getClass().getSimpleName()+"\n");
            printContextText(child);
        }
    }

    public String getDeepestContext(ParseTree ctx){
        while(ctx.getChildCount() > 0){
            if(ctx.getChild(0) instanceof TerminalNodeImpl) break;
            ctx = ctx.getChild(0);
        }
        return ctx.getClass().getSimpleName();
    }

    public void printChildContexts(ParseTree ctx){
        System.out.println("\nCCONTEXTS: "+ctx.getText());
        for(int j=0; j<ctx.getChildCount(); j++){
            ParseTree tx = ctx.getChild(j);
            System.out.println("child: "+tx.getClass().getSimpleName());
            for(int i=0; i<tx.getChildCount(); i++){
                ParseTree x = tx.getChild(i);
                System.out.println("child child: "+x.getClass().getSimpleName());
            }
            System.out.println("first children: ");
            while(ctx.getChildCount()>0){
                System.out.println(ctx.getChild(0).getClass().getSimpleName());
                ctx = ctx.getChild(0);
            }
            System.out.print("\n");    
        }
    }

    public void printParentContexts(ParseTree ctx){
        System.out.println("\nPCONTEXTS: "+ctx.getText());
        while(ctx.getParent() != null){
            System.out.println(ctx.getClass().getSimpleName());
            ctx = ctx.getParent();
        }
    }

    // @Override
    // public void enterExpression(ExpressionContext ctx){
    //     String name = "exp";
    //     String body = ctx.getText();
    //     qtest(name, body);
    // }

    // @Override
    // public void enterExpressionStatement(ExpressionStatementContext ctx){
    //     String name = "expStatement";
    //     String body = ctx.statementExpression().getText();
    //     qtest(name, body);
    // }

    // @Override
    // public void enterIfThenStatement(IfThenStatementContext ctx){
    //     String name = "ifStatement";
    //     String body = ctx.getText();
    //     System.out.println(ctx.expression().getText()+"\n"+ctx.statement().getText());
    //     qtest(name, body);
    // }


    @Override
    public void enterNormalClassDeclaration(NormalClassDeclarationContext ctx) {
        if(this.root==null) {
            try {
                this.root = makeClassOrInterfaceNode(ctx);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.compilationUnit.setClassOrInterfaceDeclaration(this.root);
        }
    }

    @Override
    public void enterNormalInterfaceDeclaration(NormalInterfaceDeclarationContext ctx) {
        if(this.root==null) {
            try {
                this.root = makeClassOrInterfaceNode(ctx);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.compilationUnit.setClassOrInterfaceDeclaration(this.root);
        }
    }

    private ClassOrInterfaceNode makeClassOrInterfaceNode(ParserRuleContext ctx) throws InterruptedException{
        ClassOrInterfaceNode node = null;
        if(ctx instanceof NormalClassDeclarationContext){
            NormalClassDeclarationContext context = (NormalClassDeclarationContext)ctx;
            node = new ClassOrInterfaceNode(null, 
                                            ((NormalClassDeclarationContext) ctx)
                                            .identifier()
                                            .getText());
            if(context.superinterfaces()!=null){
                List<ClassOrInterfaceNode> interfaces = new ArrayList<>();
                // context.superinterfaces()
                //         .interfaceTypeList()
                //         .interfaceType()
                //         .forEach(i->interfaces.add(
                //             new ClassOrInterfaceNode(this.classOrInterface, i.classType()
                //                                                 .identifier()
                //                                                 .getText())));
                context.superinterfaces().interfaceTypeList().interfaceType().forEach(i->{
                    try {
                        interfaces.add(
                            makeClassOrInterfaceNode(i)
                        );
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
                //might need an ast builder
                //ast builder so that superclass ast exists, but the ast shouldn't be nested in the class that extends its ast
                //TODO PRIO1 - fully build interface/super nodes, need to context them to their source to populate data
                //TODO PRIO1 - probs needs SuperClassNode and SuperInterfaceNode 
                node.addInterfaces(interfaces);
            }
            if(context.superclass()!=null){
                node.setSuperclass(new ClassOrInterfaceNode(this.root, 
                                                            context.superclass()
                                                                    .classType()
                                                                    .identifier()
                                                                    .getText()));
            }
            List<String> mods = new ArrayList<>();
            List<String> anns = new ArrayList<>();
            modifierContextsToStrings(context.classModifier()).forEach(mod->{
                if(mod.contains("@"))
                    anns.add(mod);
                else
                    mods.add(mod);});
            node.addAnnotations(anns);
            node.addModifiers(mods);
            
        } else if(ctx instanceof NormalInterfaceDeclarationContext){
            NormalInterfaceDeclarationContext context = (NormalInterfaceDeclarationContext)ctx;
            node = new ClassOrInterfaceNode(null, 
                                            ((NormalInterfaceDeclarationContext) ctx)
                                            .identifier()
                                            .getText());
            node.addModifiers(modifierContextsToStrings(context.interfaceModifier()));
        }
        return node;
    }

    @Override
    public void enterClassMemberDeclaration(ClassMemberDeclarationContext ctx){
        if(ctx.methodDeclaration()!=null){
            memberDeclarationMethodHelper(ctx);
        } else if (ctx.fieldDeclaration()!=null){
            memberDeclarationFieldHelper(ctx);
        }
    }

    @Override
    public void enterInterfaceMemberDeclaration(InterfaceMemberDeclarationContext ctx) {
        if(ctx.interfaceMethodDeclaration()!=null){
            memberDeclarationMethodHelper(ctx);
        } else if (ctx.constantDeclaration()!=null){
            memberDeclarationFieldHelper(ctx);
            
        }
    }

    private void memberDeclarationFieldHelper(ParserRuleContext ctx){
        // if(ctx instanceof ClassMemberDeclarationContext){
        //     // this.root.addField(
        //     //     makeClassMemberFieldNode((ClassMemberDeclarationContext)ctx));
        // } else if(ctx instanceof InterfaceMemberDeclarationContext){
        //     this.root.addField(
        //         makeInterfaceMemberFieldNode((InterfaceMemberDeclarationContext)ctx));
        // }
    }

    // private FieldNode makeClassMemberFieldNode(ClassMemberDeclarationContext ctx){
    //     String name = ctx.fieldDeclaration()
    //                     .variableDeclaratorList()
    //                     .variableDeclarator()
    //                     .get(0)
    //                     .variableDeclaratorId()
    //                     .getText();
    //     String type = ctx.fieldDeclaration()
    //                     .unannType()
    //                     .getText();
    //     FieldNode fieldNode = new FieldNode(this.root, type, name);
    //     List<VariableDeclaratorContext> list = ctx.fieldDeclaration()
    //                                             .variableDeclaratorList()
    //                                             .variableDeclarator();
    //     if(list.get(0).variableInitializer()!=null)
    //         fieldNode.setValue(list.get(0)
    //                             .variableInitializer()
    //                             .getText()
    //                             .replaceFirst("new", "new "));
    //     fieldNode.addModifiers(modifierContextsToStrings(ctx.fieldDeclaration()
    //                                                         .fieldModifier()));
    //     return fieldNode;
    // }

    // private FieldNode makeInterfaceMemberFieldNode(InterfaceMemberDeclarationContext ctx){
    //     String name = ctx.constantDeclaration()
    //                     .variableDeclaratorList()
    //                     .variableDeclarator()
    //                     .get(0)
    //                     .variableDeclaratorId()
    //                     .getText();
    //     String type = ctx.constantDeclaration()
    //                     .unannType()
    //                     .getText();
    //     FieldNode fieldNode = new FieldNode(this.root, type, name);
    //     List<VariableDeclaratorContext> list = ctx.constantDeclaration()
    //                                             .variableDeclaratorList()
    //                                             .variableDeclarator();
    //     if(list.get(0).variableInitializer()!=null)
    //     fieldNode.setValue(list.get(0)
    //                             .variableInitializer()
    //                             .getText()
    //                             .replaceFirst("new", "new "));
    //     fieldNode.addModifiers(modifierContextsToStrings(ctx.constantDeclaration()
    //                                                         .constantModifier()));
    //     return fieldNode;
    // }

    private void memberDeclarationMethodHelper(ParserRuleContext ctx){
        if(ctx instanceof ClassMemberDeclarationContext){
            this.root.addMethod(
                makeClassMemberMethodNode((ClassMemberDeclarationContext)ctx));
        } else if(ctx instanceof InterfaceMemberDeclarationContext){
            this.root.addMethod(
                makeInterfaceMemberMethodNode((InterfaceMemberDeclarationContext)ctx));
        }
    }

    private MethodNode makeClassMemberMethodNode(ClassMemberDeclarationContext ctx){
        String name = ctx.methodDeclaration()
                        .methodHeader()
                        .methodDeclarator()
                        .identifier()
                        .getText();
        MethodNode methodNode = new MethodNode(this.root, name);
        if(ctx.methodDeclaration().methodBody().block().blockStatements() != null){
            List<BlockStatementContext> blocks = ctx.methodDeclaration()
                                                    .methodBody()
                                                    .block()
                                                    .blockStatements()
                                                    .blockStatement();
            makeBlockNodes(blocks, methodNode);
        }
        modifierContextsToStrings(ctx.methodDeclaration()
                                    .methodModifier()).forEach(mod->{
                                        if(mod.contains("@"))
                                            methodNode.addAnnotation(mod);
                                        else
                                            methodNode.addModifier(mod);});
        FormalParameterListContext params = ctx.methodDeclaration()
                                                .methodHeader()
                                                .methodDeclarator()
                                                .formalParameterList();
        if(params != null) 
            methodNode.addParameters(parameterContextsToStrings(params));
        methodNode.setReturnType(ctx.methodDeclaration()
                                    .methodHeader()
                                    .result()
                                    .getText());
        return methodNode;
    }

    private MethodNode makeInterfaceMemberMethodNode(InterfaceMemberDeclarationContext ctx){
        String name = ctx.interfaceMethodDeclaration()
                        .methodHeader()
                        .methodDeclarator()
                        .identifier()
                        .getText();
        MethodNode methodNode = new MethodNode(this.root, name);
        List<BlockStatementContext> blocks = ctx.interfaceMethodDeclaration()
                                                .methodBody()
                                                .block()
                                                .blockStatements()
                                                .blockStatement();
        blocks.forEach(block->methodNode.addBlock(new BlockNode(methodNode, block.getText())));
        methodNode.addModifiers(modifierContextsToStrings(ctx.interfaceMethodDeclaration()
                                                            .interfaceMethodModifier()));
        FormalParameterListContext params = ctx.interfaceMethodDeclaration()
                                                .methodHeader()
                                                .methodDeclarator()
                                                .formalParameterList();
        if(params != null) 
            methodNode.addParameters(parameterContextsToStrings(params));
        methodNode.setReturnType(ctx.interfaceMethodDeclaration()
                                    .methodHeader()
                                    .result()
                                    .getText());
        return methodNode;
    }

    private void makeBlockNodes(List<BlockStatementContext> blocks, MethodNode method){
        List<BlockNode> blockNodes = new ArrayList<>();
        for(BlockStatementContext block : blocks){
            boolean foundType = false;
            ParseTree ctx = (ParseTree) block;
            BlockNode blockNode = new BlockNode(method, ctx.getText());
            while(!foundType){
                if(ctx instanceof AssignmentContext){
                    blockNode.setType(BlockType.EXPR_ASSN);
                    foundType = true;
                } else if(ctx instanceof LocalVariableDeclarationStatementContext){
                    blockNode.setType(BlockType.VAR_DEC);
                    foundType = true;
                } else if(ctx instanceof MethodInvocationContext){
                    blockNode.setType(BlockType.EXPR_METHINVOC);
                    foundType = true;
                } else if(ctx instanceof TerminalNodeImpl){
                    break;
                }
                ctx = ctx.getChild(0);
            }
            method.addBlock(blockNode);
        }
    }

    private List<List<String>> parameterContextsToStrings(FormalParameterListContext params){
        List<List<String>> strings = new ArrayList<>();
        if(params.formalParameters()!=null&&params.formalParameters().formalParameter()!=null){
            List<FormalParameterContext> paramList = params.formalParameters()
                                                            .formalParameter();
            paramList.add(params.lastFormalParameter().formalParameter());
            
            for(int i=0; i<paramList.size(); i++){
                FormalParameterContext ctx = paramList.get(i);
                strings.add(i, new ArrayList<String>());
                
                for(int j=0; j<ctx.variableModifier().size(); j++)
                    strings.get(i).add(ctx.variableModifier().get(j).getText());
                
                strings.get(i).add(ctx.unannType().getText());
                strings.get(i).add(ctx.variableDeclaratorId().getText());
            }
        }

        return strings;
    }

    private List<String> modifierContextsToStrings(List<? extends ParserRuleContext> mods){
        List<String> strings = new ArrayList<>();
        mods.forEach(mod->strings.add(mod.getText()));
        return strings;
    }

    public AST buildAST() throws IOException {
        //AST is a wrapper class, adds functionality
        CharStream input = CharStreams.fromPath(this.path);
        Java9Lexer lexer = new Java9Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java9Parser parser = new Java9Parser(tokens);
        ParseTree tree = parser.compilationUnit();
        ParseTreeWalker.DEFAULT.walk(this, tree);
        return new AST(this.compilationUnit);
    }

    public CompilationUnit getRoot(){
        return this.compilationUnit;
    }

    @Override
    public String toString(){
        return this.compilationUnit.toString();
    }
}
