/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.isptec.sintatico;
import compilador.isptec.lexico.*;
import java.io.*;


/**
 *
 * @author stacks
 */

/*Classe que contém todas as producoes da nossa gramática*/

public class Grammar {

    public static void program(){
        programHeading();
        block();
        Parser.consume(Tokens.PONTO);
    }

    public static void programHeading(){
        Parser.consume(Tokens.PROGRAM);
        Parser.consume(Tokens.ID);
        Parser.consume(Tokens.ABREPAR);
        Parser.consume(Tokens.ID);
        while(Parser.lookahead.getToken()==Tokens.PONTOVIRGULA){
            Parser.consume(Tokens.PONTOVIRGULA);
            Parser.consume(Tokens.ID);
        }
        Parser.consume(Tokens.FECHAPAR);
        Parser.consume(Tokens.PONTOVIRGULA);
    }

    public static void block(){
        labelDeclPart();
        constantDefPart();
        typeDefPart();
        varDeclPart();
        procAndFuncDeclPart();
        compoundStatement();
    }

    public static void labelDeclPart(){
        if(Parser.lookahead.getToken()==Tokens.LABEL){
            Parser.consume(Tokens.LABEL);
            Parser.consume(Tokens.NUMINT);
            while(Parser.lookahead.getToken()==Tokens.VIRGULA){
                Parser.consume(Tokens.VIRGULA);
                Parser.consume(Tokens.NUMINT);
            }
            Parser.consume(Tokens.PONTOVIRGULA);
        }
    }

    public static void constantDefPart(){
        if(Parser.lookahead.getToken()==Tokens.CONST){
            Parser.consume(Tokens.CONST);
            constantDef();
            Parser.consume(Tokens.PONTOVIRGULA);
            constantDefPart1();
        }
    }

    public static void constantDefPart1(){
        if(Parser.lookahead.getToken()==Tokens.ID){
            constantDef();
            Parser.consume(Tokens.PONTOVIRGULA);
            constantDefPart1();
        }
    }

    public static void constantDef(){
        Parser.consume(Tokens.ID);
        Parser.consume(Tokens.IGUAL);
        constant();
    }

    public static void constant(){
        if (Parser.lookahead.getToken()==Tokens.NUMINT || Parser.lookahead.getToken()==Tokens.NUMREAL){
            unsignedNumber();
        }
        else if(Parser.lookahead.getToken()==Tokens.MAIS || Parser.lookahead.getToken()==Tokens.MENOS){
            sign();
            sign1();
        }
        else if(Parser.lookahead.getToken()==Tokens.ID){
            Parser.consume(Tokens.ID);
        }
        else if(Parser.lookahead.getToken()==Tokens.STRING){
            Parser.consume(Tokens.STRING);
        }
        else{
            Parser.error(Tokens.ID,Parser.UNKNOWN_ERROR);
        }
    }


    public static void sign1(){
        if (Parser.lookahead.getToken()==Tokens.NUMINT || Parser.lookahead.getToken()==Tokens.NUMREAL){
            unsignedNumber();
        }
        else if(Parser.lookahead.getToken()==Tokens.ID){
            Parser.consume(Tokens.ID);
        }
        else{
            Parser.error(Tokens.ID,Parser.UNKNOWN_ERROR);
        }
    }

    public static void unsignedNumber(){
        if (Parser.lookahead.getToken()==Tokens.NUMINT){
            Parser.consume(Tokens.NUMINT);
        }
        else if( Parser.lookahead.getToken()==Tokens.NUMREAL){
            Parser.consume(Tokens.REAL);
        }
        else{
            Parser.error(Tokens.ID,Parser.UNKNOWN_ERROR);
        }
    }

    public static void sign(){
        if(Parser.lookahead.getToken()==Tokens.MAIS)){
            Parser.consume(Tokens.MAIS);
        }
        else if(Parser.lookahead.getToken()==Tokens.MENOS){
            Parser.consume(Tokens.MENOS);
        }
        else{
            Parser.error(Tokens.MAIS,Parser.UNKNOWN_ERROR);
        }
    }

    public static void typeDefPart(){
        if(Parser.lookahead.getToken()==Tokens.TYPE){
            Parser.consume(Tokens.TYPE);
            typeDef();
            Parser.consume(Tokens.PONTOVIRGULA);
            typeDefPart1();
        }
    }

    public static void typeDefPart1(){
        if(Parser.lookahead.getToken()==Tokens.ID){
            typeDef();
            Parser.consume(Tokens.PONTOVIRGULA);
            typeDefPart1();
        }
    }

    public static void typeDef(){
        Parser.consume(Tokens.ID);
        Parser.consume(Tokens.IGUAL);
        type();
    }

    public static void type(){

        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead==Tokens.ABREPAR){
            scalarType();
        }
        else if(lookahead==Tokens.NUMINT || lookahead==Tokens.NUMREAL){
            unsignedNumber();
            Parser.consume(Tokens.PONTOPONTO);
            constant();
        }
        else if(lookahead==Tokens.MAIS || lookahead==Tokens.MENOS){
            sign();
            sign1();
            Parser.consume(Tokens.PONTOPONTO);
            constant();
        }
        else if(lookahead==Tokens.STRING){
            Parser.consume(Tokens.STRING);
            Parser.consume(Tokens.PONTOPONTO);
            constant();
        }
        else if(lookahead==Tokens.ID){
            Parser.consume(Tokens.ID);
            type1();
        }
        else if(lookahead==Tokens.ARRAY || lookahead==Tokens.RECORD
                || lookahead ==Tokens.SET || lookahead == Tokens.FILE){
            structuredType();
        }
        else{
            Parser.error(Tokens.ID,Parser.UNKNOWN_ERROR);
        }
    }

    public static void type1(){
        if(Parser.lookahead.getToken()==Tokens.PONTOPONTO){
            Parser.consume(Tokens.PONTOPONTO);
            constant();
        }
    }

    public static void scalarType(){
        Parser.consume(Tokens.ABREPAR);
        Parser.consume(Tokens.ID);
        while(Parser.lookahead.getToken()==Tokens.VIRGULA){
            Parser.consume(Tokens.VIRGULA);
            Parser.consume(Tokens.ID);
        }
        Parser.consume(Tokens.FECHAPAR);
    }


    public static void structuredType(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ARRAY){
            arrayType();
        }
        else if(lookahead == Tokens.RECORD){
            recordType();
        }
        else if (lookahead == Tokens.SET){
            setType();
        }
        else if(lookahead == Tokens.FILE) {
            fileType();
        }
        else{
            Parser.error(Tokens.MAIS,Parser.UNKNOWN_ERROR);
        }
    }

    public static void arrayType(){
        Parser.consume(Tokens.ARRAY);
        Parser.consume(Tokens.ABRERET);
        indexType();
        while(Parser.lookahead.getToken()==Tokens.VIRGULA){
            Parser.consume(Tokens.VIRGULA);
            indexType();
        }
        Parser.consume(Tokens.FECHARET);
        Parser.consume(Tokens.OF);
        type();
    }

    public static void indexType(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ABREPAR){
            scalarType();
        }
        else if(lookahead == Tokens.NUMINT || lookahead == Tokens.NUMREAL){
            unsignedNumber();
            Parser.consume(Tokens.PONTOPONTO);
            constant();
        }
        else if(lookahead==Tokens.MAIS || lookahead==Tokens.MENOS){
            sign();
            sign1();
            Parser.consume(Tokens.PONTOPONTO);
            constant();
        }
        else if(lookahead==Tokens.STRING){
            Parser.consume(Tokens.STRING);
            Parser.consume(Tokens.PONTOPONTO);
            constant();
        }
        else if(lookahead==Tokens.ID){
            Parser.consume(Tokens.ID);
            type1();
        }
        else{
            Parser.error(Tokens.ABREPAR,Parser.UNKNOWN_ERROR);
        }
    }

    public static void recordType(){
        Parser.consume(Tokens.RECORD);
        fieldList();
        Parser.consume(Tokens.END);
    }

    public static void fieldList(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.CASE){
            variantType();
        }
        else{
            fixedPart();
            fixedPart1();
        }
    }

    public static void fixedPart(){
        Tokens lookahead = Parser.lookahead.getToken();
        recordSection();
        while(Parser.lookahead.getToken() == Tokens.PONTOVIRGULA){
            Parser.consume(Tokens.PONTOVIRGULA);
            recordSection();
        }
    }


    public static void fixedPart1(){
        Tokens lookahead = Parser.lookahead.getToken();
        if (lookahead == Tokens.PONTOVIRGULA){
            Parser.consume(Tokens.PONTOVIRGULA);
            variantType();
        }
    }

    public static void recordSection(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ID){
            Parser.consume(Tokens.ID);
            while(Parser.lookahead.getToken() == Tokens.VIRGULA){
                Parser.consume(Tokens.VIRGULA);
                Parser.consume(Tokens.ID);
            }
            Parser.consume(Tokens.PONTOVIRGULA);
            type();
        }
    }

    public static void variantType(){
        Tokens lookahead = Parser.lookahead.getToken();
        Parser.consume(Tokens.CASE);
        tagField();
        Parser.consume(Tokens.ID);
        Parser.consume(Tokens.OF);
        variant();
        while(Parser.lookahead.getToken() == Tokens.PONTOVIRGULA){
            Parser.consume(Tokens.PONTOVIRGULA);
            variant();
        }
    }

    public static void tagField(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ID) {
            Parser.consume(Tokens.ID);
            Parser.consume(Tokens.DOISPONTOS);
        }
    }

    public static void variant(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.NUMINT || lookahead == Tokens.NUMREAL
        || lookahead == Tokens.MAIS || lookahead == Tokens.MENOS ||
          lookahead == Tokens.ID || lookahead == Tokens.STRING){
            caseLabelList();
            Parser.consume(Tokens.DOISPONTOS);
            Parser.consume(Tokens.ABREPAR);
            fieldList();
            Parser.consume(Tokens.FECHAPAR);
        }
    }

    public static void caseLabelList(){
        Tokens lookahead = Parser.lookahead.getToken();
        constant();
        while (lookahead == Tokens.VIRGULA){
            Parser.consume(Tokens.VIRGULA);
            constant();
        }
    }

    public static void setType(){
        Parser.consume(Tokens.SET);
        Parser.consume(Tokens.OF);
        indexType();
    }

    public static void fileType(){
        Parser.consume(Tokens.FILE);
        Parser.consume(Tokens.OF);
        type();
    }

    public static void varDeclPart(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.VAR){
            Parser.consume(Tokens.VAR);
            varDecl();
            Parser.consume(Tokens.PONTOVIRGULA);
            varDeclPart1();
        }
    }

    public static void varDeclPart1(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ID){
            varDecl();
            Parser.consume(Tokens.PONTOVIRGULA);
            varDeclPart1();
        }
    }

    public static void varDecl(){
        Tokens lookahead = Parser.lookahead.getToken();
        Parser.consume(Tokens.ID);
        while(Parser.lookahead.getToken() == Tokens.VIRGULA){
            Parser.consume(Tokens.VIRGULA);
            Parser.consume(Tokens.ID);
        }
        Parser.consume(Tokens.DOISPONTOS);
        type();
    }

    public static void procAndFuncDeclPart(){
        Tokens lookahead = Parser.lookahead.getToken();
        while(Parser.lookahead.getToken() == Tokens.FUNCTION || lookahead==Tokens.PROCEDURE){
            procOrFuncDecl();
        }
    }

    public static void procOrFuncDecl(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.PROCEDURE){
            procedureDecl();
        }
        else if(lookahead == Tokens.FUNCTION){
            functionDecl();
        }
        else{
            Parser.error(Tokens.ID,Parser.UNKNOWN_ERROR);
        }
    }

    public static void procedureDecl(){
        procedureHeading();
        block();
    }

    public static void procedureHeading(){
        Parser.consume(Tokens.PROCEDURE);
        Parser.consume(Tokens.ID);
        procedureHeading1();
    }

    public static void procedureHeading1(){
        Tokens lookahead = Parser.lookahead.getToken();

        if(lookahead == Tokens.PONTOVIRGULA){
            Parser.consume(Tokens.PONTOVIRGULA);
        }
        else if (lookahead == Tokens.ABREPAR){
            paramSection();
        }
        else{
            Parser.error(Tokens.ID,Parser.UNKNOWN_ERROR);
        }
    }

    public static void paramSection(){
        Tokens lookahead = Parser.lookahead.getToken();
        Parser.consume(Tokens.ABREPAR);
        formalParamSection();
        while(Parser.lookahead.getToken() == Tokens.PONTOVIRGULA){
            Parser.consume(Tokens.PONTOVIRGULA);
            formalParamSection();
        }
        Parser.consume(Tokens.FECHAPAR);
    }

    public static void formalParamSection(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ID){
            varDecl();
        }
        else if (lookahead == Tokens.VAR){
            Parser.consume(Tokens.VAR);
            varDecl();
        }
        else if(lookahead == Tokens.FUNCTION){
            Parser.consume(Tokens.FUNCTION);
            varDecl();
        }
        else if(lookahead == Tokens.PROCEDURE){
            Parser.consume(Tokens.PROCEDURE);
            Parser.consume(Tokens.ID);
            while(Parser.lookahead.getToken() == Tokens.VIRGULA){
                Parser.consume(Tokens.VIRGULA);
                Parser.consume(Tokens.ID);
            }
        }
        else{
            Parser.error(Tokens.ID,Parser.UNKNOWN_ERROR);
        }
    }

    public static void functionDecl(){
        functionHeading();
        block();
    }

    public static void functionHeading(){
        Parser.consume(Tokens.FUNCTION);
        Parser.consume(Tokens.ID);
        functionHeading1();
    }

    public static void functionHeading1(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.DOISPONTOS) {
            Parser.consume(Tokens.DOISPONTOS);
            Parser.consume(Tokens.ID);
            Parser.consume(Tokens.PONTOVIRGULA);
        }
        else if(lookahead == Tokens.ABREPAR){
            paramSection();
            Parser.consume(Tokens.DOISPONTOS);
            type();
            Parser.consume(Tokens.PONTOVIRGULA);
        }
    }

    public static void statement(){
        Tokens lookahead = Parser.lookahead.getToken();

        if(lookahead == Tokens.NUMINT){
            Parser.consume(Tokens.NUMINT);
            Parser.consume(Tokens.DOISPONTOS);
            unlabelledStatement();
        }
        else{
            unlabelledStatement();
        }
    }

    public static void unlabelledStatement(){
        Tokens lookahead = Parser.lookahead.getToken();

        if(lookahead == Tokens.BEGIN || lookahead == Tokens.IF||
                lookahead == Tokens.CASE || lookahead ==Tokens.WHILE ||
                lookahead == Tokens.REPEAT||lookahead == Tokens.FOR||
                lookahead == Tokens.WITH
            ){
            structuredStatement();
        }
        else{
            simpleStatement();
        }
    }

    public static void simpleStatement(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ID){
            Parser.consume(Tokens.ID);
            simpleStatement1();
        }
        else if (lookahead == Tokens.GOTO){
            goToStatement();
        }
    }

    public static void simpleStatement1(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ABREPAR){
            functionDesignator1();
        }
        else{
            assignment();
        }
    }

    public static void assignment(){
        variable2();
        Parser.consume(Tokens.DOISPONTOSIGUAL);
        expression();
    }

    public static void variable(){
        Parser.consume(Tokens.ID);
        variable2();
    }

    public static void variable2(){
        Tokens lookahead = Parser.lookahead.getToken();

        if(lookahead == Tokens.ABRERET){
            arrayVariable();
            variable2();
        }
        else if(lookahead == Tokens.PONTO){
            recordVariable();
            variable2();
        }
    }


    public static void recordVariable(){
        Parser.consume(Tokens.PONTO);
        Parser.consume(Tokens.ID);
    }


    public static void arrayVariable(){
        Tokens lookahead = Parser.lookahead.getToken();
        Parser.consume(Tokens.ABRERET);
        expression();
        while(Parser.lookahead.getToken() == Tokens.VIRGULA){
            Parser.consume(Tokens.VIRGULA);
            expression();
        }
        Parser.consume(Tokens.FECHARET);
    }

    public static void expression(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.MAIS|| lookahead == Tokens.MENOS||
                lookahead == Tokens.ID||lookahead == Tokens.NUMINT||
                lookahead == Tokens.NUMREAL||
                lookahead == Tokens.STRING ||
                lookahead == Tokens.NIL ||
                lookahead==Tokens.ABREPAR || lookahead == Tokens.NOT|
                lookahead == Tokens.ABRERET){
            simpleExpression();
            expression2();
        }
    }

    public static void expression2(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.IGUAL||lookahead == Tokens.DIFERENTE||
                lookahead == Tokens.MENOR||
                lookahead == Tokens.MENORIGUAL||
                lookahead == Tokens.MAIORIGUAL||
                lookahead == Tokens.MAIOR|| lookahead == Tokens.IN ){
            relationalOperator();
            simpleExpression();
        }
    }

    public static void relationalOperator(){
        Tokens lookahead = Parser.lookahead.getToken();
        switch(lookahead){
            case IGUAL:
                Parser.consume(Tokens.IGUAL);
                break;
            case DIFERENTE:
                Parser.consume(Tokens.DIFERENTE);
                break;
            case MENOR:
                Parser.consume(Tokens.MENOR);
                break;
            case MENORIGUAL:
                Parser.consume(Tokens.MENORIGUAL);
                break;
            case MAIOR:
                Parser.consume(Tokens.MAIOR);
                break;
            case MAIORIGUAL:
                Parser.consume(Tokens.MAIORIGUAL);
                break;
            case IN:
                Parser.consume(Tokens.IN);
                break;
            default:
                Parser.error(Tokens.MENORIGUAL,Parser.UNKNOWN_ERROR);
        }
    }

    public static void simpleExpression(){
        Tokens lookahead = Parser.lookahead.getToken();

        if(lookahead == Tokens.ID || lookahead == Tokens.NUMINT||
                lookahead == Tokens.NUMREAL||
                lookahead == Tokens.STRING ||
                lookahead == Tokens.NIL ||
                lookahead==Tokens.ABREPAR ||
                lookahead == Tokens.NOT||
                lookahead == Tokens.ABRERET ){
            term();
            simpleExpression1();
        }

        else if(lookahead == Tokens.MAIS || lookahead ==Tokens.MENOS){
            sign();
            term();
            simpleExpression1();
        }
        else{
            Parser.error(Tokens.ID,Parser.UNKNOWN_ERROR);
        }
    }

    public static void simpleExpression1(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.MAIS || lookahead ==Tokens.MENOS ||
        lookahead == Tokens.OR){
            addingOperator();
            term();
            simpleExpression1();
        }
    }

    public static void addingOperator(){
        Tokens lookahead = Parser.lookahead.getToken();
        switch(lookahead){
            case MAIS:
                Parser.consume(Tokens.MAIS);
                break;
            case MENOS:
                Parser.consume(Tokens.MENOS);
                break;
            case OR:
                Parser.consume(Tokens.OR);
                break;
            default:
                Parser.error(Tokens.MAIS,Parser.EXPECTED_ERROR);
                break;
        }
    }

    public static void term(){
        factor();
        term1();
    }

    public static void term1(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.VEZES || lookahead ==Tokens.DIVISAO ||
                lookahead == Tokens.DIV || lookahead == Tokens.MOD ||
                lookahead == Tokens.AND){
            multiplyingOperator();
            factor();
            term1();
        }
    }


    public static void multiplyingOperator(){
        Tokens lookahead = Parser.lookahead.getToken();
        switch(lookahead){
            case VEZES:
                Parser.consume(Tokens.VEZES);
                break;
            case DIVISAO:
                Parser.consume(Tokens.DIVISAO);
                break;
            case DIV:
                Parser.consume(Tokens.DIV);
                break;
            case MOD:
                Parser.consume(Tokens.MOD);
                break;
            case AND:
                Parser.consume(Tokens.AND);
                break;
            default:
                Parser.error(Tokens.VEZES,Parser.EXPECTED_ERROR);
                break;
        }
    }

    public static void factor(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ID){
            Parser.consume(Tokens.ID);
            factor2();
        }
        else if(lookahead == Tokens.NUMINT||
                lookahead == Tokens.NUMREAL||
                lookahead == Tokens.STRING || lookahead == Tokens.NIL){
            unsignedConstant();
        }
         else if(lookahead==Tokens.ABREPAR) {
             Parser.consume(Tokens.ABREPAR);
             expression();
             Parser.consume(Tokens.FECHAPAR);

        }
         else if(lookahead == Tokens.ABRERET){
             set();
        }
        else if (lookahead == Tokens.NOT){
            Parser.consume(Tokens.NOT);
            factor();
        }
        else{
            Parser.error(Tokens.MAIS,Parser.UNKNOWN_ERROR);
        }
    }

    public static void factor2(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.PONTO || lookahead == Tokens.ABRERET){
            variable2();
            factor3();
        }
        else{
            functionDesignator1();
        }
    }

    public static void factor3(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ABRERET){
            arrayVariable();
        }
        else if(lookahead == Tokens.PONTO){
            recordVariable();
        }
    }

    public static void unsignedConstant(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.NUMINT || lookahead ==Tokens.NUMREAL){
            unsignedNumber();
        }
        else if (lookahead == Tokens.STRING){
            Parser.consume(Tokens.STRING);
        }
        else if (lookahead == Tokens.NIL){
            Parser.consume(Tokens.NIL);
        }
        else{
            Parser.error(Tokens.INT,Parser.UNKNOWN_ERROR);
        }
    }

    public static void functionDesignator1(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.ABREPAR){
            Parser.consume(Tokens.ABREPAR);
            actualParamater();
            while(Parser.lookahead.getToken() == Tokens.VIRGULA){
                Parser.consume(Tokens.VIRGULA);
                actualParamater();
            }
            Parser.consume(Tokens.FECHAPAR);
        }
    }

    public static void set(){
        Parser.consume(Tokens.ABRERET);
        elementList();
        Parser.consume(Tokens.FECHARET);
    }

    public static void elementList(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.MAIS|| lookahead == Tokens.MENOS||
                lookahead == Tokens.ID||lookahead == Tokens.NUMINT||
                lookahead == Tokens.NUMREAL||
                lookahead == Tokens.STRING ||
                lookahead == Tokens.NIL ||
                lookahead==Tokens.ABREPAR || lookahead == Tokens.NOT|
                lookahead == Tokens.ABRERET){
            expression();
            while(Parser.lookahead.getToken()==Tokens.VIRGULA){
                Parser.consume(Tokens.VIRGULA);
                element();
            }
        }
    }

    public static void element(){
        expression();
        element1();
    }

    public static void element1(){
        Tokens lookahead = Parser.lookahead.getToken();
        if (lookahead == Tokens.PONTOPONTO){
            Parser.consume(Tokens.PONTOPONTO);
            expression();
        }
    }

    public static void actualParamater(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead ==Tokens.ID){
            Parser.consume(Tokens.ID);
            factor2();
            actualParamater1();
        }
        else if(lookahead == Tokens.ABREPAR){
            Parser.consume(Tokens.ABREPAR);
            expression();
            Parser.consume(Tokens.FECHAPAR);
            actualParamater2();
        }
        else if(lookahead == Tokens.ABRERET){
            set();
            actualParamater2();
        }
        else if (lookahead == Tokens.NOT){
            Parser.consume(Tokens.NOT);
            factor();
            actualParamater2();
        }
        else if(lookahead == Tokens.MAIS || lookahead==Tokens.MENOS){
            sign();
            term();
            expression2();
        }
        else if(lookahead == Tokens.NUMREAL||
                lookahead == Tokens.STRING ||
                lookahead == Tokens.NIL){
            unsignedConstant();
            actualParamater1();
        }
    }

    public static void actualParamater1(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.VEZES || lookahead ==Tokens.DIVISAO ||
                lookahead == Tokens.DIV || lookahead == Tokens.MOD ||
                lookahead == Tokens.AND){
            actualParamater2();
        }
    }

    public static void actualParamater2(){
        Tokens lookahead = Parser.lookahead.getToken();
        if(lookahead == Tokens.VEZES || lookahead ==Tokens.DIVISAO ||
                lookahead == Tokens.DIV || lookahead == Tokens.MOD ||
                lookahead == Tokens.AND){
            term1();
            simpleExpression1();
            expression2();
        }
        else{
            Parser.error(Tokens.EXCEPT,Parser.UNKNOWN_ERROR);
        }
    }




























}
