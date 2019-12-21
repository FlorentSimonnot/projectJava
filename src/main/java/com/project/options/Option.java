package com.project.options;

import java.util.List;

public class Option {
    private final OptionEnum option;
    private String args = "";

    public enum OptionEnum{
        HELP,
        INFO,
        TARGET,
        FEATURES,
        NULL
    }

    public Option(OptionEnum option){
        this.option = option;
    }

    public void setArgs(String args){
        if(option == OptionEnum.TARGET || option == OptionEnum.FEATURES)
            this.args = args;
        else{
            throw new IllegalStateException("This option doesn't take an argument");
        }
    }

    public OptionEnum getOption(){
        return option;
    }

    public boolean argsIsEmpty(){
        return args.isEmpty();
    }

    public String getArgs(){
        return args;
    }

    @Override
    public String toString(){
        var space = "";
        if(args.length() > 0)
            space = " " + args;
        return option.name() + space;
    }

}
