package org.msehgal.codevis.visualization;

import org.msehgal.codevis.AST.nodes.*;
import org.msehgal.codevis.AST.*;

public class RunVisualizer {

    private BasicVisualizer vis;
    private String MAIN;

    public RunVisualizer(String PATH){
        this.vis = new BasicVisualizer(PATH);
        String[] files = PATH.split("\\");
        this.MAIN = files[files.length-1];
    }

    private void traceRun(){
        MethodNode mainMethod = (MethodNode) findMain(vis.getForest());
        //TODO implement expr and stmts 

    }

    private Node findMain(ASTForest forest){
        AST ast = forest.get(MAIN);
        return ast.get("main");
    }

    private RunTree getRunTree(BodyNode body){
        for(BlockNode block : body.getBlocks()){
            RunNode run = new RunNode(block.getText());
            
        }
        return null;
    }
    
}
