package org.msehgal.codevis.AST.nodes.statements.expressions;

import org.msehgal.codevis.AST.nodes.Node;

public class AtomNode extends ExpressionNode{
    
    String atom;

    public AtomNode(Node parent){
        super(parent);
    }

    public AtomNode(Node parent, String atom){
        super(parent);
        this.atom = atom;
    }

    public String getAtom() {
        return atom;
    }

    public void setAtom(String atom) {
        this.atom = atom;
    }

    @Override
    public String toString(){
        return getAtom();
    }
}
