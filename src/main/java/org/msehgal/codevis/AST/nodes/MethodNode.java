package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.List;

public class MethodNode extends DeclarationNode {

    private ModifierNode modifier;
    private String id;
    private BodyNode body = new BodyNode(this);
    private List<ParameterNode> parameters = new ArrayList<>();
    private TypeNode returnType = null;
    //new return type
    private String result;

    public MethodNode(Node parent){
        super(parent);
    }

    public MethodNode(Node parent, String name) {
        super(parent, name);
    }

    public void addBlock(BlockNode block){
        this.body.addBlock(block);
    }

    public List<BlockNode> getBlocks(){
        return this.body.blocks;
    }

    public BodyNode getBody(){
        return this.body;
    }

    public void setBody(BodyNode body){
        this.body = body;
    }

    public List<ParameterNode> getParameters(){
        return this.parameters;
    }

    public TypeNode getReturnType(){
        return this.returnType;
    }

    public String getResult(){
        return this.result;
    }

    public void setResult(String result){
        this.result = result;
    }

    public void addParameter(List<String> param){
        int mods = param.size()-2;
        for(int i=0; i<param.size()-1; i++){
            while(i<mods) this.addModifier(param.get(i));
            
            ParameterNode parameter = new ParameterNode(this, 
                                                param.get(i), 
                                                param.get(i+1));
            this.parameters.add(parameter);
            addChildren(parameter);
        }
    }
    
    public void addParameters(List<List<String>> params){
        params.forEach(param->addParameter(param));
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setReturnType(String returnType){
        if(this.returnType == null){
            TypeNode retType = new TypeNode(this, returnType);
            this.returnType = retType;
            addChildren(retType);
        }
    }

    /*
    @Override
    public String toString(){
        return "\nNODE{\nMethodDeclaration: "+super.toString()+
                "\nModifiers: "+this.getModifiers()+
                "\nParameters: "+this.getParameters()+
                "\nReturnType: "+this.getReturnType()+
                "\n}";       
    }
    */

}
