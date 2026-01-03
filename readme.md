# Feature Flag Service

A backend **feature flag management service** built with **Spring Boot**, supporting **rule-based evaluation**, **priority ordering**, and **TTL-based caching** for high-performance reads.

---

## 🚀 Features

- Create and manage feature flags per environment
- Rule-based evaluation with **priority**
- Supported rule types:
    - **User ID allowlist**
    - **Percentage rollout (deterministic hashing)**
    - **Environment-based rules**
- Deterministic evaluation (same input → same result)
- **In-memory caching (Caffeine)** with configurable TTL
- Global exception handling
- Fully unit-tested core logic

---

## 🧠 How Feature Evaluation Works

1. A feature flag is fetched using `(flagKey, environment)`
2. If the flag is disabled → evaluation stops
3. Enabled rules are fetched **ordered by priority**
4. Rules are evaluated one by one:
    - First matching rule decides the result
5. If no rule matches → feature is disabled
6. Evaluation results are cached for **60 seconds**

---

## 🧩 Caching Strategy

- **Cache key**:  
  `(flagKey, environment, userId | "__anonymous__")`
- **Cache TTL**: 60 seconds
- **Cache scope**: In-memory (Caffeine)
- **Consistency model**: Eventual consistency (acceptable for feature flags)

**Design decision**  
Rule updates may take up to 60 seconds to reflect, in exchange for reduced load and simpler invalidation logic.

---

## 🛠 Tech Stack

- Java 22
- Spring Boot
- Spring Data JPA
- Spring Cache + Caffeine
- PostgreSQL
- Mockito + JUnit 5
- Maven

---

## 📦 API Overview

### Evaluate a Feature Flag

**POST** `/api/evaluate`

```json
{
  "flagKey": "new_ui",
  "environment": "PRODUCTION",
  "variables": {
    "userId": "123"
  }
}
```

**RESPONSE**

```json
{
  "enabled": true,
  "reason": "PERCENTAGE_ROLLOUT"
}
```

---

### Create Feature Flag (Admin)
**POST** `/api/admin/flags`
```json
{
  "flagKey": "new_ui",
  "environment": "PRODUCTION",
  "enabled": true,
  "description": "New dashboard UI"
}
```

---

### Create Feature Rule (Admin)
**POST** `/api/admin/rules`
```json
{
  "flagKey": "new_ui",
  "flagEnvironment": "PRODUCTION",
  "ruleType": "PERCENTAGE",
  "ruleValue": "30",
  "priority": 2,
  "enabled": true
}
```

---

## 🧪 Testing

**Unit tests covered:**

- Flag not found
- Disabled flags
- Rule priority handling
- User ID rules
- Percentage rollout
- Invalid rule validation

Mockito is used to isolate service-layer logic.

---

## 🧠 Design Decisions

- Service layer handles business logic, controllers remain thin
- Database constraints are preferred over pre-checks where possible
- TTL caching chosen over explicit invalidation to reduce complexity
- Cache is ephemeral and rebuilt after restart
- Evaluation logic is deterministic ensuring no random flag flip

---

## 🏗️ Architecture & Flow

The system is designed as a lightweight feature flag evaluation engine with deterministic behavior and low latency.

### High-Level Components

- **Controller Layer**
    - Exposes REST APIs for evaluation and admin operations
    - Keeps request handling thin and delegates logic to services

- **Service Layer**
    - Core business logic lives here
    - Handles rule evaluation, priority ordering, validation, and caching
    - Designed to be deterministic

- **Repository Layer**
    - JPA repositories for FeatureFlag and FeatureRule
    - Relies on database constraints for data integrity

- **Cache Layer**
    - Caffeine-based in-memory cache
    - TTL-based eviction to avoid complex invalidation logic

---

### Evaluation Flow

1. Client calls `/api/evaluate`
2. Cache is checked using key:

   flagKey | environment | userId
3. If cache hit → return cached result
4. If cache miss:
- Fetch feature flag
- Validate flag status
- Fetch enabled rules ordered by priority
- Evaluate rules sequentially
5. First matching rule determines the result
6. Successful evaluations are cached with TTL

---

### Rule Evaluation Strategy

Rules are evaluated in ascending priority order:

1. **USER_ID**
- Explicit allow-list for specific users
2. **PERCENTAGE**
- Deterministic hashing using `(flagKey + userId)`
- Ensures consistent rollout per user
3. **ENVIRONMENT**
- Enables flag for entire environments

---

### Caching Strategy

- Only successful evaluations are cached
- TTL: 60 seconds
- No explicit invalidation
- Cache rebuilds naturally after restart

This design favors simplicity and predictability while remaining performant.

---

## 📌 Future Improvements

- Distributed cache (Redis) for multi-instance deployments
- Rule audit history
- Async cache warming
- Admin UI
- Feature flag dependency graphs

---

## 👤 Author

Built by **Aryan Singh**

Mail: **a3ryn33@gmail.com**
