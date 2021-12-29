package org.msehgal.codevis.visualization;

import java.util.List;

public class RunNode {
    private StepNode step;
    private RunNode previous;
    private RunNode next;
    private String content;
    
    public RunNode(String content){
        this.content = content;
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
