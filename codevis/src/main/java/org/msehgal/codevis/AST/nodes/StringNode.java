package org.msehgal.codevis.AST.nodes;

public class StringNode extends Node {

    private String string;

    public StringNode(Node parent, String string) {
        super(parent);
        this.string = string;
    }

    @Override
    public String toString(){
        return this.string;
    }
    
}
