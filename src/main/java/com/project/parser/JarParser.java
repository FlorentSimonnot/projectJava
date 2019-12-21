package com.project.parser;

import com.project.files.FileClass;
import com.project.files.FilesCollector;
import com.project.files.JarFileC;

import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarParser implements FileParserInterface {

    @Override
    public FilesCollector parseMyFile(String name) throws ParserException {
        return listFilesForFolder(Objects.requireNonNull(name));
    }

    private FilesCollector listFilesForFolder(String name) throws ParserException {
        var collector = new FilesCollector();
        if(!name.endsWith(".jar"))
            throw new ParserException();
        try {
            var jar = new JarFile(Objects.requireNonNull(name));
            Enumeration<JarEntry> entries = jar.entries();
            while(entries.hasMoreElements()) {
                var n = entries.nextElement().getName();
                var split = n.split("/");
                if(split.length > 0){
                    var nameFile = split[split.length-1];
                    if(nameFile.endsWith(".class")){
                        collector.addFile(new JarFileC(nameFile, n, name));
                    }
                }
            }
        } catch (IOException e) {
            throw new ParserException();
        }
        return collector;
    }
}
