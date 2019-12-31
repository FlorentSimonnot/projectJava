package fr.project.parsing.parser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * 
 * A Factory that allows to create a parser for directory, .class file or .jar file.
 * @author SIMONNOT Florent
 * 
 */
public class ParserFactory {

    /**
     * Creates a parser according to the type of file
     * @param path - the path of the file
     * @return FileParserInterface - a parser according to the type of file
     * @throws IOException - if we can't read the file
     */
    public static FileParserInterface createParser(Path path) throws IOException {
        if(Files.isDirectory(Objects.requireNonNull(path))){
            return new DirectoryParser();
        }else{
            var probContentType = Files.probeContentType(Objects.requireNonNull(path));
            if(probContentType.equals("application/java-vm")){
                return new FileClassParser();
            }
            else if(probContentType.equals("application/java-archive")){
                return new JarParser();
            }
            else{
                throw new IllegalArgumentException();
            }
        }
    }
}
