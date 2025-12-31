package com.example.featureFlag.controller;

import com.example.featureFlag.dto.CreateFeatureRuleRequest;
import com.example.featureFlag.service.FeatureRuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminFeatureRuleController {

    private final FeatureRuleService featureRuleService;

    AdminFeatureRuleController(FeatureRuleService featureRuleService) {
        this.featureRuleService = featureRuleService;
    }

    @PostMapping("/rules")
    public ResponseEntity<Void> createFeatureRule(@Valid @RequestBody CreateFeatureRuleRequest createFeatureRuleRequest) {
        featureRuleService.saveFeatureRule(createFeatureRuleRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
