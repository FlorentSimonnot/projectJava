package com.project.main;

import com.project.parser.*;
import org.objectweb.asm.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class App {

    public static void main(String[] args){

        var fileFactory = new FileFactory();

        /*
         *********   Add our pattern and parser in our factory ******
         */
        fileFactory.register(
               ".*\\.class",
                new FileClassParser()
        );

        fileFactory.register(
               ".*\\.jar",
                new JarParser()
        );

        fileFactory.register(
                ".*",
                new DirectoryParser()
        );

        var res = FileParser.parseFile(args[0], fileFactory);
        System.out.println(res.getVersions());

        //res.forEach(l -> System.out.println(l.getName()));

    }

}
