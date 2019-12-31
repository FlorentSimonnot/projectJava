package com.project.featuresObserver;


/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to observe the behavior of an Instruction.
 * It allows to notify the user when an Instruction is detected in the .class file.
 * This class is used by the classes MyMethodVisitor and MyClassVisitor.
 *
 */
public interface FeatureObserver {
	
	/**
     * Does an action when the feature is detected.
     * @param memthodName - the text format that is displayed when a feature is detected
     * @param featureName - the feature's name
     */
    void onFeatureDetected(String methodName, String featureName);
    //List<String> getFeaturesFound();
    
    /**
     * Displays the list of String corresponding of all features detected into a .class file.
     */
    void showFeatures();
}
