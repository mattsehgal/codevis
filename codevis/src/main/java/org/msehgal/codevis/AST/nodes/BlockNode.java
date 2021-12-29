package org.msehgal.codevis.AST.nodes;

import org.msehgal.codevis.AST.nodes.statements.StatementNode;

public class BlockNode extends Node{
    
    private BlockType type;
    //contains expression nodes

    private StatementNode statement;

    public BlockNode(Node parent){
        super(parent);
    }

    public BlockNode(Node parent, String text){
        //Blocknodes should be more specific obj like ExprNode?
        super(parent, text);
    }

    public StatementNode getStatement() {
        return statement;
    }

    public void setStatement(StatementNode statement) {
        this.statement = statement;
    }

    public BlockType getType(){
        return this.type;
    }

    public void setType(BlockType type){
        this.type = type;
    }

    @Override
    public String toString(){
        return this.getText()+": "+this.type;
    }
}
