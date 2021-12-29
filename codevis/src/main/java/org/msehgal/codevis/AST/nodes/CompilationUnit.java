package org.msehgal.codevis.AST.nodes;

import java.util.List;

public class CompilationUnit extends Node {
    public String XPATH = "//compilationUnit/ordinaryCompilation";
    
    private String id;
    // //compilationUnit/ordinaryCompilation/packageDeclaration
    private PackageNode packageDeclaration;
    // //compilationUnit/ordinaryCompilation/importDeclaration
    private List<ImportNode> imports;
    // //compilationUnit/ordinaryCompilation/typeDeclaration/classDeclaration||interfaceDeclaration
    private ClassOrInterfaceNode classOrInterfaceDeclaration;
    // //compilationUnit/ordinaryCompilation/typeDeclaration/classDeclaration
    private ClassNode classDeclaration;

    public CompilationUnit() {
        super(null);
    }

    public CompilationUnit(String name) {
        super(null, name);
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public ClassNode getClassDeclaration(){
        return this.classDeclaration;
    }

    public void setClassDeclaration(ClassNode classDeclaration){
        this.classDeclaration = classDeclaration;
        addChild(classDeclaration);
    }

    public ClassOrInterfaceNode getClassOrInterfaceDeclaration() {
        return this.classOrInterfaceDeclaration;
    }

    public List<ImportNode> getImports() {
        return this.imports;
    }

    public PackageNode getPackageDeclaration() {
        return this.packageDeclaration;
    }

    public void setPackageDeclaration(PackageNode pack) {
        this.packageDeclaration = pack;
    }

    public void setClassOrInterfaceDeclaration(ClassOrInterfaceNode node){
        this.classOrInterfaceDeclaration = node;
        addChildren(node);
    }

    @Override
    public String toString(){
        return super.toString();
        /*+
                "\nPackage: "+this.packageDeclaration+
                "\nImports: "+this.imports+
                "\nClassOrInterface: "+this.classOrInterfaceDeclaration;
        */
    }
    
}
