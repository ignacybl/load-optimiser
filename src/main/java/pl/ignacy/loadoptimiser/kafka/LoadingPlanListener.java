package pl.ignacy.loadoptimiser.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import pl.ignacy.loadoptimiser.dto.LoadingPlanRequest;
import pl.ignacy.loadoptimiser.dto.LoadingPlanResponse;
import pl.ignacy.loadoptimiser.service.LoadingPlanService;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoadingPlanListener {
    private final LoadingPlanService loadingPlanService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "loading-plan-requests", groupId = "load-optimiser-group")
    public void handleLoadingPlanRequest(LoadingPlanRequest loadingPlanRequest){
        log.info("New request by Kafka. Strategy: {}", loadingPlanRequest.strategyType());

        List<LoadingPlanResponse> responses = loadingPlanService.createPlan(loadingPlanRequest);
        for(LoadingPlanResponse response: responses) {
            kafkaTemplate.send("loading-plan-results", response);
        }
        log.info("Plan has been created and sent on topic - results");

    }
    @DltHandler
    public void handleDlt(LoadingPlanRequest data, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("Data from topic: {} has been transferred to DLT: {}", topic, data);
    }
}
