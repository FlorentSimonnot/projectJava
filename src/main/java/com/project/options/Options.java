package com.project.options;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 
 * @author SIMONNOT Florent
 * A class that stores all options given when you run the project Retro.
 *
 */
public class Options {
    private final List<Option> options;

    /**
     * Creates a new Options.
     * @param options - a list of Option
     */
    public Options(List<Option> options){
        this.options = options;
    }

    /**
     * Gets all options
     * @return the list of Option
     */
    public List<Option> getOptions(){
        return options;
    }

    /**
     * Tests if the option [--help] is asked.
     * @return true if the option [--help] is asked, false if not.
     */
    public boolean helpIsDemanding(){
        return options.contains(new Option(Option.OptionEnum.HELP));
    }

    /**
     * Applies an action for each option of the options.
     * @param action - a Consumer you want to apply to your options
     */
    public void forEach(Consumer<? super Option> action){
        options.forEach(action);
    }

    /**
     * Gets all the arguments of a given OptionEnum
     * @param option - the OptionEnum you want to gets the arguments
     * @return all the arguments of the given OptionEnum
     */
    public String getArgsOption(Option.OptionEnum option){
        if(containOption(option)){
            return findOption(option).getArgs();
        }
        return new Option(Option.OptionEnum.NULL).getArgs();
    }

    private boolean containOption(Option.OptionEnum option){
        for(Option op : options){
            if(op.getOption() == option)
                return true;
        }
        return false;
    }

    private Option findOption(Option.OptionEnum option){
        if(containOption(option)){
            for(Option op : options){
                if(op.getOption().equals(option)){
                    return op;
                }
            }
        }
        throw new IllegalArgumentException();
    }

}
