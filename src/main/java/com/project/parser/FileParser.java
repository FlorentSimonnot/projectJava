package com.project.parser;

import com.project.files.FilesCollector;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to parse all type of file.
 * Simply creates a collector of .class files according to file as argument.
 * The argument can be a .class file, a directory or a .jar file.
 *
 */
public class FileParser {

	/**
	 * Collects all files from a file given.
	 * @param name - the name of a file
	 * @return the FilesCollector that contains all type of file of the file given.
	 * @throws IOException
	 * @throws ParserException
	 */
    public static FilesCollector parseFile(String name) throws IOException {
        Objects.requireNonNull(name);
        return ParserFactory.createParser(Paths.get(name)).parseMyFile(name);
    }
}
