package org.msehgal.codevis.visualization;

public class StepNode {
    
    private StepNode next;

    private StepType stepType;

    private String[][] result;

    public StepNode(StepType type){
        this.stepType = type;
    }

    public String[][] getResult(){
        return this.result;
    }

    public void setResult(String[][] res){
        this.result = res;
    }

    public StepNode getNext(){
        return this.next;
    }

    public void setNext(StepNode next){
        this.next = next;
    }

    public StepType getType() {
        return stepType;
    }

    public void setType(StepType type) {
        this.stepType = type;
    }
}
