package org.msehgal.codevis.AST.nodes.statements;

import java.util.ArrayList;

import org.msehgal.codevis.AST.nodes.Node;

public class StatementNode extends Node{

    private ArrayList<StatementNode> children = new ArrayList<>();

    public StatementNode(Node parent){
        super(parent);
    }

    public boolean addChild(StatementNode child){
        return this.children.add(child);
    }
    
    public String[][] evaluate(){return null;};

    
}
