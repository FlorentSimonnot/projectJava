package com.project.parser;

import com.project.files.FileClass;
import com.project.files.FileInterface;

import java.util.Objects;

public class FileClassParser implements FileParserInterface {

    @Override
    public FileInterface parseMyFile(String name) {
        return new FileClass(Objects.requireNonNull(name));
    }
}
