package com.project.options;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to parse all the options of the project Retro.
 * It creates a list of options according to the arguments when you run the project Retro.
 *
 */
public class OptionsParser {

	/**
	 * Creates a list of Option according to the arguments of the run command of Retro.
	 * @param args - the arguments of the run command
	 * @param factory - an OptionFactory
	 * @return the list of Option according to the arguments of the run command of Retro.
	 */
    public static Options parseOptions(String[] args, OptionFactory factory){
        Objects.requireNonNull(factory);
        var optionsList = new ArrayList<Option>();
        for (String arg : Objects.requireNonNull(args)) {
            if (arg.startsWith("--")) {
                checkLastArgument(optionsList);
                optionsList.add(factory.createOption(arg));
            }
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
