package com.project.options;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Options {
    private final List<Option> options;

    public Options(List<Option> options){
        this.options = options;
    }

    public List<Option> getOptions(){
        return options;
    }

    public boolean helpIsDemanding(){
        return options.contains(new Option(Option.OptionEnum.HELP));
    }

    public void forEach(Consumer<? super Option> action){
        options.forEach(action);
    }

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
