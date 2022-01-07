package org.msehgal.codevis.AST;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.simple.JSONObject;
import org.msehgal.codevis.AST.nodes.BlockNode;
import org.msehgal.codevis.AST.nodes.ClassOrInterfaceNode;
import org.msehgal.codevis.AST.nodes.CompilationUnit;
import org.msehgal.codevis.AST.nodes.FieldNode;
import org.msehgal.codevis.AST.nodes.MethodNode;
import org.msehgal.codevis.AST.nodes.Node;
import org.msehgal.codevis.AST.nodes.ParameterNode;

public class AST {
    //TODO PRIO0 - add tree functionality
    private CompilationUnit root;
    private boolean contexted;

    public AST(CompilationUnit root){
        this.root = root;
        this.contexted = false;
    }

    public CompilationUnit getRoot(){
        return this.root;
    }
    
    //hash table/constant time lookup
    public Node get(String name) {
        Queue<Node> queue = new LinkedList<>();
        Node node = this.root;
        queue.add(node);
        
        while(!queue.isEmpty()){
            //TODO PRIO1 digusting, pls fix
            //caused by interfaces being instantiated as null in astlistener
            //should be fixed once they are implemented
            if(queue.peek() != null){
                node = queue.poll();
            } else {
                queue.poll();
                node = queue.poll();
            }

            //this is fine
            if(name.equals(node.getText()))
                return node;
            else if(!node.getChildren().isEmpty()){
                queue.addAll(node.getChildren());
            }
        }
        return null;
    }

    //TODO PRIO0 - use GSON or something, type safety warnings are dumb
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        CompilationUnit cu = this.root;
        json.put("compilation unit", cu.getText().toString());
        json.put("package", cu.getPackageDeclaration().toString());
        json.put("imports", listToJSON(cu.getImports()));

        ClassOrInterfaceNode coi = cu.getClassOrInterfaceDeclaration();
        JSONObject dec = new JSONObject();
        dec.put("name", coi.getText().toString());
        //TODO PRIO1 - annotation name not showing in json
        // List<ModifierNode> cAnns = separateAnnotations(coi.getModifiers());
        // if(!cAnns.isEmpty()) coi.getModifiers().removeAll(cAnns);
        dec.put("annotations", listToJSON(coi.getAnnotations()));
        dec.put("modifiers", listToJSON(coi.getModifiers()));
        dec.put("interfaces", listToJSON(coi.getInterfaces()));
        //TODO PRIO1 - this null check needs to be removed once superclass parsing is done
        if(coi.getSuperclass() != null)
            dec.put("superclass", coi.getSuperclass().getText().toString());
        
        JSONObject members = new JSONObject();
        JSONObject fields = new JSONObject();
        int fieldCount = 0;
        for(FieldNode field : coi.getFields()){
            JSONObject fieldJSON = new JSONObject();
            fieldJSON.put("name", field.getText().toString());
            //fieldJSON.put("modifiers", listToJSON(field.getModifiers()));
            fieldJSON.put("type", field.getType().toString());
            //fieldJSON.put("value", field.getValue());

            fields.put(fieldCount, fieldJSON);
            fieldCount++;
        }
        JSONObject methods = new JSONObject();
        int methodCount = 0;
        for(MethodNode method : coi.getMethods()){
            JSONObject methodJSON = new JSONObject();
            methodJSON.put("name", method.getText().toString());
            // List<ModifierNode> mAnns = separateAnnotations(method.getModifiers());
            // if(!mAnns.isEmpty()) method.getModifiers().removeAll(mAnns);
            methodJSON.put("annotations", listToJSON(method.getAnnotations()));
            methodJSON.put("modifiers", listToJSON(method.getModifiers()));
            methodJSON.put("body", listToJSON(method.getBlocks()));
            methodJSON.put("return type", method.getReturnType().toString());
            
            JSONObject params = new JSONObject();
            int paramCount = 0;
            for(ParameterNode param : method.getParameters()){
                JSONObject paramJSON = new JSONObject();
                paramJSON.put("name", param.getText().toString());
                paramJSON.put("type", param.getType().toString());
                paramJSON.put("value", param.getValue());
                params.put(paramCount, paramJSON);
                paramCount++;
            }
            methodJSON.put("parameters", params);

            methods.put(methodCount, methodJSON);
            methodCount++;
        }

        members.put("fields", fields);
        members.put("methods", methods);
        dec.put("members", members);
        json.put("class or interface", dec);

        return json;
    }

    //type safety warning is annoying
    @SuppressWarnings("unchecked")
    private JSONObject listToJSON(List<? extends Node> list){
        JSONObject json = new JSONObject();
        if(list!=null){
            int c = 0;
            for(Node node : list){
                //TODO PRIO2 - this null check needs to be removed, caused by creating superclass/interface nodes
                if(node != null){
                    json.put(c, node.toString());
                    c++;
                }
            }
        }
        return json;
    }

    public boolean isContexted() {
        return this.contexted;
    }

    public void setContexted(boolean contexted) {
        this.contexted = contexted;
    }

    @Override 
    public String toString(){
        return this.root.toString();
    }
}
