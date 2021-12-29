package org.msehgal.codevis.visualization;

import java.util.LinkedList;
import java.util.Queue;

public class RunTree {
    private RunNode root;
    //private Queue<StepNode> steps;

    public RunTree(RunNode root){
        this.root = root;
    }

    //logic might be in RunVis instead
    public void processTree(){
        RunNode curr = this.root;
        while(curr.getNext() != null){
            Queue<StepNode> steps = new LinkedList<>();
            StepNode step = curr.getStep();
            
            while(step.getNext() != null){
                steps.add(step);
                evaluateStep(step);
                step = step.getNext();
            }

            update(steps);
            steps.clear();
            curr = curr.getNext();
        }
    }

    public void evaluateStep(StepNode step){

    }

    public void update(Queue<StepNode> steps){

    }
}
