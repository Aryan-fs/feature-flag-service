package com.example.featureFlag.exception;

import com.example.featureFlag.entity.RuleType;

public class InvalidRuleValueException extends RuntimeException {
    public InvalidRuleValueException(RuleType ruleType, String ruleValue) {
        super("Invalid rule value, ruleType: " + ruleType + ", ruleValue: " + ruleValue);
    }
}
