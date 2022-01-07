package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.List;

public class DeclarationNode extends Node {

    private List<AnnotationNode> annotations = new ArrayList<>();
    private List<ModifierNode> modifiers = new ArrayList<>();

    public DeclarationNode(Node parent){
        super(parent);
    }

    public DeclarationNode(Node parent, String name) {
        super(parent, name);
    }

    public void addAnnotation(String annotation){
        AnnotationNode ann = new AnnotationNode(this, annotation);
        this.annotations.add(ann);
        addChildren(ann);
    }

    public void addAnnotations(List<String> annotations){
        annotations.forEach(annotation->addAnnotation(annotation));
    }

    public void addModifier(String modifier){
        ModifierNode mod = new ModifierNode(this, modifier);
        this.modifiers.add(mod);
        addChildren(mod);
    }

    public void addModifiers(List<String> modifiers){
        modifiers.forEach(modifier->addModifier(modifier));
    }

    public List<AnnotationNode> getAnnotations(){
        return this.annotations;
    }

    public List<ModifierNode> getModifiers(){
        return this.modifiers;
    }
}
