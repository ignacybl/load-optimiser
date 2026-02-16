package pl.ignacy.loadoptimiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ignacy.loadoptimiser.entity.LoadingPlan;

public interface LoadingPlanRepository extends JpaRepository<LoadingPlan, Long> {
}
