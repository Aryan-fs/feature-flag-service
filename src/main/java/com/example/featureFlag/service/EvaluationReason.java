package com.example.featureFlag.service;

public enum EvaluationReason {
    FLAG_DISABLED,
    USER_RULE_MATCH,
    PERCENTAGE_ROLLOUT,
    ENVIRONMENT_MATCH,
    NO_RULE_MATCH,
}
