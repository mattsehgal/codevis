package org.msehgal.codevis.AST.nodes;

public class VariableNode extends DeclarationNode {

    private TypeNode type;
    private Object value;

    public VariableNode(Node parent, String type, String name) {
        super(parent, name);
        this.type = new TypeNode(this, type);
    }

    public TypeNode getType(){
        return this.type;
    }

    public Object getValue(){
        return this.value;
    }

    public void setType(String type){
        TypeNode typeNode = new TypeNode(this, type);
        this.type = typeNode;
        addChildren(typeNode);
    }

    public void setValue(Object value){
        this.value = value;
    }
    
}
