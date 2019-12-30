package fr.project.detection.observers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A observer that observes the behavior of a concatenation instruction.
 * It allows to notify the user when a concatenation instruction is detected in the .class file.
 * @author SIMONNOT Florent
 *
 */
public class ConcatenationObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    /**
     * Adds the text format into the list of String when the concatenation feature is detected.
     * 
     */
    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("concatenation"))
            features.add(methodName);
    }

    /**
     * Displays the list of String corresponding of all concatenations detected into a .class file.
     */
    @Override
    public void showFeatures() {
        features.forEach(f-> System.out.println(f));
    }
}
