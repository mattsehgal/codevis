package org.msehgal.codevis.visualization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.msehgal.codevis.AST.AST;
import org.msehgal.codevis.AST.ASTForest;
import org.msehgal.codevis.AST.nodes.Node;

//tree of run nodes -- might be better as runprocessor
public class RunTree {
    private RunNode root;
    //private Queue<StepNode> steps;
    private RunState currState = new RunState();
    private RunState prevState = null;

    private ASTForest asts;

    public RunTree(RunNode root, ASTForest asts){
        this.root = root;
        this.asts = asts;
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
                    
                    processStep(step);

                    for(int i=0; i<params.length; i++){
                        if(params[i].length == res.length-2){
                            idx = i;
                            break;
                        }
                    }
                    
                    //TODO in the future this can't check just first param as null
                    if("null".equals(res[2]) || res[2] == null){
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

                    for(AST ast : asts.getTrees()){
                        if(ast.getRoot().getClassDeclaration().getId().equals(className)){
                            System.out.println("yeet");
                        } else {
                            System.out.println("not yeet");
                        }
                    }
                    
                    //TODO in the future this can't check just first param as null
                    if("null".equals(res[2])){
                        object = Class.forName(className).getConstructor().newInstance();
                    } else {
                        object = Class.forName(className).getConstructor(params[idx]).newInstance(res[2]);
                    }
                }
                String refName = res[1][0];
                currState.addObject(refName, object, className);
                //System.out.println("RUNTREE: CREATEOBJ: "+refName+" "+className);
            } else if(step.getType() == StepType.CHANGE_REF){
    
            } else if(step.getType() == StepType.UPDATE_VAL){
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

    private <T> void processStep(StepNode step) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
        String[][] res = step.getResult();
        //res format obj: 0 = className, 1 = refName, 2+ = params
        if(step.getType() == StepType.CREATE_OBJ){
            String className = res[0][0];
            String refName = res[1][0];
            Class[] parameters = new Class[res.length-2];
            //TODO what to do with params
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
                if("null".equals(res[2]) || res[2] == null){
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
                String[] correctParams = getCorrectParameters(params, res, className);
                //TODO write parameter solver
                for(int i=0; i<params.length; i++){
                    if(params[i].length == res.length-2){
                        idx = i;
                        break;
                    }
                }

                for(AST ast : asts.getTrees()){
                    if(ast.getRoot().getClassDeclaration().getId().equals(className)){
                        System.out.println("yeet");
                    } else {
                        System.out.println("not yeet");
                    }
                }
                
                //TODO in the future this can't check just first param as null
                if("null".equals(res[2])){
                    object = Class.forName(className).getConstructor().newInstance();
                } else {
                    object = Class.forName(className).getConstructor(params[idx]).newInstance(res[2]);
                }
            }
            currState.addObject(refName, object, className);
            //System.out.println("RUNTREE: CREATEOBJ: "+refName+" "+className);
        } else if(step.getType() == StepType.UPDATE_VAL){
            String refName = res[0][0];
            RunObject<T> obj = currState.getRunObject(refName);
            //TODO quote stripping has to happen earlier
            obj.setObject(res[2][0].replace("\"", ""));

            
            //call method from res[] too
            //System.out.println("RUNTREE: MODREF: "+refName+" "+Arrays.toString(res));
            
        } else {
            //error handle for null steptype
        }
    }

    //gets all params from all constructors for Class className
    private Class[][] getParameters(String className) throws SecurityException, ClassNotFoundException{
        Constructor[] cons = Class.forName(className).getConstructors();
        Class[][] params = new Class[cons.length][];
        for(int i=0; i<cons.length; i++){
            params[i] = cons[i].getParameterTypes();
        }
        return params;
    }

    private String[] getCorrectParameters(Class[][] params, String[][] res, String className){
        Object object = new Object();
        int idx = 0;
        //[[t,n][t,n][t,n]], [[]], ...
        List<String[][]> resParams = getParamsAsList(res);
        //[[t,t,t][t,t,...][...]]
        String[][] paramsClassNames = new String[params.length][];
        for(int i=0; i<params.length; i++){
            String[] paramClassNames = new String[params[i].length];
            for(int j=0; j<params[i].length; j++){
                paramClassNames[j] = params[i][j].getCanonicalName();
            }
            paramsClassNames[i] = paramClassNames;
        }

        for(String[][] paramsArray : resParams){
            boolean correctParams = false;
            int i = 0;
            while(!correctParams){
                String[] p = new String[paramsArray[i].length];
                String[] cn = paramsClassNames[i];
                for(int j=0; j<p.length; j++){
                    p[j] = paramsArray[j][0];
                }
                Arrays.sort(p);
                Arrays.sort(cn);
                if(p.equals(cn)){
                    return paramsArray[i];
                }
                i++;
            }
        }

        for(int i=0; i<params.length; i++){
            if(params[i].length == res.length-2){
                idx = i;
                break;
            }
        }
        //get params from ASTs
        for(AST ast : asts.getTrees()){
            if(ast.getRoot().getClassDeclaration().getId().equals(className)){
                System.out.println("yeet");
            } else {
                System.out.println("not yeet");
            }
        }
        return null;
    }

    private List<String[][]> getParamsAsList(String[][] res){
        String[][] resParams = Arrays.copyOfRange(res, 2, res.length-1);
        //List([[type, name], [type, name], [...]], [[...]] )
        List<String[][]> params = new ArrayList<>();
        //iterates for each array of resParams: [type, name, type, name, ...][...][...]
        for(int i=0; i<resParams.length; i++){
            //iterates through each resParams[i]: [type, name, ...]
            String[][] reformattedParams = new String[resParams[i].length/2][];
            for(int j=0; j<resParams[i].length; j+=2){
                String[] param = new String[2];
                param[0] = resParams[i][j];
                param[1] = resParams[i][j+1];
                reformattedParams[i] = param;
            }
            params.add(reformattedParams);
        }
        return params;
    }



    private void updateStates(){
        
    }
}
