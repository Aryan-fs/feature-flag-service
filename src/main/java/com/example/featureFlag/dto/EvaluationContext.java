package com.example.featureFlag.dto;

import com.example.featureFlag.common.Environment;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EvaluationContext {
    private Environment environment;
    private Map<String,?> variables;
}
