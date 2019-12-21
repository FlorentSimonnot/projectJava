package com.project.featuresObserver;

import com.project.featuresObserver.FeatureObserver;

import java.util.ArrayList;
import java.util.List;

public class RecordObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("record"))
            features.add(methodName);
    }

    @Override
    public void showFeatures() {
        features.forEach(f-> System.out.println(f));
    }
}
