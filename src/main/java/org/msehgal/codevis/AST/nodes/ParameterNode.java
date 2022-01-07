package org.msehgal.codevis.AST.nodes;

public class ParameterNode extends VariableNode {

    public ParameterNode(Node parent, String type, String name) {
        super(parent, type, name);
        //TODO PRIO0 - this node will need to handle non-variable input, ie anonymous objects
    }

    /*
    @Override 
    public String toString(){
        return this.getType()+" "+this.getName();
    }
    */
    
}
