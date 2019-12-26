package com.project.parser;

import com.project.files.DirectoryFile;
import com.project.files.FileClass;
import com.project.files.FilesCollector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to parse a directory.
 *
 */
public class DirectoryParser implements FileParserInterface {

	@Override
	/**
	 * Collects all the .class files of a directory.
	 * @param name - the name of the directory you want to parse
	 * @return the FilesCollector of all .class files in the directory given
	 */
	public FilesCollector parseMyFile(String name) throws ParserException, IOException {
		return listFilesForFolder(name);
	}

	private FilesCollector listFilesForFolder(String name) throws ParserException, IOException {
		if(Objects.requireNonNull(name).contains("."))
			throw new ParserException("We can't accept this type of file");
		var collector = new FilesCollector();
		var folder = Files.newDirectoryStream(Path.of(name));
		for (var path : folder) {
			if (path.getFileName().toString().endsWith(".class")) {
				collector.addFile(new DirectoryFile(path.getFileName().toString(), path.toString()));
			}
		}
		return collector;
	}
}
