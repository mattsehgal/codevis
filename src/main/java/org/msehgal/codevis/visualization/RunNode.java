package org.msehgal.codevis.visualization;

import java.util.List;

import org.msehgal.codevis.AST.nodes.BlockNode;
import org.msehgal.codevis.AST.nodes.statements.StatementNode;
import org.msehgal.codevis.AST.nodes.statements.VariableDeclarationNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.AssignmentNode;

public class RunNode {
    private StepNode step;
    private RunNode previous;
    private RunNode next;
    private String content;
    
    public RunNode(String content){
        this.content = content;
    }

    public void parseBlock(BlockNode block){

    }

    public StepNode getStep(){
        return this.step;
    }

    public void setStep(StepNode step){
        this.step = step;
    }

    public RunNode getNext(){
        return this.next;
    }

    public RunNode getPrevious(){
        return this.previous;
    }

    public void setNext(RunNode next){
        this.next = next;
    }

    public void setPrevious(RunNode prev){
        this.previous = prev;
    }
}
