package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    
    private String text;
    
    private Node parent;
    
    private ArrayList<Node> children = new ArrayList<>();

    //empty constructor to help class.forname.contstructor problem
    public Node(){
        this.parent = null;
    }

    public Node(Node parent){
        this.parent = parent;
    }

    public Node(Node parent, String text){
        this(parent);
        this.text = text;
    }

    public boolean addChild(Node child){
        return this.children.add(child);
    }

    public boolean addChildren(Node... children){
        return this.children.addAll(Arrays.asList(children));
    }

    public Node getChild(int i){
        return this.children.get(i);
    }

    public ArrayList<Node> getChildren(){
        return this.children;
    }

    public String getText() {
        return this.text;
    }

    public Node getParent(){
        return this.parent;
    }

    @Override
    public String toString(){
        return this.text != null ? 
            this.text : this.getClass().getSimpleName();
    }
}
