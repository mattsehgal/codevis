package org.msehgal.codevis.AST.nodes.statements.expressions;

import org.msehgal.codevis.AST.nodes.Node;

public class AtomNode extends ExpressionNode{
    
    String atom;

    Class clazz;

    Object value;

    public AtomNode(Node parent){
        super(parent);
    }

    public AtomNode(Node parent, String atom){
        super(parent);
        this.atom = atom;
    }

    public AtomNode(Node parent, String atom, Class clazz){
        super(parent);
        this.atom = atom;
        this.clazz = clazz;
    }

    //return order: name, className
    @Override
    public String[][] evaluate(){
        return new String[][]{{this.atom}};
        //return new String[][]{{this.atom, this.clazz.getName()}};
    }

    public String[] getContent(){
        return evaluate()[0];
    }

    public String getAtom() {
        return atom;
    }

    public void setAtom(String atom) {
        this.atom = atom;
    }

    public String getClassName() {
        return clazz.getName();
    }

    public void setClass(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString(){
        return getAtom();
    }
}
