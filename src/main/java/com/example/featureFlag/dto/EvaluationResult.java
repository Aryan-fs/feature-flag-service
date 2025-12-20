package com.example.featureFlag.dto;

import com.example.featureFlag.service.EvaluationReason;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationResult {
    private boolean enabled;
    private EvaluationReason reason;
}