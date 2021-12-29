package org.msehgal.codevis.util.TestSourceCode;

public class TestMain {
    
    private int initField = 1;

    private char A;
    public static void main(String[] args){
        TestClass test = new TestClass();
        test.fieldA = 2;
        // test.methodA("paramA");
        
        // if(test.fieldA > 0){
        //     test.fieldB = "B";
        // }

        // int a = test.getFieldA();
    }

    private String aMethod(TestClass test){
        test.fieldB = "C";
        return test.fieldB;
    }
}
