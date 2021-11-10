package org.msehgal.codevis.AST.nodes;

public class FieldNode extends VariableNode {

    public FieldNode(Node parent, String type, String name) {
        super(parent, type, name);
    }

    /*
    @Override
    public String toString(){
        return "\nNODE{\nFieldDeclaration: "+super.toString()+
                "\nType: "+this.getType()+
                "\nModifiers: "+this.getModifiers()+
                "\nValue: "+this.getValue()+
                "\n}";     
    }
    */
    
}
