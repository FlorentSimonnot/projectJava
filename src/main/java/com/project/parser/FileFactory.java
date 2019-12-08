package com.project.parser;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

public class FileFactory {
    final HashMap<String, FileParserInterface> map = new HashMap<>();

    public void register(String regex, FileParserInterface parserFileInterface){
        map.putIfAbsent(regex, parserFileInterface);
    }
}
