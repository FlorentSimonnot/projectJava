package com.project.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OptionsParser {

    public static Options parseOptions(String[] args, OptionFactory factory){
        Objects.requireNonNull(factory);
        var optionsList = new ArrayList<Option>();
        for (String arg : Objects.requireNonNull(args)) {
            //Add new option
            if (arg.startsWith("--")) {
                checkLastArgument(optionsList);
                optionsList.add(factory.createOption(arg));
            }
            //Set arg for last option
            else{
                if(optionsList.size() > 0) {
                    optionsList.get(optionsList.size() - 1).setArgs(arg);
                }
            }
        }
        checkLastArgument(optionsList);
        return new Options(optionsList);
    }


    private static void checkLastArgument(List<Option> optionsList){
        if(Objects.requireNonNull(optionsList).size() > 0) {
            var last = optionsList.get(optionsList.size() - 1);
            if(OptionUtils.lastOptionWaitingArgument(last)){
                throw new IllegalStateException();
            }
        }
    }
}
