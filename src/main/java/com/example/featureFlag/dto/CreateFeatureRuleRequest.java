package com.example.featureFlag.dto;


import com.example.featureFlag.common.Environment;
import com.example.featureFlag.entity.RuleType;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFeatureRuleRequest {
    @NotNull
    private String flagKey;
    @NotNull
    private Environment flagEnvironment;

    @NotNull
    private RuleType ruleType;
    @NotNull
    private String ruleValue;
    @Min(value = 1, message = "Priority Can't be lower than 1")
    private int priority;
    @NotNull
    private boolean enabled;
}
