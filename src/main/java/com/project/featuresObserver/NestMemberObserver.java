package com.project.featuresObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to observe the behavior of a nest member instruction.
 *
 */
public class NestMemberObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    /**
     * Adds the text format into the list of String when the nest member feature is detected.
     * 
     */
    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("nestMember"))
            features.add(methodName);
    }

    /**
     * Displays the list of String corresponding of all nest member detected into a .class file.
     */
    @Override
    public void showFeatures() {
        features.forEach(f-> System.out.println(f));
    }
}
