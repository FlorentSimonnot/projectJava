package com.project.featuresObserver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author SIMONNOT Florent
 * A Factory that associate a feature as String with a FeatureObserver.
 *
 */
public class FeaturesObserverFactory {
    private final HashMap<String, FeatureObserver> register = new HashMap<>();

    /**
     * Registers a feature and its observer in the register.
     * @param feature
     * @param observer
     */
    public void register(String feature, FeatureObserver observer){
        this.register.putIfAbsent(feature, observer);
    }

    /**
     * Gets the Observer according to the feature.
     * @param feature - a simple String
     * @return the FeatureObserver asked.
     */
    FeatureObserver getObserver(String feature){
        return register.get(feature);
    }

}
