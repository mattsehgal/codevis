package org.msehgal.codevis.visualization;

import java.util.HashMap;
import java.util.Map;

public class RunState<T> {

    private Map<String, RunObject<T>> objects = new HashMap<>();

    //TODO implement line number locator
    private int loc;

    public RunState(){

    }

    public void addObject(String id, Object object, String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        RunObject<T> runObj = new RunObject<>(object, className, clazz);
        this.objects.put(id, runObj);
    }

    //perhaps obscure runobject and only deal in objects and T?
    public RunObject<T> getRunObject(String id){
        return this.objects.get(id);
    }

    public Map<String, RunObject<T>> getObjects() {
        return objects;
    }

    public void setObjects(Map<String, RunObject<T>> objects) {
        this.objects = objects;
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        for(String key : this.objects.keySet()){
            res.append(key+": "+this.objects.get(key).toString());
        }
        return res.toString();
    }
}
