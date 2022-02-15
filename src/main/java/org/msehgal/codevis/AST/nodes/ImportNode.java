package org.msehgal.codevis.AST.nodes;

public class ImportNode extends Node {

    private String name;
    
    private String qualifiedName;

    public ImportNode(Node parent) {
        super(parent);
        // TODO implement
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
        String[] arr = qualifiedName.split("\\.");
        this.name = arr[arr.length-1];
    }

}
