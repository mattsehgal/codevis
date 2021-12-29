package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.List;

public class ClassNode extends Node{
    private ModifierNode classModifier;

    private String id;
    //no type params

    private ClassNode superclass;
    
    private List<InterfaceNode> superinterfaces = new ArrayList<>();

    private List<FieldNode> fields = new ArrayList<>();
    
    private List<MethodNode> methods = new ArrayList<>();

    public ClassNode(Node parent) {
        super(parent);
    }

    public ModifierNode getClassModifier(){
        return this.classModifier;
    }

    public void setClassModifier(ModifierNode classModifier){
        this.classModifier = classModifier;
        addChild(classModifier);
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public ClassNode getSuperclass() {
        return this.superclass;
    }

    public void setSuperclass(ClassNode superclass) {
        this.superclass = superclass;
        addChild(superclass);
    }

    public List<InterfaceNode> getInterfaces() {
        return this.superinterfaces;
    }

    public void addInterface(InterfaceNode interf){
        this.superinterfaces.add(interf);
        addChild(interf);
    }

    public void addInterfaces(List<InterfaceNode> interfaces){
        interfaces.forEach(interf->addInterface(interf));
    }

    public void addField(FieldNode field){
        this.fields.add(field);
        addChild(field);
    }

    public void addFields(List<FieldNode> fields){
        fields.forEach(field->addField(field));
    }

    public List<FieldNode> getFields(){
        return this.fields;
    }

    public void addMethod(MethodNode method){
        this.methods.add(method);
        addChild(method);
    }

    public void addMethods(List<MethodNode> methods){
        methods.forEach(method->addMethod(method));
    }

    public List<MethodNode> getMethods(){
        return this.methods;
    }
    
    @Override
    public String toString(){
        return id;
    }
}



