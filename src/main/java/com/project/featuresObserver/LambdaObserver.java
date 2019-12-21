package com.project.featuresObserver;

import java.util.ArrayList;
import java.util.List;

public class LambdaObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("lambda"))
            features.add(methodName);
    }

    @Override
    public void showFeatures() {
        features.forEach(f-> System.out.println(f));
    }
}
