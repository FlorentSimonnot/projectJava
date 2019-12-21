package com.project.featuresObserver;

import java.util.ArrayList;
import java.util.List;

public class NestMemberObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("nestMember"))
            features.add(methodName);
    }

    @Override
    public void showFeatures() {
        features.forEach(f-> System.out.println(f));
    }
}
