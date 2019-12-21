package com.project.parser;

import com.project.files.FilesCollector;

public interface FileParserInterface {

    FilesCollector parseMyFile(String name) throws ParserException;

}
