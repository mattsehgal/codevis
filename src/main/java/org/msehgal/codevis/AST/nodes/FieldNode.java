package org.msehgal.codevis.AST.nodes;

public class FieldNode extends Node{

    private ModifierNode modifier;
    private String id;
    private String type;


    public FieldNode(Node parent){
        super(parent);
    }

    public String getId(){
        return this.id;
    }

    public String getType(){
        return this.type;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return this.id;
    }
    
}
