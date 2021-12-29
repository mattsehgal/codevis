package org.msehgal.codevis.AST.nodes.statements.expressions;

import org.msehgal.codevis.AST.nodes.Node;

public class AssignmentNode extends ExpressionNode{
    
    private Node leftHandSide;

    private Node operator;

    private ExpressionNode expression;

    public AssignmentNode(Node parent){
        super(parent);
    }

    public AssignmentNode(Node parent, Node lhs, Node op, ExpressionNode expr){
        super(parent);
        this.leftHandSide = lhs;
        this.operator = op;
        this.expression = expr;
    }

    public Node getLeftHandSide() {
        return leftHandSide;
    }

    public void setLeftHandSide(Node leftHandSide) {
        this.leftHandSide = leftHandSide;
    }

    public Node getOperator() {
        return operator;
    }

    public void setOperator(Node operator) {
        this.operator = operator;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public String toString(){
        String lhs = leftHandSide.toString();
        String op = operator.toString();
        String exp = expression.toString();
        return lhs+" "+
                op+" "+
                exp;
    }

    
}
