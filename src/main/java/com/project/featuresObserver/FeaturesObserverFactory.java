package com.project.featuresObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class FeaturesObserverFactory {
    private final HashMap<String, FeatureObserver> register = new HashMap<>();

    /**
     * public void register(String feature, FeatureObserver observer)
     * Register a feature and its observer in the register.
     * @param feature
     * @param observer
     */
    public void register(String feature, FeatureObserver observer){
        this.register.putIfAbsent(feature, observer);
    }

    FeatureObserver getObserver(String feature){
        return register.get(feature);
    }

}
