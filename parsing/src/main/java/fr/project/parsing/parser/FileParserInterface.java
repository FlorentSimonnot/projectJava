package fr.project.parsing.parser;

import java.io.IOException;

import fr.project.parsing.files.FilesCollector;

/**
 * 
 * @author SIMONNOT Florent
 * An interface which its goal is to parse a file.
 *
 */
public interface FileParserInterface {

	/**
	 * Collects all type of file from a file given.
	 * @param name - the name of the file you want to parse
	 * @return the FilesCollector of all type of file of the file given
	 * @throws ParserException
	 */
    FilesCollector parseMyFile(String name) throws ParserException, IOException;

}
