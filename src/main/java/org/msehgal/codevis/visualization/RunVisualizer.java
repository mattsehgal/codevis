package org.msehgal.codevis.visualization;

import org.msehgal.codevis.AST.nodes.*;
import org.msehgal.codevis.AST.nodes.statements.StatementNode;
import org.msehgal.codevis.AST.nodes.statements.VariableDeclarationNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.AssignmentNode;
import org.msehgal.codevis.AST.nodes.statements.expressions.AtomNode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.msehgal.codevis.AST.*;

public class RunVisualizer {

    private BasicVisualizer vis;
    private String MAIN;

    public RunVisualizer(String PATH){
        this.vis = new BasicVisualizer(PATH);
        // String[] files = PATH.split("\\");
        // this.MAIN = files[files.length-1];
    }

    private void traceRun(){
        MethodNode mainMethod = (MethodNode) findMain(vis.getForest());
        //TODO implement expr and stmts 

    }

    private Node findMain(ASTForest forest){
        AST ast = forest.get(MAIN);
        return ast.get("main");
    }

    //RENAME, same as tree method name
    public void processTree() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
        List<RunTree> runs = new ArrayList<>();
        ASTForest forest = vis.getForest();
        List<BodyNode> bodies = new ArrayList<>();
        for(AST ast : forest.getTrees()){
            if(ast.getRoot()!=null)
                ast.getRoot().getClassDeclaration().getMethods().forEach(m->bodies.add(m.getBody()));
        }
        //TODO change, doesnt need to process every body tree, just as called
        for(BodyNode body : bodies){
            RunTree run = getRunTree(body);
            run.processTree();
        }
        
    }

    private RunTree getRunTree(BodyNode body){
        RunNode prev = null;
        RunNode root = null;
        for(BlockNode block : body.getBlocks()){
            RunNode run = new RunNode(block.getText());
            if(root==null) root = run;
            parseBlock(block, run);
            if(prev!=null) prev.setNext(run);
            run.setPrevious(prev);
            prev = run;
        }
        return new RunTree(root, vis.getForest());
    }

    private void parseBlock(BlockNode block, RunNode run){
        StatementNode statement = block.getStatement();
        if(block.getType() == BlockType.EXPR_ASSN){
            AssignmentNode assn = (AssignmentNode) statement;
            String[][] res = assn.evaluate();
            run.setStep(getStep(res, 1));
        } else if(block.getType() == BlockType.VAR_DEC){
            VariableDeclarationNode varDec = (VariableDeclarationNode) statement;
            if(!varDec.getFoundQualifiedName()){
                getQualifiedNameFromForest(varDec.getClassName());
            }
            String[][] res = varDec.evaluate();
            run.setStep(getStep(res, 0));
        }
    }

    //0:createobj, 1:modifyref
    private StepNode getStep(String[][] res, int type){
        StepType sType = (type==0) ? StepType.CREATE_OBJ : StepType.UPDATE_VAL;
        StepNode step = new StepNode(sType);
        step.setResult(res);
        return step;
    }

    private String getQualifiedNameFromForest(AtomNode className){
        String name = className.getAtom();
        //only has cu for testmain, not rest of testsourcecode
        for(AST ast : vis.getForest().getTrees()){
            CompilationUnit cu = ast.getRoot();
            if(name.equals(cu.getClassDeclaration().getId())){
                return cu.getPackageDeclaration().getName();
            }
        }
        return name;
    }
}
