package fr.project.detection.observers;

/**
 * 
 * @author SIMONNOT Florent
 * A class that allows to observe the behavior of an Instruction
 *
 */
public interface FeatureObserver {
	
	/**
     * Does an action when the feature is detected.
     * @param methodName - the text format that is displayed when a feature is detected
     * @param featureName - the feature's name
     */
    void onFeatureDetected(String methodName, String featureName);
    //List<String> getFeaturesFound();
    
    /**
     * Displays the list of String corresponding of all features detected into a .class file.
     */
    void showFeatures();
}
