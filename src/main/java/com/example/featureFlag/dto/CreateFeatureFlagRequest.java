package com.example.featureFlag.dto;

import com.example.featureFlag.common.Environment;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFeatureFlagRequest {
    @NotNull
    private String flagKey;
    private String description;

    @NotNull
    private Environment environment;

    @NotNull
    private Boolean enabled;
}
