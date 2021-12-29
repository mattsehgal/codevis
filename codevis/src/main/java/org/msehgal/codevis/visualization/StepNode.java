package org.msehgal.codevis.visualization;

import org.msehgal.codevis.AST.nodes.Node;

public class StepNode {
    private RunNode run;
    
    private StepNode next;

    private StepType type;

    private Node parent;
    private Node left;
    private Node right;

    public StepNode(RunNode run, StepType type){
        this.run = run;
        this.type = type;
    }

    public StepNode getNext(){
        return this.next;
    }

    public void setNext(StepNode next){
        this.next = next;
    }
}
