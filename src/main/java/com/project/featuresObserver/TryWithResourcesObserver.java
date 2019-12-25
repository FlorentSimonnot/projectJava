package com.project.featuresObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to observe the behavior of a try-with-resources instruction.
 *
 */
public class TryWithResourcesObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    /**
     * Adds the text format into the list of String when the try-with-resources feature is detected.
     * @param memthodName - the text format that is displayed when the try-with-resources is detected
     * @param featureName - the feature's name
     * 
     */
    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("addSuppressed"))
            features.add(methodName);
    }

    /**
     * Displays the list of String corresponding of all try-with-resources detected into a .class file.
     */
    @Override
    public void showFeatures() {
        features.forEach(f-> System.out.println(f));
    }
}
