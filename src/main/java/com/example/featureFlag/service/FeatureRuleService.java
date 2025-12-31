package com.example.featureFlag.service;

import com.example.featureFlag.dto.CreateFeatureRuleRequest;
import com.example.featureFlag.entity.FeatureRule;
import org.springframework.http.ResponseEntity;


public interface FeatureRuleService {
    void saveFeatureRule(CreateFeatureRuleRequest createFeatureRuleRequest);
}
