package fr.project.parsing.parser;

import fr.project.parsing.files.FileClass;
import fr.project.parsing.files.FilesCollector;

import java.util.Objects;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to parse a .class file.
 *
 */
public class FileClassParser implements FileParserInterface {

    @Override
    /**
     * Collects a .class file according to a name given.
     * @param name - the name of the file you want to collect
     * @return the FilesCollector of .class file of the file given
     */
    public FilesCollector parseMyFile(String name) throws ParserException {
        Objects.requireNonNull(name);
        var collector = new FilesCollector();
        if(!name.endsWith(".class"))
            throw new ParserException();
        collector.addFile(new FileClass(name));
        return collector;
    }
}
