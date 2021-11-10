package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.List;

public class DeclarationNode extends Node {

    private List<ModifierNode> modifiers = new ArrayList<>();

    public DeclarationNode(Node parent, String name) {
        super(parent, name);
    }

    public void addModifier(String modifier){
        ModifierNode mod = new ModifierNode(this, modifier);
        this.modifiers.add(mod);
        addChildren(mod);
    }

    public void addModifiers(List<String> modifiers){
        modifiers.forEach(modifier->addModifier(modifier));
    }

    public List<ModifierNode> getModifiers(){
        return this.modifiers;
    }
}
