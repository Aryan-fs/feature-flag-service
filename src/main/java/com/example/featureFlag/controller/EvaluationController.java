package com.example.featureFlag.controller;

import com.example.featureFlag.dto.EvaluationContext;
import com.example.featureFlag.dto.EvaluationRequest;
import com.example.featureFlag.dto.EvaluationResult;
import com.example.featureFlag.service.EvaluationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EvaluationController {

    private EvaluationService evaluationService;

    EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResult> evaluate(@RequestBody EvaluationRequest evaluationRequest) {
        EvaluationContext evaluationContext = new EvaluationContext();
        evaluationContext.setVariables(evaluationRequest.getVariables());
        evaluationContext.setEnvironment(evaluationRequest.getEnvironment());
        EvaluationResult result = evaluationService.evaluate(evaluationRequest.getFlagKey(),evaluationContext);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
