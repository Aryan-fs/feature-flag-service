package com.example.featureFlag.service;

import com.example.featureFlag.dto.EvaluationContext;
import com.example.featureFlag.dto.EvaluationResult;

import java.util.HashMap;

// Contract
public interface EvaluationService {
    public EvaluationResult evaluate(String flagKey, EvaluationContext context);
}
