package com.project.options;

import java.util.HashMap;

public class OptionFactory {
    private final HashMap<String, Option> map = new HashMap<>();

    public void register(String regex, Option parserFileInterface){
        map.putIfAbsent(regex, parserFileInterface);
    }

    public Option createOption(String name){
        if(!map.containsKey(name))
            throw new IllegalStateException();
        return map.get(name);
    }
}
