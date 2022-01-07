package org.msehgal.codevis.AST.nodes.statements.expressions;

import org.msehgal.codevis.AST.nodes.statements.StatementNode;

import java.util.ArrayList;
import java.util.List;

import org.msehgal.codevis.AST.nodes.Node;

public class AssignmentNode extends ExpressionNode{
    
    private List<StatementNode> children = new ArrayList<>();

    private StatementNode leftHandSide;

    private StatementNode operator;

    private ExpressionNode expression;

    public AssignmentNode(Node parent){
        super(parent);
    }

    public boolean addChild(StatementNode child){
        return this.children.add(child);
    }

    public Node getLeftHandSide() {
        return leftHandSide;
    }

    public void setLeftHandSide(AtomNode leftHandSide) {
        this.leftHandSide = leftHandSide;
        this.addChild(leftHandSide);
    }

    public Node getOperator() {
        return operator;
    }

    public void setOperator(AtomNode operator) {
        this.operator = operator;
        this.addChild(operator);
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
        this.addChild(expression);
    }

    @Override
    public String[][] evaluate(){
        String[][] res = new String[this.children.size()][];
        // for(int i=0; i<this.children.size(); i++){
        //     StatementNode child = this.children.get(i);
        //     res[i] = child.evaluate()[0];
        // }
        res[0] = this.leftHandSide.evaluate()[0];
        res[1] = this.operator.evaluate()[0];
        res[2] = this.expression.evaluate()[0];
        return res;
    }

    @Override
    public String toString(){
        String lhs = (leftHandSide == null) ? "null" : leftHandSide.toString();
        String op = (operator == null) ? "null" : operator.toString();
        String exp = (expression == null) ? "null" : expression.toString();
        return lhs+" "+
                op+" "+
                exp;
    }

    
}
