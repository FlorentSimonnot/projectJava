package com.project.parser;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to throw exceptions when you parse your files for the project Retro.
 *
 */
public class ParserException extends Exception {

	/**
	 * Creates a new ParserException.
	 */
    public ParserException(){
        super();
    }

    /**
     * Creates a new ParserException.
     * @param s - the error message you want to display
     */
    public ParserException(String s){
        super(s);
    }

}
