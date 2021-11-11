package org.msehgal.codevis;

import java.io.IOException;

import org.msehgal.codevis.antlr.*;
import org.msehgal.codevis.util.directories.DirTreeNode;
import org.msehgal.codevis.visualization.BasicVisualizer;

public class Test extends Java9BaseListener {
    static String PATH = "C:/Users/matt/codevis/codevis/src/main/java/org/msehgal/codevis/util/TestSourceCode";
    public static void main( String[] args ) throws IOException {
        BasicVisualizer vis = new BasicVisualizer();
        vis.addDirectory(PATH);
        //DirTreeNode node = vis.getFile("TestClass.java");
        //System.out.println(node);
        //System.out.println(node.getAST().get("methodA").toString());
        vis.printDirectoriesJSON();
        System.out.println(vis.getJSON().getAbsolutePath());
        //dirTree.walk();
    }

}
