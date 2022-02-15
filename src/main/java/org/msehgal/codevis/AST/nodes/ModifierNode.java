package org.msehgal.codevis.AST.nodes;

public class ModifierNode extends Node {
    
    public String id;

    public ModifierNode(Node parent){
        super(parent);
    }

    public ModifierNode(Node parent, String type) {
        super(parent, type);
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

}
