package org.msehgal.codevis.AST.nodes;

import java.util.ArrayList;
import java.util.List;

public class MethodNode extends DeclarationNode {

    private List<ParameterNode> parameters = new ArrayList<>();
    private TypeNode returnType = null;

    public MethodNode(Node parent, String name) {
        super(parent, name);
    }

    public List<ParameterNode> getParameters(){
        return this.parameters;
    }

    public TypeNode getReturnType(){
        return this.returnType;
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
