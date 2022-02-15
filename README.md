# codevis
Backend implemenation to process basic Java code into ASTs, return AST structured code as JSON (for frontend usage).  
  
The above is done via [BasicVisualizer](https://github.com/mattsehgal/codevis/tree/main/src/main/java/org/msehgal/codevis/visualization/BasicVisualizer.java).  
Code execution visualization in progess, via [RunVisualizer](https://github.com/mattsehgal/codevis/tree/main/src/main/java/org/msehgal/codevis/visualization/RunVisualizer.java).  
Run visualization returns a sequence of state updates, creating and modifying objects and references. 
  
CodeVis is built around ANTLR4 Java parsing, extracting data from the returned parse trees and organizing
it into a Java implementation of an Abstract Syntax Tree which has extra depth to hold all statements and expressions
attached to each Block within the Body of each method.
