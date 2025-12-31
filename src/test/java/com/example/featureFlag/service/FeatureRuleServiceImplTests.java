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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeatureRuleServiceImplTests {

    @Mock
    private FeatureRuleRepository featureRuleRepository;
    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @InjectMocks
    private FeatureRuleServiceImpl featureRuleService;

    @Test
    public void shouldThrowErrorWhenFeatureFlagNotFound() {
        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION)).
                thenReturn(null);

        CreateFeatureRuleRequest createFeatureRuleRequest = new CreateFeatureRuleRequest();
        createFeatureRuleRequest.setFlagKey("new_ui");
        createFeatureRuleRequest.setFlagEnvironment(Environment.PRODUCTION);
        createFeatureRuleRequest.setRuleType(RuleType.USER_ID);
        createFeatureRuleRequest.setRuleValue("123");
        createFeatureRuleRequest.setPriority(1);
        createFeatureRuleRequest.setEnabled(true);

        assertThrows(FeatureFlagNotFoundException.class,() -> featureRuleService.saveFeatureRule(createFeatureRuleRequest));
        verify(featureRuleRepository, times(0)).save(any());
    }

    @Test
    public void shouldThrowErrorWhenInvalidUserIdRuleValue() {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setKey("new_ui");
        featureFlag.setEnvironment(Environment.PRODUCTION);

        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION)).
                thenReturn(featureFlag);

        CreateFeatureRuleRequest createFeatureRuleRequest = new CreateFeatureRuleRequest();
        createFeatureRuleRequest.setFlagKey("new_ui");
        createFeatureRuleRequest.setFlagEnvironment(Environment.PRODUCTION);
        createFeatureRuleRequest.setRuleType(RuleType.USER_ID);
        createFeatureRuleRequest.setRuleValue(", 123");
        createFeatureRuleRequest.setPriority(1);
        createFeatureRuleRequest.setEnabled(true);

        assertThrows(InvalidRuleValueException.class, () -> featureRuleService.saveFeatureRule(createFeatureRuleRequest));
        verify(featureRuleRepository, times(0)).save(any());
    }

    @Test
    public void shouldThrowErrorWhenInvalidPercentageRuleValue() {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setKey("new_ui");
        featureFlag.setEnvironment(Environment.PRODUCTION);

        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION)).
                thenReturn(featureFlag);

        CreateFeatureRuleRequest createFeatureRuleRequest = new CreateFeatureRuleRequest();
        createFeatureRuleRequest.setFlagKey("new_ui");
        createFeatureRuleRequest.setFlagEnvironment(Environment.PRODUCTION);
        createFeatureRuleRequest.setRuleType(RuleType.PERCENTAGE);
        createFeatureRuleRequest.setRuleValue("-13");
        createFeatureRuleRequest.setPriority(1);
        createFeatureRuleRequest.setEnabled(true);

        assertThrows(InvalidRuleValueException.class, () -> featureRuleService.saveFeatureRule(createFeatureRuleRequest));
        verify(featureRuleRepository, times(0)).save(any());
    }

    @Test
    public void shouldSaveWhenValidFeatureFlagAndValidUserRule() {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setKey("new_ui");
        featureFlag.setEnvironment(Environment.PRODUCTION);

        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION)).
                thenReturn(featureFlag);

        CreateFeatureRuleRequest createFeatureRuleRequest = new CreateFeatureRuleRequest();
        createFeatureRuleRequest.setFlagKey("new_ui");
        createFeatureRuleRequest.setFlagEnvironment(Environment.PRODUCTION);
        createFeatureRuleRequest.setRuleType(RuleType.USER_ID);
        createFeatureRuleRequest.setRuleValue("123, 456, 789 ");
        createFeatureRuleRequest.setPriority(1);
        createFeatureRuleRequest.setEnabled(true);
        featureRuleService.saveFeatureRule(createFeatureRuleRequest);

        verify(featureRuleRepository, times(1)).save(any());
    }

    @Test
    public void shouldSaveWhenValidFeatureFlagAndValidPercentageRule() {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setKey("new_ui");
        featureFlag.setEnvironment(Environment.PRODUCTION);

        when(featureFlagRepository.findByKeyAndEnvironment("new_ui", Environment.PRODUCTION)).
                thenReturn(featureFlag);

        CreateFeatureRuleRequest createFeatureRuleRequest = new CreateFeatureRuleRequest();
        createFeatureRuleRequest.setFlagKey("new_ui");
        createFeatureRuleRequest.setFlagEnvironment(Environment.PRODUCTION);
        createFeatureRuleRequest.setRuleType(RuleType.PERCENTAGE);
        createFeatureRuleRequest.setRuleValue("25");
        createFeatureRuleRequest.setPriority(1);
        createFeatureRuleRequest.setEnabled(true);
        featureRuleService.saveFeatureRule(createFeatureRuleRequest);

        verify(featureRuleRepository, times(1)).save(any());
    }
}
