package org.msehgal.codevis.util.TestSourceCode;

import java.util.List;
import org.msehgal.codevis.AST.nodes.Node;

public class TestMain {
    
    // private int initField = 1;
    private static String testField = "testFieldString";
    // private char A;
    public static void main(String[] args){
        //TestClass test = new TestClass();
        //String testNodeName = "testNodeName";
        //Node testNode = new Node(new Node(null, "test1"), testField);
        Node testNode = new Node(null, testField);
        String testString = "A";
        testString = "B";
        //test.fieldA = 2;
        // test.methodA("paramA");
        
        // if(test.fieldA > 0){
        //     test.fieldB = "B";
        // }

        // int a = test.getFieldA();
    }

    // private String aMethod(TestClass test){
    //     test.fieldB = "C";
    //     return test.fieldB;
    // }
}
