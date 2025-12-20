package com.example.featureFlag.repository;

import com.example.featureFlag.common.Environment;
import com.example.featureFlag.entity.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, UUID> {
    public FeatureFlag findByKeyAndEnvironment(String key, Environment environment);
}
