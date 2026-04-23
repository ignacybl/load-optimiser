package pl.ignacy.loadoptimiser.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import pl.ignacy.load_optimiser_common.dto.LoadingPlanRequest;
import pl.ignacy.load_optimiser_common.dto.LoadingPlanResponse;
import pl.ignacy.loadoptimiser.service.LoadingPlanService;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoadingPlanListener {
    private final LoadingPlanService loadingPlanService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "loading-plan-requests", groupId = "load-optimiser-group")
    public void handleLoadingPlanRequest(LoadingPlanRequest request, Acknowledgment ack) {
        log.info("Processing request: {}", request.strategyType());

        try {
            List<LoadingPlanResponse> responses = loadingPlanService.createPlan(request);

            for (LoadingPlanResponse response : responses) {
                kafkaTemplate.send("loading-plan-results", response).get();
            }
            ack.acknowledge();

        } catch (Exception e) {
            log.error("Error processing message", e);

        }
    }

}