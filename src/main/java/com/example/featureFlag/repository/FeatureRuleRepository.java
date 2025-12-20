package com.example.featureFlag.repository;

import com.example.featureFlag.entity.FeatureRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeatureRuleRepository extends JpaRepository<FeatureRule, UUID> {
    List<FeatureRule> findByFeatureFlagIdAndEnabledTrueOrderByPriorityAsc(UUID featureFlagId);
}
