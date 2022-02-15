package org.msehgal.codevis.AST.nodes.statements.expressions;

import org.msehgal.codevis.AST.nodes.Node;

public class IncrementNode extends ExpressionNode{
    
    private ExpressionNode value;

    private int loc;

    public IncrementNode(Node parent){
        super(parent);
    }


}
