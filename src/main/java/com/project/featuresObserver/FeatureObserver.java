package com.project.featuresObserver;

import java.util.List;

public interface FeatureObserver {
    void onFeatureDetected(String methodName, String featureName);
    //List<String> getFeaturesFound();
    void showFeatures();
}
