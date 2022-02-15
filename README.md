# codevis
Backend implemenation to process basic Java code into ASTs, return AST structured code or state updates as JSON (for frontend usage).  
  
The above is done via [BasicVisualizer](https://github.com/mattsehgal/codevis/tree/main/src/main/java/org/msehgal/codevis/visualization/BasicVisualizer.java).
BasicVisualizer returns a JSON AST representation of the code, for the front end to parse into display objects to visualize the code structure. 
  
Code execution visualization (implemenation in progess) via [RunVisualizer](https://github.com/mattsehgal/codevis/tree/main/src/main/java/org/msehgal/codevis/visualization/RunVisualizer.java). 
RunVisualizer returns a JSON represenation of each state change that takes place in the execution tied to the line number invoking the change.
Run visualization returns a sequence of state updates: creating and modifying objects and references. This return is for the frontend to display in animation of each line being ran showing the changes happening to the state (objects and references).   
  
CodeVis is built around ANTLR4 Java parsing, extracting data from the returned parse trees and organizing
it into a Java implementation of an Abstract Syntax Tree which has extra depth to hold all statements and expressions
attached to each Block within the Body of each method.
