package org.msehgal.codevis.AST.nodes;

public class TypeNode extends Node {

    public TypeNode(Node parent) {
        super(parent);
    }
    
    public TypeNode(Node parent, String type) {
        super(parent, type);
    }
}
