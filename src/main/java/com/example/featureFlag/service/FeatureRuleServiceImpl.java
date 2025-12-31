package com.example.featureFlag.service;

import com.example.featureFlag.common.Environment;
import com.example.featureFlag.dto.CreateFeatureRuleRequest;
import com.example.featureFlag.entity.FeatureFlag;
import com.example.featureFlag.entity.FeatureRule;
import com.example.featureFlag.entity.RuleType;
import com.example.featureFlag.exception.FeatureFlagNotFoundException;
import com.example.featureFlag.exception.InvalidRuleValueException;
import com.example.featureFlag.repository.FeatureFlagRepository;
import com.example.featureFlag.repository.FeatureRuleRepository;
import org.springframework.stereotype.Service;

@Service
public class FeatureRuleServiceImpl implements FeatureRuleService {

    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureRuleRepository featureRuleRepository;

    FeatureRuleServiceImpl(FeatureFlagRepository featureFlagRepository, FeatureRuleRepository featureRuleRepository) {
        this.featureFlagRepository = featureFlagRepository;
        this.featureRuleRepository = featureRuleRepository;
    }

    @Override
    public void saveFeatureRule(CreateFeatureRuleRequest createFeatureRuleRequest) {
        String flagKey = createFeatureRuleRequest.getFlagKey();
        Environment environment = createFeatureRuleRequest.getFlagEnvironment();

        FeatureFlag featureFlag = featureFlagRepository.findByKeyAndEnvironment(flagKey, environment);

        if(featureFlag == null) throw new FeatureFlagNotFoundException(flagKey,  environment);

        FeatureRule featureRule = new FeatureRule();
        featureRule.setFeatureFlag(featureFlag);
        featureRule.setRuleType(createFeatureRuleRequest.getRuleType());
        featureRule.setRuleValue(createFeatureRuleRequest.getRuleValue());
        featureRule.setPriority(createFeatureRuleRequest.getPriority());
        featureRule.setEnabled(createFeatureRuleRequest.isEnabled());

        if(featureRule.getRuleType() == RuleType.USER_ID) {
            if(!featureRule.getRuleValue().isEmpty()) {
                String[] ruleValues = featureRule.getRuleValue().split(",");
                for(String ruleValue : ruleValues) {
                    if(!ruleValue.trim().isEmpty())continue;
                    throw new InvalidRuleValueException(RuleType.USER_ID, featureRule.getRuleValue());
                }
            }
        } else if (featureRule.getRuleType() == RuleType.PERCENTAGE) {
            try {
                int value = Integer.parseInt(featureRule.getRuleValue().trim());
                if(value < 0 || value > 100) throw new InvalidRuleValueException(RuleType.PERCENTAGE, featureRule.getRuleValue());
            } catch (NumberFormatException e) {
                throw new InvalidRuleValueException(RuleType.PERCENTAGE, featureRule.getRuleValue());
            }
        }

        featureRuleRepository.save(featureRule);
    }
}
