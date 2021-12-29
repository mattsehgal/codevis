package org.msehgal.codevis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.msehgal.codevis.AST.ASTBuilder;
import org.msehgal.codevis.AST.XPaths;
import org.msehgal.codevis.visualization.BasicVisualizer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// @SpringBootApplication
// @Controller
public class App {

	final static String PATH = "C:/Users/matt/codevis/codevis/src/main/java/org/msehgal/codevis/util/TestSourceCode/TestMain.java";
	public static void main(String[] args) throws IOException {
		//SpringApplication.run(App.class, args);
		printTest();
	}

	@RequestMapping("/")
	@ResponseBody
	private static String basicTest() {
		BasicVisualizer vis = new BasicVisualizer(PATH);
		return vis.getJSONasString();
	}

	private static void printTest() {
		//BasicVisualizer vis = new BasicVisualizer(PATH);
		//vis.getJSON();
		//vis.printDirectoriesJSON();
		Path p = Paths.get(PATH);
		ASTBuilder builder = new ASTBuilder(p);
		String xpath = XPaths.CU.x;
		builder.buildAST();
	}

}
