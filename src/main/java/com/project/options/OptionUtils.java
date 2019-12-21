package com.project.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OptionUtils {
    private static final String[] featuresArray = new String[]{"try-with-resources", "nestMember", "lambda", "record", "concatenation"};
    private static final List<String> featuresList = new ArrayList<>(Arrays.asList(featuresArray));

    public static String checkArgument(Option.OptionEnum optionEnum, String argument){
        Objects.requireNonNull(optionEnum, argument);
        switch(optionEnum){
            case TARGET: return checkVersionNumber(argument);
            case FEATURES: return checkListOfFeatures(argument);
            default: throw new IllegalStateException("This option can't take this argument");
        }
    }

    static boolean lastOptionWaitingArgument(Option option){
        if(option.getOption() == Option.OptionEnum.TARGET || option.getOption() == Option.OptionEnum.FEATURES){
            return option.argsIsEmpty();
        }
        return false;
    }

    private static String checkVersionNumber(String arg){
        var version = Integer.parseInt(Objects.requireNonNull(arg));
        if(version > 4 && version < 14){
            return arg;
        }
        throw new IllegalArgumentException("This argument is wrong !");
    }

    private static String checkListOfFeatures(String arg){
        Objects.requireNonNull(arg);
        var listTrim = arg.replaceAll("\\s", "");
        var features = listTrim.subSequence(1, listTrim.length()-1).toString().split(",");
        if(features.length > 0){
            for(String feature : features){
                if(!featuresList.contains(feature)){
                    throw new IllegalArgumentException(feature + " is invalid !");
                }
            }
            return listTrim;
        }
        throw new IllegalArgumentException("Arguments are invalids !");
    }

}
