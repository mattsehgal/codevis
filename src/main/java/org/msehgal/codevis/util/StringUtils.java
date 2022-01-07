package org.msehgal.codevis.util;

import java.util.Iterator;

import org.msehgal.codevis.util.directories.DirTreeNode;

public class StringUtils {
	private StringUtils() {
		
	}
	
	public static String printDirTree(DirTreeNode root) {
		StringBuilder sb = new StringBuilder();
		genSubTree(root, sb, 0);
		return sb.toString();
	}
	
	private static void genSubTree(DirTreeNode node, StringBuilder sb, int n) {
		if(node.isDir) genDirLine(node, sb, n);
		else genFileLine(node, sb, n);
	}
	
	private static void genDirLine(DirTreeNode node, StringBuilder sb, int n) {
		if(node.getParent() != null) {
			sb.append("\n");
			sb.append(genIndents(n)+"|__["+node.getName()+"]");
		} else {
			sb.append(genIndents(n)+"["+node.getName()+"]");
		}
		n++;
		parseChildren(node, sb, n);
	}
	
	private static void parseChildren(DirTreeNode node, StringBuilder sb, int n) {
		if(node.getChildren() != null) {
			Iterator<DirTreeNode> iterator = node.getChildren().iterator();
			while(iterator.hasNext()) genSubTree(iterator.next(), sb, n);
		}
	}
	
	private static void genFileLine(DirTreeNode node, StringBuilder sb, int n) {
		sb.append("\n"+genIndents(n)+"|__"+node.getName());		
	}
	
	private static String genIndents(int n) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<n; i++) sb.append("   ");
		return sb.toString();
	}
}
