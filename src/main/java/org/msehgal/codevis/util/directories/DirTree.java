package org.msehgal.codevis.util.directories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.json.simple.JSONArray;
import org.msehgal.codevis.AST.AST;
import org.msehgal.codevis.util.StringUtils;

public class DirTree {

	private DirTreeNode root;
	private File rootFile;
	private boolean ast = false;

	/**
	 * Creates a DirTree. Attempts to initialize the tree by generating the root
	 * {@link DirTreeNode} from the passed in {@link File}. (The node is
	 * auto-building and generates the entire subtree of the root.)
	 * 
	 * @param root the root file reference to initialize the tree with
	 * @param ast whether or not to generate ASTs for each node
	 */
	public DirTree(File root, boolean ast) {
		this.rootFile = root;
		this.setAst(ast);
		try {
			initTree(ast);
		} catch (IOException e) {
			System.out.println("IO Error: DirTree_init");
		}
	}

	public DirTree(File root) {
		this(root, false);
	}
	
	/**
	 * Initializes the root node.
	 * @param ast boolean whether to build ASTs for .java files
	 * @throws IOException
	 */
	private void initTree(boolean ast) throws IOException{
		this.root = new DirTreeNode(this.rootFile, ast);
	}

	//TODO PRIO1 - this needs to actually do something lol
	public void walk(DirTreeNode node) {
		System.out.println(node.getName());
		if(node.getAST() != null) 
			System.out.println("\tAST: "+node.getAST().toString());
		else if (!isLeaf(node)) {
			node.getChildren().forEach(child->walk(child));
		}
	}

	public void walk(){
		this.walk(this.root);
	}

	public DirTreeNode get(String name){
		Queue<DirTreeNode> queue = new LinkedList<>();
		DirTreeNode node = this.root;
		queue.add(node);

		while(!queue.isEmpty()){
			node = queue.poll();
			if(name.equals(node.getName()))
				return node;
			else if(!isLeaf(node))
				queue.addAll(node.getChildren());
		}
		return null;
	}

	public DirTreeNode get(Path path){return get(path.toFile().getName());}
	
	public DirTreeNode get(File file){return get(file.getName());}

	/**
	 * Checks if {@link DirTreeNode} is a leaf by checking its children list
	 * @param node the node to check
	 * @return true if the node is a leaf; childNodes is empty or null
	 */
	public boolean isLeaf(DirTreeNode node) {
		if(node.getChildren() == null) return true;
		if(node.getChildren().isEmpty()) return true;
		return false;
	}

	public Set<DirTreeNode> toSet(DirTree tree){
		Set<DirTreeNode> set = new LinkedHashSet<>();
		set.add(this.root);
		for(DirTreeNode child : this.root.getChildren()){
			set.addAll(traverse(child, set));
		}

		return set;
	}

	public Set<DirTreeNode> toSet(){
		return toSet(this);
	}

	private Set<DirTreeNode> traverse(DirTreeNode node, Set<DirTreeNode> set){
		set.add(node);
		for(DirTreeNode child : node.getChildren()){
			traverse(child, set);
		}
		return set;
	}

	public List<AST> getASTs(){
		List<AST> list = new ArrayList<>();
		for(DirTreeNode node : toSet()){
			if(node.getAST() != null){
				list.add(node.getAST());
			}
		}
		return list;
	}

	public boolean isAst() {
		return this.ast;
	}

	public void setAst(boolean ast) {
		this.ast = ast;
	}

	public DirTreeNode getRoot() {
		return this.root;
	}

	public File getRootFile() {
		return this.rootFile;
	}
    //TYPE SAFETY
    @SuppressWarnings("unchecked")
	public JSONArray toJSON(){
		JSONArray json = new JSONArray();
		for(DirTreeNode node : toSet())
			json.add(node.toJSON());

		return json;
	}
	
	@Override
	public String toString() {
		return StringUtils.printDirTree(this.root);
	}

}
