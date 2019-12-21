package com.project.featuresObserver;

import java.util.ArrayList;
import java.util.List;

public class TryWithResourcesObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("addSuppressed"))
            features.add(methodName);
    }

    @Override
    public void showFeatures() {
        features.forEach(f-> System.out.println(f));
    }
}
