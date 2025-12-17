package com.example.featureFlag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "feature_rule", uniqueConstraints = @UniqueConstraint(columnNames = {"feature_flag_id", "priority"}))
@Getter
@Setter
public class FeatureRule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_flag_id", nullable = false)
    private FeatureFlag featureFlag;

    @Enumerated(EnumType.ORDINAL)
    private RuleType ruleType;

    @Column(nullable = false)
    String ruleValue;

    @Column(nullable = false)
    int priority;

    @Column(nullable = false)
    boolean enabled;

    @CreationTimestamp
    Timestamp createdAt;

    @UpdateTimestamp
    Timestamp updatedAt;
}

enum RuleType {
    USER_ID,
    PERCENTAGE,
    ENVIRONMENT,
}
