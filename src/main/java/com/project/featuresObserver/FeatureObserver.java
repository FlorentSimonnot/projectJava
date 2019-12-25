package com.project.featuresObserver;

import java.util.List;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to observe the behavior of an Instruction
 *
 */
public interface FeatureObserver {
	
	/**
     * Does an action when the feature is detected.
     * @param memthodName - the text format that is displayed when the concatenation is detected
     * @param featureName - the feature's name
     * 
     */
    void onFeatureDetected(String methodName, String featureName);
    //List<String> getFeaturesFound();
    
    /**
     * Displays the list of String corresponding of all features detected into a .class file.
     */
    void showFeatures();
}
