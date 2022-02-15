package org.msehgal.codevis.visualization;

public class RunObject<T> extends Object{
    
    private Object object;

    private String className;

    private Class<T> clazz;

    public RunObject(Object obj, String cName, Class<T> clazz){
        this.object = obj;
        this.className = cName;
        this.clazz = clazz;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object obj) {
        this.object = obj;
    }

    public Class<T> getObjectClass() {
        return this.clazz;
    }

    public T getObjectAsClass(){
        return this.clazz.cast(this.object);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString(){
        return clazz.getName()+" "+className+" = "+object;
    }
    


}
