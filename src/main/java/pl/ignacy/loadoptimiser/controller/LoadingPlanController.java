package pl.ignacy.loadoptimiser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ignacy.loadoptimiser.dto.LoadingPlanRequest;
import pl.ignacy.loadoptimiser.dto.LoadingPlanResponse;
import pl.ignacy.loadoptimiser.service.LoadingPlanService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoadingPlanController {

    private final LoadingPlanService loadingPlanService;
    @PostMapping("/plan-loads")
    public ResponseEntity<List<LoadingPlanResponse>> createPlan(@Valid @RequestBody LoadingPlanRequest request) {
        List<LoadingPlanResponse> createdPlans = loadingPlanService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlans);
    }

    @GetMapping("/plan-loads")
    public ResponseEntity<Page<LoadingPlanResponse>> getPlans(){
        return ResponseEntity.ok(loadingPlanService.getAllPlans());
    }

    @GetMapping("/plan-loads/{id}")
    public ResponseEntity<LoadingPlanResponse> getPlan(@PathVariable Long id){
        return ResponseEntity.ok(loadingPlanService.getPlanById(id));
    }


}
