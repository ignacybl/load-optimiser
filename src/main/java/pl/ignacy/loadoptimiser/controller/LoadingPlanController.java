package pl.ignacy.loadoptimiser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ignacy.loadoptimiser.dto.LoadingPlanRequest;
import pl.ignacy.loadoptimiser.dto.LoadingPlanResponse;
import pl.ignacy.loadoptimiser.service.LoadingPlanService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoadingPlanController {

    private final LoadingPlanService loadingPlanService;

    @PostMapping("/plan-load")
    public ResponseEntity<List<LoadingPlanResponse>> createPlan(@Valid @RequestBody LoadingPlanRequest request) {
        List<LoadingPlanResponse> createdPlans = loadingPlanService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlans);
    }

}
