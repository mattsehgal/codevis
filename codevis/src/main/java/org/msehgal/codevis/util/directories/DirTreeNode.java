package org.msehgal.codevis.util.directories;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.json.simple.JSONObject;

import org.msehgal.codevis.AST.AST;
import org.msehgal.codevis.AST.ASTListener;

public class DirTreeNode {
	private String name;
	private File file;
	private Path path;
	public boolean isDir;
	
	private DirTreeNode parentNode;
	private Set<DirTreeNode> childNodes = new LinkedHashSet<>();
	
	private boolean hasAST;
	private AST AST;
	
	/**
	 * Constructs a DirTreeNode from a {@link File} and links it to its parent.
	 * The node automatically builds and stores its subtree.
	 * @param file file to build the node for
	 * @param parent parent node of this node
	 * @param hasAST boolean whether to construct ASTs for source code files
	 */
	public DirTreeNode(File file, DirTreeNode parent, boolean hasAST) {
		this.name = file.getName();
		this.file = file;
		this.path = file.toPath();
		this.isDir = file.isDirectory();
		this.parentNode = parent;
		this.hasAST = hasAST;
		try {
			if(this.hasAST) setAST();
			buildSubtree();
		} catch (IOException e) {
			System.out.println("IO Error: buildSubtree");
		}
	}

	public DirTreeNode(File file){this(file, null, false);}

	public DirTreeNode(File file, DirTreeNode parent){this(file, parent, false);}

	public DirTreeNode(File file, boolean hasAST){this(file, null, hasAST);}

	/**
	 * Generates and stores an AST for this node if it is a source code file.
	 * Uses {@link ASTListener to walk the parse tree and return an {@link AST}.
	 * @throws IOException
	 */
	public void setAST() throws IOException {
		//TODO PRIO0 - contains .java eventually should be removed/editted to handle each language
		//trying to move this logic to astbuilder, just need to take in
		//the created object
		if(!isDir && name.contains(".java"))
			this.AST = new ASTListener(this.path).buildAST();
	}

	/**
	 * Builds and stores this node's children.
	 * @return true if the subtree was built successfully, false if this node is a
	 *         leaf
	 * @throws IOException
	 */
	private boolean buildSubtree() throws IOException {
		if (!isLeaf()) {
			try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(this.path)) {
				dirStream.forEach(p->this.childNodes.add(
					new DirTreeNode(p.toFile(), this, this.hasAST)));
				return true;
			}
		} else return false;
	}

	/**
	 * Checks if {@link DirTreeNode} is a leaf by checking the root file's data.
	 * @param node the node to be checked
	 * @return true if the node is a leaf; file or empty directory
	 * @throws IOException
	 */
	private boolean isLeaf() throws IOException {
		if (this.isDir) {
			try (Stream<Path> files = Files.list(this.path)){
				return !files.findFirst().isPresent();
			}
		} else return true;
	}

	public String getName(){return this.name;}

	public File getFile(){return this.file;}

	public Path getPath(){return this.path;}

	public DirTreeNode getParent(){return this.parentNode;}

	public Set<DirTreeNode> getChildren(){return this.childNodes;}

	public boolean hasAST(){return this.hasAST;}

	public AST getAST(){return this.hasAST ? this.AST:null;}

	public boolean addChildren(DirTreeNode node) {
		return this.childNodes.add(node);
	}

	public boolean addChildren(Collection<DirTreeNode> nodes) {
		return this.childNodes.addAll(nodes);
	}

	//TODO PRIO1 consider moving the json creation logic all to the same place, currently seems to be spread out becoming difficult to locate
	//fuckin type safety
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		if(this.hasAST && this.AST != null){
			json.put("java file", this.AST.toJSON());
		} else {
			json.put("directory", this.name);
		}
		return json;
	}

	@Override
	public String toString() {
		return "{Name : " + this.name
				+
				"\nIs directory? : "+this.isDir+
				(this.isDir ? 
						"\nSubdirectories : "+(!this.childNodes.isEmpty() ? 
								this.childNodes.toString():"None")
						: "")
				+"}";
	}
}
