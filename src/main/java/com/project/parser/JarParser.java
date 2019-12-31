package com.project.parser;

import java.io.IOException;
import java.util.Objects;
import java.util.jar.JarFile;

import com.project.files.FilesCollector;
import com.project.files.JarFileC;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to parse a .jar file.
 *
 */
public class JarParser implements FileParserInterface {

    @Override
    /**
     * Collects all .class files from a .jar file.
     * @param name - the name of the .jar file
     * @return the FilesCollector of all .class files of the .jar file given.
     */
    public FilesCollector parseMyFile(String name) throws IOException {
        return listFilesForFolder(Objects.requireNonNull(name));
    }

    private FilesCollector listFilesForFolder(String name) throws IOException {
        if(!name.endsWith(".jar"))
            throw new IllegalArgumentException();
        var collector = new FilesCollector();
        var jar = new JarFile(Objects.requireNonNull(name));
        var entries = jar.entries();
        var iterator = entries.asIterator();
        while(iterator.hasNext()) {
        	var f = iterator.next();
        	if (f.getRealName().endsWith(".class")) {
        		collector.addFile(new JarFileC(f.getRealName(), f.getName(), name));
        	}
        }
        jar.close();
        return collector;
    }
}
