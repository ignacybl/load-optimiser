package pl.ignacy.loadoptimiser.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.ignacy.loadoptimiser.entity.LoadingPlan;
import pl.ignacy.loadoptimiser.repository.LoadingPlanRepository;
import pl.ignacy.loadoptimiser.repository.PackageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MaintenanceService {

    private final LoadingPlanRepository loadingPlanRepository;
    private final PackageRepository packageRepository;

    @Scheduled(cron = "0 * * * * *")
    @SchedulerLock(name = "archiveOldPlansTask", lockAtLeastFor = "PT1M", lockAtMostFor = "PT5M")
    @Transactional
    public void archiveOldPlans(){
        LocalDateTime timeForArchive = LocalDateTime.now().minusDays(30);
        List<LoadingPlan> oldPlans = loadingPlanRepository.findAllByCreatedAtBefore(timeForArchive);

        for(LoadingPlan plan: oldPlans){
            plan.getPackages().forEach(p -> p.setLoadingPlan(null));

            loadingPlanRepository.delete(plan);
        }
        log.info("{} plans have been deleted", oldPlans.size());
    }
}