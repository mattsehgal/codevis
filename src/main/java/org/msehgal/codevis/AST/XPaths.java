package org.msehgal.codevis.AST;

public enum XPaths {
    CU("//compilationUnit/ordinaryCompilation"),
    __CU("/ordinaryCompilation"),
    CU_PACKAGE(__CU.x+"/packageDeclaration"),
    CU_IMPORTS(__CU.x+"//importDeclaration"),
    CU_CLASS(__CU.x+"//typeDeclaration/classDeclaration"),
    CU_INTERF(__CU.x+"/typeDeclaration/interfaceDeclaration"),

    CLASS(CU_CLASS.x+"/normalClassDeclaration"),
    __CLASS("/normalClassDeclaration"),
    CLASS_MOD(__CLASS.x+"/classModifier"),
    CLASS_ID(__CLASS.x+"/identifier"),
    CLASS_SUPERCLASS(__CLASS.x+"/superclass/classType/identifier"),
    CLASS_SUPERINTERFS(__CLASS.x+"/superinterfaces/interfaceTypeList"),
    CLASS_MEMBERS(__CLASS.x+"/classBody/classBodyDeclaration//classMemberDeclaration"),
    CLASS_FIELDS(CLASS_MEMBERS.x+"//fieldDeclaration"),
    CLASS_METHODS(CLASS_MEMBERS.x+"//methodDeclaration"),

    FIELD(CLASS_FIELDS.x),
    __FIELD("/fieldDeclaration"),
    FIELD_MOD(__FIELD.x+"/fieldModifier"),
    FIELD_TYPE(__FIELD.x+"/unannType"), //?
    FIELD_VARS(__FIELD.x+"/variableDeclaratorList"),
    FIELD_VAR(FIELD_VARS.x+"/variableDeclarator"),
    FIELD_ID(FIELD_VAR.x+"/variableDeclaratorId/identifier"),

    METHOD(CLASS_METHODS.x),
    __METHOD("//methodDeclaration"),
    METHOD_MOD(__METHOD.x+"/methodModifier"),
    METHOD_HEAD(__METHOD.x+"/methodHeader"),
    METHOD_BODY(__METHOD.x+"/methodBody"),
    METHOD_RETURN(METHOD_HEAD.x+"/result"),
    METHOD_ID(METHOD_HEAD.x+"/methodDeclarator/identifier"),
    METHOD_PARAMS(METHOD_HEAD.x+"/methodDeclarator/formalParameterList"),
    METHOD_BLOCK(METHOD_BODY.x+"/block"),
    METHOD_BLOCKS(METHOD_BLOCK.x+"/blockStatements"),

    //NO IDEA HOW WE GOT HERE, FIGURE OUT LATER
    BLOCK("/methodBody/block"),
    BLOCKS(BLOCK.x+"/blockStatements//blockStatement"),
    __BLOCK("/methodBody/block/blockStatements/blockStatement"),
    BLOCK_TYPE("/blockStatement/*"),

    ASSN_EXPR(BLOCK_TYPE.x+"/statementWithoutTrailingSubstatement/expressionStatement/statementExpression/assignment"),
    CLASSINST_EXPR(BLOCK_TYPE.x+"/statementWithoutTrailingSubstatement/expressionStatement/statementExpression/classInstanceCreationExpression"),
    METHINVOC_EXPR(BLOCK_TYPE.x+"/statementWithoutTrailingSubstatement/expressionStatement/statementExpression/methodInvocation"),

    ;

    public final String x;

    private XPaths(String xpath){
        this.x = xpath;
    }
}