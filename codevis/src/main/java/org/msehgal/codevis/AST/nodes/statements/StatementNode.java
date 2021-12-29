package org.msehgal.codevis.AST.nodes.statements;

import org.msehgal.codevis.AST.nodes.Node;

public class StatementNode extends Node{

    private StatementNode child;

    public StatementNode(Node parent){
        super(parent);
    }

    public StatementNode(Node parent, StatementNode child){
        super(parent);
        this.child = child;
    }
    
    public StatementNode getChild() {
        return child;
    }

    public void setChild(StatementNode child) {
        this.child = child;
    }
    
}
