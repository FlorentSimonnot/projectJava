package com.project.featuresObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to observe the behavior of a concatenation instruction.
 *
 */
public class ConcatenationObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    /**
     * Adds the text format into the list of String when the concatenation feature is detected.
     * @param memthodName - the text format that is displayed when the concatenation is detected
     * @param featureName - the feature's name
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
