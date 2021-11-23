package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.List;

import org.msehgal.codevis.antlr.Java9Parser.*;

public class BodyNode extends Node {
    //expr nodes?
    //stmnt nodes?
    List<BlockNode> blocks = new ArrayList<>();

    public BodyNode(Node parent, BlockStatementsContext ctx) {
        super(parent);
        setBlocks(ctx);
    }

    public List<BlockNode> getBlocks(){
        return this.blocks;
    }

    private void setBlocks(BlockStatementsContext ctx){
        for(BlockStatementContext block : ctx.blockStatement()){
            blocks.add(new BlockNode(this, block.getText()));
        }
    }

    @Override
    public String toString(){
        String str = "";
        blocks.forEach(block->str.concat(block.getName()+", "));
        return "BodyNode: "+str;
    }

    
}
