package org.msehgal.codevis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.msehgal.codevis.antlr.*;
import org.msehgal.codevis.visualization.BasicVisualizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.msehgal.codevis.util.TestSourceCode.TestClass;

// @SpringBootApplication
// @Controller
public class App {

	final static String PATH = "C:/Users/matt/codevis/codevis/src/main/java/org/msehgal/codevis/util/TestSourceCode";
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
		BasicVisualizer vis = new BasicVisualizer(PATH);
		vis.getJSON();
		vis.printDirectoriesJSON();
	}

}
