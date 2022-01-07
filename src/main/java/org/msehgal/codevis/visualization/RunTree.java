package org.msehgal.codevis.visualization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.msehgal.codevis.AST.nodes.Node;

//tree of run nodes -- might be better as runprocessor
public class RunTree {
    private RunNode root;
    //private Queue<StepNode> steps;
    private RunState currState = new RunState();
    private RunState prevState = null;

    public RunTree(RunNode root){
        this.root = root;
    }

    //logic might be in RunVis instead
    public void processTree() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
        RunNode curr = this.root;
        while(curr != null){
            Queue<StepNode> steps = new LinkedList<>();
            StepNode step = curr.getStep();
            
            while(step != null){
                steps.add(step);
                //evaluateStep(step);
                step = step.getNext();
            }

            update(steps);
            curr = curr.getNext();
        }
    }

    public void evaluateStep(StepNode step){

    }

    public <T> void update(Queue<StepNode> steps) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
        while(!steps.isEmpty()){
            StepNode step = steps.poll();
            String[][] res = step.getResult();
            if(step.getType() == StepType.CREATE_OBJ){
                String className = res[0][0];
                //needs to be solved by using parameters in vardec
                Object object = new Object();
                try{
                    Class[][] params = getParameters(className);
                    int idx = 0;
                    for(int i=0; i<params.length; i++){
                        if(params[i].length == res.length-2){
                            idx = i;
                            break;
                        }
                    }
                    //TODO in the future this can't check just first param as null
                    if(res[2].equals("null")){
                        object = Class.forName(className).getConstructor().newInstance();
                    } else {
                        object = Class.forName(className).getConstructor(params[idx]).newInstance(res[2]);
                    }
                } catch (ClassNotFoundException e) {
                    //TODO this needs to happen when finding className
                    className = "java.lang."+className;
                    // Object[] params = Class.forName(className).getConstructor().getParameterTypes();
                    // Class[] paramClasses = new Class[params.length];
                    // for(int i=0; i<params.length; i++){
                    //     paramClasses[i] = params[i].getClass();
                    // }
                    // //Object paramType = Class.forName(className).getConstructor().newInstance();
                    // object = Class.forName(className).getConstructor(paramClasses).newInstance(res[2]);
                    Class[][] params = getParameters(className);
                    int idx = 0;
                    //TODO write parameter solver
                    for(int i=0; i<params.length; i++){
                        if(params[i].length == res.length-2){
                            idx = i;
                            break;
                        }
                    }
                    //TODO in the future this can't check just first param as null
                    if(res[2].equals("null")){
                        object = Class.forName(className).getConstructor().newInstance();
                    } else {
                        object = Class.forName(className).getConstructor(params[idx]).newInstance(res[2]);
                    }
                }
                String refName = res[1][0];
                currState.addObject(refName, object, className);
                //System.out.println("RUNTREE: CREATEOBJ: "+refName+" "+className);
            } else if(step.getType() == StepType.MODIFY_REF){
                String refName = res[0][0];
                RunObject<T> obj = currState.getRunObject(refName);
                //TODO quote stripping has to happen earlier
                obj.setObject(res[2][0].replace("\"", ""));

                
                //call method from res[] too
                //System.out.println("RUNTREE: MODREF: "+refName+" "+Arrays.toString(res));
                
            } else {
                //error handle for null steptype
            }
            System.out.println("RUNSTATE: "+this.currState.toString());   
        }
    }

    private Class[][] getParameters(String className) throws SecurityException, ClassNotFoundException{
        Constructor[] cons = Class.forName(className).getConstructors();
        Class[][] params = new Class[cons.length][];
        for(int i=0; i<cons.length; i++){
            params[i] = cons[i].getParameterTypes();
        }
        return params;
    }

    private void updateStates(){
        
    }
}
