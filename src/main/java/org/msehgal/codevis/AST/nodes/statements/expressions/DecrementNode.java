package org.msehgal.codevis.AST.nodes.statements.expressions;

import org.msehgal.codevis.AST.nodes.Node;

public class DecrementNode extends ExpressionNode{
    
    private ExpressionNode value;

    private int loc;

    public DecrementNode(Node parent){
        super(parent);
    }


}
