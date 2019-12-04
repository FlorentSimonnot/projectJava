package com.project.parser;

import com.project.files.FileInterface;

import java.util.regex.Pattern;

public class FileParser {

    /**
     * FileInterface parseFile(String name, FileFactory factory)
     * @param name the name of file call in options
     * @param factory the factory which create a FileInterface
     * @return a FileInterface in according with the file extension
     */
    public static FileInterface parseFile(String name, FileFactory factory){
        for(var entry : factory.map.entrySet()){
            if(Pattern.matches(entry.getKey(), name)){
                return entry.getValue().parseMyFile(name);
            }
        }
        throw new IllegalArgumentException("Please verify your file extension");
    }
}
