package org.msehgal.codevis.AST;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.msehgal.codevis.antlr.Java9Lexer;
import org.msehgal.codevis.antlr.Java9Parser;

import org.msehgal.codevis.AST.nodes.*;
import org.msehgal.codevis.antlr.Java9BaseListener;
import org.msehgal.codevis.antlr.Java9Parser.ClassMemberDeclarationContext;
import org.msehgal.codevis.antlr.Java9Parser.CompilationUnitContext;
import org.msehgal.codevis.antlr.Java9Parser.FormalParameterContext;
import org.msehgal.codevis.antlr.Java9Parser.FormalParameterListContext;
import org.msehgal.codevis.antlr.Java9Parser.InterfaceMemberDeclarationContext;
import org.msehgal.codevis.antlr.Java9Parser.NormalClassDeclarationContext;
import org.msehgal.codevis.antlr.Java9Parser.NormalInterfaceDeclarationContext;
import org.msehgal.codevis.antlr.Java9Parser.PackageDeclarationContext;
import org.msehgal.codevis.antlr.Java9Parser.VariableDeclaratorContext;

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
        if(ctx instanceof ClassMemberDeclarationContext){
            this.root.addField(
                makeClassMemberFieldNode((ClassMemberDeclarationContext)ctx));
        } else if(ctx instanceof InterfaceMemberDeclarationContext){
            this.root.addField(
                makeInterfaceMemberFieldNode((InterfaceMemberDeclarationContext)ctx));
        }
    }

    private FieldNode makeClassMemberFieldNode(ClassMemberDeclarationContext ctx){
        String name = ctx.fieldDeclaration()
                        .variableDeclaratorList()
                        .variableDeclarator()
                        .get(0)
                        .variableDeclaratorId()
                        .getText();
        String type = ctx.fieldDeclaration()
                        .unannType()
                        .getText();
        FieldNode fieldNode = new FieldNode(this.root, type, name);
        List<VariableDeclaratorContext> list = ctx.fieldDeclaration()
                                                .variableDeclaratorList()
                                                .variableDeclarator();
        if(list.get(0).variableInitializer()!=null)
            fieldNode.setValue(list.get(0)
                                .variableInitializer()
                                .getText()
                                .replaceFirst("new", "new "));
        fieldNode.addModifiers(modifierContextsToStrings(ctx.fieldDeclaration()
                                                            .fieldModifier()));
        return fieldNode;
    }

    private FieldNode makeInterfaceMemberFieldNode(InterfaceMemberDeclarationContext ctx){
        String name = ctx.constantDeclaration()
                        .variableDeclaratorList()
                        .variableDeclarator()
                        .get(0)
                        .variableDeclaratorId()
                        .getText();
        String type = ctx.constantDeclaration()
                        .unannType()
                        .getText();
        FieldNode fieldNode = new FieldNode(this.root, type, name);
        List<VariableDeclaratorContext> list = ctx.constantDeclaration()
                                                .variableDeclaratorList()
                                                .variableDeclarator();
        if(list.get(0).variableInitializer()!=null)
        fieldNode.setValue(list.get(0)
                                .variableInitializer()
                                .getText()
                                .replaceFirst("new", "new "));
        fieldNode.addModifiers(modifierContextsToStrings(ctx.constantDeclaration()
                                                            .constantModifier()));
        return fieldNode;
    }

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
