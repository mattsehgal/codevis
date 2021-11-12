package org.msehgal.codevis.AST;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.msehgal.codevis.AST.nodes.ClassOrInterfaceNode;
import org.msehgal.codevis.util.directories.DirTree;

public class ASTForest {
    private Map<DirTree, List<AST>> forest;
    private List<AST> trees;

    public ASTForest(){
        this.forest = new HashMap<>();
    }

    public ASTForest(DirTree dir){
        this();
        add(dir);
    }

    public ASTForest(Collection<DirTree> dirs){
        this();
        add(dirs);
    }

    public void add(DirTree dir){
        this.forest.put(dir, dir.getASTs());
    }

    public void add(Collection<DirTree> dirs){
        dirs.forEach(dir->add(dir));
    }

    public AST get(String name){
        for(DirTree dir : this.forest.keySet()){
            if(dir.get(name).getAST() != null){
                return dir.get(name).getAST();
            }
        }
        return null;
    }

    public void contextSupers(){
        extractTreesToList();
        for(AST tree : this.trees){
            ClassOrInterfaceNode coi = tree.getRoot().getClassOrInterfaceDeclaration();
            if(!tree.isContexted()){
                coi.setSuperclass(get(coi.getSuperclass().getName())
                                                        .getRoot()
                                                        .getClassOrInterfaceDeclaration());
                tree.setContexted(true);
            }
        }
    }

    private void extractTreesToList(){
        for(List<AST> asts : this.forest.values()){
            this.trees.addAll(asts);
        }
    }
}
