package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.List;

public class ClassOrInterfaceNode extends DeclarationNode {

    private List<DeclarationNode> members = new ArrayList<>();
    private List<FieldNode> fields = new ArrayList<>();
    private List<MethodNode> methods = new ArrayList<>();
    private List<ClassOrInterfaceNode> interfaces = new ArrayList<>();
    private ClassOrInterfaceNode superclass;
    private PackageNode paccage;

    public ClassOrInterfaceNode(Node parent, String name) {
        super(parent, name);
    }

    public List<ClassOrInterfaceNode> getInterfaces() {
        return this.interfaces;
    }

    public void addInterface(ClassOrInterfaceNode interf){
        this.interfaces.add(interf);
        addChildren(interf);
    }

    public void addInterfaces(List<ClassOrInterfaceNode> interfaces){
        interfaces.forEach(interf->addInterface(interf));
    }

    public PackageNode getPackage() {
        return this.paccage;
    }

    public void setPackage(PackageNode paccage){
        this.paccage = paccage;
    }

    public ClassOrInterfaceNode getSuperclass() {
        return this.superclass;
    }

    public void setSuperclass(ClassOrInterfaceNode superclass) {
        this.superclass = superclass;
        addChildren(superclass);
    }

    public List<DeclarationNode> getMembers(){
        return this.members;
    }

    public void addField(FieldNode field){
        this.fields.add(field);
        //this.members.add(field);
        addChildren(field);
    }

    public List<FieldNode> getFields(){
        return this.fields;
    }

    public void addMethod(MethodNode method){
        this.methods.add(method);
        this.members.add(method);
        addChildren(method);
    }

    public List<MethodNode> getMethods(){
        return this.methods;
    }
    
    /*
    @Override
    public String toString(){
        return "ClassOrInterfaceDeclaration: "+super.toString()+
                "\nModifiers: "+this.getModifiers()+
                ((!this.interfaces.isEmpty())?"\nInterfaces: "+interfaces:"")+
                ((this.superclass!=null)?"\nSuperclass: "+superclass:"")+
                "\nMembers: "+this.members+
                "\n}";
    }
    */
    
}
