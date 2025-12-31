package com.example.featureFlag.exception;

import com.example.featureFlag.common.Environment;

public class FeatureFlagNotFoundException extends RuntimeException {
    public FeatureFlagNotFoundException(String flagKey, Environment environment) {
        super("Feature Flag Not Found: " + flagKey +" in " + environment);
    }
}
