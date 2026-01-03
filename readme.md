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