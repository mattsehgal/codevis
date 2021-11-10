package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    private String name;
    private Node parent;
    private ArrayList<Node> children = new ArrayList<>();

    public Node(Node parent){
        this.parent = parent;
    }

    public Node(Node parent, String name){
        this(parent);
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public Node getParent(){
        return this.parent;
    }

    protected boolean addChildren(Node... children){
        return this.children.addAll(Arrays.asList(children));
    }

    public ArrayList<Node> getChildren(){
        return this.children;
    }

    @Override
    public String toString(){
        return this.name != null ? 
            this.name : this.getClass().getName();
    }
}
