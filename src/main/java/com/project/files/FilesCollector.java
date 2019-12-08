package com.project.files;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class FilesCollector {
    private final List<FileInterface> collector = new ArrayList<>();

    public boolean addFile(FileInterface fileClass){
        return collector.add(fileClass);
    }

    /**
     * return a string which show the java version for each file in the collector
     * @return
     */
    public String getVersions(){
        if(collector.size() == 0)
            return "No files in the collector";
        var joiner = new StringJoiner("\n");
        collector.forEach(f ->{
            joiner.add(f.getName()+" - Java "+f.getVersion());
        });
        return joiner.toString();
    }

    public int getSize(){
        return collector.size();
    }

    public void forEach(Consumer<? super FileInterface> action){
        collector.forEach(action);
    }

}
