package org.msehgal.codevis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.msehgal.codevis.antlr.*;
import org.msehgal.codevis.visualization.BasicVisualizer;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	private void testVis(){
		String PATH = "C:/Users/matt/codevis/codevis/src/main/java/org/msehgal/codevis/util/TestSourceCode";
		BasicVisualizer vis = new BasicVisualizer();
        //DirTreeNode node = vis.getFile("TestClass.java");
        //System.out.println(node);
        //System.out.println(node.getAST().get("methodA").toString());
        vis.printDirectoriesJSON();
        System.out.println(vis.getJSON().getAbsolutePath());
        //dirTree.walk();
	}

}
