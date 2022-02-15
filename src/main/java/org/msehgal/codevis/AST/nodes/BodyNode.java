package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BodyNode extends Node {
    //expr nodes?
    //stmnt nodes?
    private String content;

    List<BlockNode> blocks = new ArrayList<>();

    public BodyNode(Node parent) {
        super(parent);
    }

    public BodyNode(Node parent, String content){
        super(parent);
        this.content = content;
    }

    public void addBlock(BlockNode block){
        this.blocks.add(block);
    }

    public void addBlocks(List<BlockNode> blocks){
        blocks.forEach(block->this.blocks.add(block));
    }

    public List<BlockNode> getBlocks(){
        return this.blocks;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // @Override
    // public String toString(){
    //     String str = "";
    //     blocks.forEach(block->str.concat(block.getText()+", "));
    //     return "BodyNode: "+str;
    // }

    
}
