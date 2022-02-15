package org.msehgal.codevis.visualization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.msehgal.codevis.AST.ASTForest;
import org.msehgal.codevis.util.directories.*;

public class BasicVisualizer {
    private ASTForest forest = new ASTForest();
    private List<DirTree> directories = new ArrayList<>();
    
    public BasicVisualizer(){}

    public BasicVisualizer(String PATH){
        addDirectory(PATH);
        //this.forest.contextSupers();
    }
    
    public void addDirectory(String PATH){
        //doesn't add anything to dirs field
        this.directories.add(new DirTree(new File(PATH), true));
        this.forest.add(new DirTree(new File(PATH), true));
    }

    public DirTreeNode getFile(String name){
        for(DirTree dir : this.directories){
            if(dir.get(name)!=null) return dir.get(name);
        }
        return null;
    }

    public ASTForest getForest(){
        return this.forest;
    }

    @SuppressWarnings("unchecked")
    public File getJSON(){
        JSONArray master = new JSONArray();
        for(DirTree dir : this.directories){
            master.add(dir.toJSON());
        }
        try {
            FileWriter file = new FileWriter("master.json");
            file.write(master.toJSONString());
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new File("master.json");
    }

    public String getJSONasString(){
        File f = this.getJSON();
        String res = "";
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while((line = br.readLine()) != null)
                res += (line+"\n");
            br.close();
        } catch(IOException e) {}
        return res;
    }

    //TYPE SAFETY
    @SuppressWarnings("unchecked")
    public void printDirectoriesJSON(){
        JSONArray json = new JSONArray();
        for(DirTree dir : this.directories)
            json.add(dir.toJSON());
            //System.out.println(dir.toJSON());

        System.out.println(json.toJSONString());
    }

}
