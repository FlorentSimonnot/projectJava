package fr.project.warningObservers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A class that allows to observe the behavior of a lambda instruction.
 * @author SIMONNOT Florent
 *
 */
public class WarningLambdaObserver implements WarningObserver {
    private final List<String> warnings = new ArrayList<>();

    @Override
    public void onWarningDetected(String warning, String token) {
        if(token.equals("lambda")) warnings.add(warning);
    }

    @Override
    public void showWarning() {
        if(warnings.size() == 0) return;
        System.err.println("We found " +  warnings.size() + " lambda !");
        warnings.forEach(System.out::println);
    }

    @Override
    public int numberOfWarningsDetected() {
        return warnings.size();
    }
}
