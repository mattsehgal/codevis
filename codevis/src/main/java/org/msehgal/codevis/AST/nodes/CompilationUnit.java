package org.msehgal.codevis.AST.nodes;

import java.util.List;

public class CompilationUnit extends Node {
    
    private PackageNode packageDeclaration;
    private List<ImportNode> imports;
    private ClassOrInterfaceNode classOrInterfaceDeclaration;


    public CompilationUnit(String name) {
        super(null, name);
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
