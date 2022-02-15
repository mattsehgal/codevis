package org.msehgal.codevis.AST.nodes;

public class PackageNode extends Node {

    private String name;

    public PackageNode(Node parent){
        super(parent);
    }

    public PackageNode(Node parent, String name) {
        super(parent, name);
        // TODO implement
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
