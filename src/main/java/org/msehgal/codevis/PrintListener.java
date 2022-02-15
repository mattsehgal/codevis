package org.msehgal.codevis;

import java.util.ArrayList;

import org.antlr.v4.runtime.*;
import org.msehgal.codevis.antlr.*;

public class PrintListener extends Java9BaseListener {

    private ArrayList<String> test = new ArrayList<>();

    private ArrayList<String> methods = new ArrayList<>();

    private ArrayList<String> variables = new ArrayList<>();

    private <T extends ParserRuleContext> void addNodeToList(ParserRuleContext ctx){
        String name = ctx.getText();
        if(ctx instanceof Java9Parser.MethodDeclaratorContext)
            methods.add(name);
        else if (ctx instanceof Java9Parser.VariableDeclaratorContext)    
            variables.add(name);
    }

    public ArrayList<String> getMethods(){
        return this.methods;
    }

    public ArrayList<String> getVariables(){
        return this.variables;
    }

    public ArrayList<String> getTest(){
        return this.test;
    }

    @Override
    public void enterMethodDeclarator(Java9Parser.MethodDeclaratorContext ctx){
        this.addNodeToList(ctx);
    }

    @Override
    public void enterVariableDeclarator(Java9Parser.VariableDeclaratorContext ctx) {
        this.addNodeToList(ctx);
    }
}
