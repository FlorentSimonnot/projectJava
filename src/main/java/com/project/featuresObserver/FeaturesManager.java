package com.project.featuresObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeaturesManager {

    private FeaturesManager(){}

    public static List<FeatureObserver> createObservers(String args, FeaturesObserverFactory factory){
        List<FeatureObserver> observers = new ArrayList<>();
        var splitArgs = args.subSequence(1, args.length()-1).toString();
        var argsArray = splitArgs.split(",");
        Arrays.stream(argsArray).map(a -> a.strip()).forEach(arg -> observers.add(factory.getObserver(arg)));
        return observers;
    }


}
