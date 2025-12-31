package com.example.featureFlag.service;

import com.example.featureFlag.common.Environment;
import com.example.featureFlag.dto.EvaluationContext;
import com.example.featureFlag.dto.EvaluationResult;
import com.example.featureFlag.entity.FeatureFlag;
import com.example.featureFlag.entity.FeatureRule;
import com.example.featureFlag.entity.RuleType;
import com.example.featureFlag.repository.FeatureFlagRepository;
import com.example.featureFlag.repository.FeatureRuleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EvaluationServiceImplTests {

    @Mock
    FeatureFlagRepository featureFlagRepository;
    @Mock
    FeatureRuleRepository featureRuleRepository;

    @InjectMocks
    EvaluationServiceImpl evaluationService;

    @Test
    void shouldDisableWhenFlagNotFound() {
        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION))
                .thenReturn(null);

        EvaluationContext evaluationContext = new EvaluationContext();
        evaluationContext.setEnvironment(Environment.PRODUCTION);
        evaluationContext.setVariables(Map.of("userId", "123"));

        EvaluationResult result = evaluationService.evaluate("new_ui", evaluationContext);

        assertFalse(result.isEnabled());
        Assertions.assertEquals(EvaluationReason.FLAG_DISABLED, result.getReason());
    }

    @Test
    void shouldDisableWhenFlagExistsButDisabled() {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setKey("new_ui");
        featureFlag.setEnvironment(Environment.PRODUCTION);
        featureFlag.setEnabled(false);
        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION))
                .thenReturn(featureFlag);

        EvaluationContext evaluationContext = new EvaluationContext();
        evaluationContext.setEnvironment(Environment.PRODUCTION);
        evaluationContext.setVariables(Map.of("userId", "123"));

        EvaluationResult result = evaluationService.evaluate("new_ui", evaluationContext);
        Assertions.assertFalse(result.isEnabled());
        Assertions.assertEquals(EvaluationReason.FLAG_DISABLED, result.getReason());
    }

    @Test
    void shouldEnableWhenUserRuleMatches() {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setId(UUID.randomUUID());
        featureFlag.setKey("new_ui");
        featureFlag.setEnvironment(Environment.PRODUCTION);
        featureFlag.setEnabled(true);
        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION))
                .thenReturn(featureFlag);

        FeatureRule featureRule = new FeatureRule();
        featureRule.setRuleType(RuleType.USER_ID);
        featureRule.setEnabled(true);
        featureRule.setRuleValue("123 ");
        featureRule.setFeatureFlag(featureFlag);
        featureRule.setPriority(1);
        when(featureRuleRepository.findByFeatureFlagIdAndEnabledTrueOrderByPriorityAsc(featureFlag.getId()))
                .thenReturn(List.of(featureRule));

        EvaluationContext evaluationContext = new EvaluationContext();
        evaluationContext.setEnvironment(Environment.PRODUCTION);
        evaluationContext.setVariables(Map.of("userId", "123"));

        EvaluationResult result = evaluationService.evaluate("new_ui", evaluationContext);
        Assertions.assertTrue(result.isEnabled());
        Assertions.assertEquals(EvaluationReason.USER_RULE_MATCH, result.getReason());
    }

    @Test
    void shouldFallbackToPercentageRuleWhenUserRuleDoesNotMatch() {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setId(UUID.randomUUID());
        featureFlag.setKey("new_ui");
        featureFlag.setEnvironment(Environment.PRODUCTION);
        featureFlag.setEnabled(true);
        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION))
                .thenReturn(featureFlag);

        FeatureRule featureRule1 = new FeatureRule();
        featureRule1.setRuleType(RuleType.USER_ID);
        featureRule1.setEnabled(true);
        featureRule1.setRuleValue("123 ");
        featureRule1.setFeatureFlag(featureFlag);
        featureRule1.setPriority(1);

        FeatureRule featureRule2 = new FeatureRule();
        featureRule2.setRuleType(RuleType.PERCENTAGE);
        featureRule2.setEnabled(true);
        featureRule2.setRuleValue("100");
        featureRule2.setFeatureFlag(featureFlag);
        featureRule2.setPriority(2);

        when(featureRuleRepository.findByFeatureFlagIdAndEnabledTrueOrderByPriorityAsc(featureFlag.getId()))
                .thenReturn(List.of(featureRule1, featureRule2));

        EvaluationContext evaluationContext = new EvaluationContext();
        evaluationContext.setEnvironment(Environment.PRODUCTION);
        evaluationContext.setVariables(Map.of("userId", "20"));

        EvaluationResult result = evaluationService.evaluate("new_ui", evaluationContext);
        Assertions.assertTrue(result.isEnabled());
        Assertions.assertEquals(EvaluationReason.PERCENTAGE_ROLLOUT, result.getReason());
    }

    @Test
    void shouldDisableWhenNoRuleMatches() {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setId(UUID.randomUUID());
        featureFlag.setKey("new_ui");
        featureFlag.setEnvironment(Environment.PRODUCTION);
        featureFlag.setEnabled(true);
        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION))
                .thenReturn(featureFlag);

        FeatureRule featureRule1 = new FeatureRule();
        featureRule1.setRuleType(RuleType.USER_ID);
        featureRule1.setEnabled(true);
        featureRule1.setRuleValue("123 ");
        featureRule1.setFeatureFlag(featureFlag);
        featureRule1.setPriority(1);

        FeatureRule featureRule2 = new FeatureRule();
        featureRule2.setRuleType(RuleType.PERCENTAGE);
        featureRule2.setEnabled(true);
        featureRule2.setRuleValue("0");
        featureRule2.setFeatureFlag(featureFlag);
        featureRule2.setPriority(2);

        when(featureRuleRepository.findByFeatureFlagIdAndEnabledTrueOrderByPriorityAsc(featureFlag.getId()))
                .thenReturn(List.of(featureRule1, featureRule2));

        EvaluationContext evaluationContext = new EvaluationContext();
        evaluationContext.setEnvironment(Environment.PRODUCTION);
        evaluationContext.setVariables(Map.of("userId", "20"));

        EvaluationResult result = evaluationService.evaluate("new_ui", evaluationContext);
        Assertions.assertFalse(result.isEnabled());
        Assertions.assertEquals(EvaluationReason.NO_RULE_MATCH, result.getReason());
    }
}
