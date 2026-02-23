package pl.ignacy.loadoptimiser.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.ignacy.loadoptimiser.entity.LoadingPlan;

import java.time.LocalDateTime;
import java.util.List;

public interface LoadingPlanRepository extends JpaRepository<LoadingPlan, Long> {
   @EntityGraph(attributePaths = "packages")
    List<LoadingPlan> findAllByCreatedAtBefore(LocalDateTime dateTime);
    @EntityGraph(attributePaths = {"vehicle", "packages"})
    Page<LoadingPlan> findAll(Pageable pageable);
}
