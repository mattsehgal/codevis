package org.msehgal.codevis.util.TestSourceCode;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TestClass extends TestSuperClass implements TestInterface {
    private static int fieldA;
    private String fieldB;
    private List<String> fieldC = new ArrayList<>();

    public TestClass(){

    }

    public void methodA(List<Integer> paramA){

    }

    @SuppressWarnings("unused")
    public String methodB(String paramA, int paramB, boolean paramC){
        if(paramC) return paramA+paramB;
        else return "";
    }
}
