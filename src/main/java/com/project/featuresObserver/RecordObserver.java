package com.project.featuresObserver;

import com.project.featuresObserver.FeatureObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to observe the behavior of a record.
 *
 */
public class RecordObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    /**
     * Adds the text format into the list of String when the record feature is detected.
     * @param memthodName - the text format that is displayed when the record is detected
     * @param featureName - the feature's name
     * 
     */
    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("record"))
            features.add(methodName);
    }

    /**
     * Displays the list of String corresponding of all records detected into a .class file.
     */
    @Override
    public void showFeatures() {
        features.forEach(f-> System.out.println(f));
    }
}
