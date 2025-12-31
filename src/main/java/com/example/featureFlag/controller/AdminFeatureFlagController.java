package com.example.featureFlag.controller;

import com.example.featureFlag.dto.CreateFeatureFlagRequest;
import com.example.featureFlag.entity.FeatureFlag;
import com.example.featureFlag.repository.FeatureFlagRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminFeatureFlagController {

    private final FeatureFlagRepository featureFlagRepository;

    AdminFeatureFlagController(FeatureFlagRepository featureFlagRepository) {
        this.featureFlagRepository = featureFlagRepository;
    }

    @PostMapping("/flags")
    public ResponseEntity<FeatureFlag> createFeatureFlag(@RequestBody CreateFeatureFlagRequest createFeatureFlagRequest){
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setKey(createFeatureFlagRequest.getFlagKey());
        featureFlag.setDescription(createFeatureFlagRequest.getDescription());
        featureFlag.setEnabled(createFeatureFlagRequest.getEnabled());
        featureFlag.setEnvironment(createFeatureFlagRequest.getEnvironment());

        featureFlagRepository.save(featureFlag);
        return new  ResponseEntity<>(featureFlag, HttpStatus.CREATED);
    }
}
