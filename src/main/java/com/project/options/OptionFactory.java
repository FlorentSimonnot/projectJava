package com.project.options;

import java.util.HashMap;

/**
 * 
 * @author SIMONNOT Florent
 * A Factory that associate an option as String with an Option.
 *
 */
public class OptionFactory {
    private final HashMap<String, Option> map = new HashMap<>();

    /**
     * Registers an option and its Option in the map.
     * @param regex - the name of the option
     * @param parserFileInterface - the Option asked
     */
    public void register(String regex, Option parserFileInterface){
        map.putIfAbsent(regex, parserFileInterface);
    }

    /**
     * Gets the Option corresponding to the name given.
     * @param name - the name of the option
     * @return the Option corresponding to the name given.
     */
    public Option createOption(String name){
        if(!map.containsKey(name))
            throw new IllegalStateException();
        return map.get(name);
    }
}
