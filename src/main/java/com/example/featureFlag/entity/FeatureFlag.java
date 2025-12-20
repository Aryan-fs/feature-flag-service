package com.example.featureFlag.entity;

import com.example.featureFlag.common.Environment;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name="feature_flags", uniqueConstraints = {@UniqueConstraint(columnNames = {"key", "environment"})})
@Data
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String key;

    private String description;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Environment environment;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    private String createdBy;

}
