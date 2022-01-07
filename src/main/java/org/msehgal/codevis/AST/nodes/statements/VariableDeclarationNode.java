package org.msehgal.codevis.AST.nodes.statements;

import java.util.ArrayList;
import java.util.List;

import org.msehgal.codevis.AST.nodes.Node;
import org.msehgal.codevis.AST.nodes.statements.expressions.AtomNode;

public class VariableDeclarationNode extends StatementNode{
    
    private List<StatementNode> children = new ArrayList<>();
    
    private AtomNode className;

    private boolean foundQualifiedName = false;
    
    private AtomNode name;
    
    private AtomNode[] params;

    public VariableDeclarationNode(Node parent){
        super(parent);
    }

    public boolean addChild(StatementNode child){
        return this.children.add(child);
    }

    @Override
    public String[][] evaluate(){
        String[][] res = new String[this.children.size()][];
        // for(int i=0; i<this.children.size(); i++){
        //     StatementNode child = this.children.get(i);
        //     res[i] = child.evaluate()[0];
        // }
        res[0] = this.className.evaluate()[0];
        res[1] = this.name.evaluate()[0];
        for(int i=2, j=0; i<params.length; i++){
            res[i] = params[j].getContent();
        }
        return res;
    }

    public AtomNode getClassName() {
        return className;
    }

    public void setClassName(AtomNode className) {
        if(className.getAtom().contains(".")){
            this.foundQualifiedName = true;
        }
        this.className = className;
        this.addChild(className);
    }

    public boolean getFoundQualifiedName(){
        return this.foundQualifiedName;
    }
    
    public AtomNode getName() {
        return name;
    }

    public void setName(AtomNode name) {
        this.name = name;
        this.addChild(name);
    }

    public AtomNode[] getParams() {
        return params;
    }

    // private String[] getParamsAsStrings() {
    //     String[] res = new String[params.length*2];
    //     int pointer = 0;
    //     for(int i=0; i<params.length; i++){
    //         res[i] = params[pointer];
    //     }
    // }

    public void setParams(AtomNode... params) {
        this.params = params;
        for(AtomNode param : params) this.addChild(param);
    }



    
}
