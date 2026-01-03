package com.example.featureFlag.service;

import com.example.featureFlag.dto.EvaluationContext;
import com.example.featureFlag.dto.EvaluationResult;
import com.example.featureFlag.entity.FeatureFlag;
import com.example.featureFlag.entity.FeatureRule;
import com.example.featureFlag.repository.FeatureFlagRepository;
import com.example.featureFlag.repository.FeatureRuleRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureRuleRepository featureRuleRepository;

    public EvaluationServiceImpl(FeatureFlagRepository featureFlagRepository, FeatureRuleRepository featureRuleRepository) {
        this.featureFlagRepository = featureFlagRepository;
        this.featureRuleRepository = featureRuleRepository;
    }

    @Override
    @Cacheable(
            cacheNames = "feature-evaluations",
            key = "#flagKey + '|' + #context.environment + '|' + (#context.variables['userId'] ?: '__anonymous__')",
            unless = "#result == null"
    )
    public EvaluationResult evaluate(String flagKey, EvaluationContext context) {
        EvaluationResult evaluationResult = new EvaluationResult();
        FeatureFlag featureFlag = featureFlagRepository.findByKeyAndEnvironment(flagKey, context.getEnvironment());
        if (featureFlag == null || !featureFlag.getEnabled()) {
            evaluationResult.setEnabled(false);
            evaluationResult.setReason(EvaluationReason.FLAG_DISABLED);
            return evaluationResult;
        }

        List<FeatureRule> featureRules = featureRuleRepository.findByFeatureFlagIdAndEnabledTrueOrderByPriorityAsc(featureFlag.getId());
        Map<String, ?> variables = (context.getVariables() != null)?context.getVariables():Map.of();
        for(FeatureRule featureRule : featureRules) {
            String userId = null;
            if(variables.get("userId") != null) {
                userId = (String) variables.get("userId");
            }
            switch (featureRule.getRuleType()) {
                case USER_ID:
                    if(userId == null) continue;
                    String[] values = featureRule.getRuleValue().split(",");
                    for(String value : values) {
                        if (userId.equals(value.trim())) {
                            evaluationResult.setEnabled(true);
                            evaluationResult.setReason(EvaluationReason.USER_RULE_MATCH);
                            return evaluationResult;
                        }
                    }
                    continue;
                case PERCENTAGE:
                    if(userId == null) continue;
                    int threshold;
                    try {
                        threshold = Integer.parseInt(featureRule.getRuleValue().trim());
                    } catch(NumberFormatException e) {
                        continue;
                    }
                    int bucket = Math.floorMod((flagKey + ":" + userId).hashCode(), 100);
                    if(bucket<threshold) {
                        evaluationResult.setEnabled(true);
                        evaluationResult.setReason(EvaluationReason.PERCENTAGE_ROLLOUT);
                        return evaluationResult;
                    }
                    continue;
                case ENVIRONMENT:
                    String curEnvironment = context.getEnvironment().name();
                    if(curEnvironment.equals(featureRule.getRuleValue().trim())) {
                        evaluationResult.setEnabled(true);
                        evaluationResult.setReason(EvaluationReason.ENVIRONMENT_MATCH);
                        return evaluationResult;
                    }
            }
        }
        evaluationResult.setReason(EvaluationReason.NO_RULE_MATCH);
        return evaluationResult;
    }
}
