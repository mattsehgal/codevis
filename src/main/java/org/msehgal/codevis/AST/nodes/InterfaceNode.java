package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.List;

public class InterfaceNode extends Node{
    private ModifierNode interfaceModifier;

    private String id;
    //no type params
    
    private List<InterfaceNode> superinterfaces = new ArrayList<>();

    private List<FieldNode> constants = new ArrayList<>();
    
    private List<MethodNode> methods = new ArrayList<>();

    public InterfaceNode(Node parent) {
        super(parent);
    }

    public ModifierNode getClassModifier(){
        return this.interfaceModifier;
    }

    public void setClassModifier(ModifierNode interfaceModifier){
        this.interfaceModifier = interfaceModifier;
        addChild(interfaceModifier);
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
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
        this.constants.add(field);
        addChild(field);
    }

    public void addFields(List<FieldNode> fields){
        fields.forEach(field->addField(field));
    }

    public List<FieldNode> getFields(){
        return this.constants;
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
