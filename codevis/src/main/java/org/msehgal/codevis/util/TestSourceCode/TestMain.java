package org.msehgal.codevis.util.TestSourceCode;

public class TestMain {
    
    public static void main(String[] args){
        TestClass test = new TestClass();
        
        test.methodA("paramA");
        
        if(test.fieldA > 0){
            test.fieldB = "B";
        }
    }
}
