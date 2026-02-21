package pl.ignacy.loadoptimiser;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.ignacy.loadoptimiser.dto.LoadingPlanRequest;
import pl.ignacy.loadoptimiser.dto.LoadingPlanResponse;
import pl.ignacy.loadoptimiser.entity.LoadingPlan;
import pl.ignacy.loadoptimiser.entity.Package;
import pl.ignacy.loadoptimiser.entity.Vehicle;
import pl.ignacy.loadoptimiser.enums.StrategyType;
import pl.ignacy.loadoptimiser.mapper.LoadingPlanMapper;
import pl.ignacy.loadoptimiser.repository.LoadingPlanRepository;
import pl.ignacy.loadoptimiser.repository.PackageRepository;
import pl.ignacy.loadoptimiser.repository.VehicleRepository;
import pl.ignacy.loadoptimiser.service.LoadingPlanService;
import pl.ignacy.loadoptimiser.strategy.LoadOptimiserStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoadingPlanServiceTest {

    @Mock
    private LoadingPlanRepository loadingPlanRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private PackageRepository packageRepository;
    @Mock
    private LoadingPlanMapper loadingPlanMapper;
    @Mock
    private LoadOptimiserStrategy greedyStrategy;
    @Mock
    private LoadOptimiserStrategy firstFitStrategy;
    @Mock
    private LoadingPlanResponse loadingPlanResponse;

    private LoadingPlanService service;
    private Map<StrategyType, LoadOptimiserStrategy> strategies;

    @BeforeEach
    void setUp() {
        strategies = new HashMap<>();
        strategies.put(StrategyType.GREEDY, greedyStrategy);
        strategies.put(StrategyType.FIRSTFIT, firstFitStrategy);

        service = new LoadingPlanService(loadingPlanRepository, vehicleRepository,
                packageRepository, loadingPlanMapper, strategies);
    }

    @Nested
    class CreatePlan {

        @Test
        void shouldCreateLoadingPlan() {
            Vehicle vehicle = new Vehicle();
            vehicle.setId(1L);
            Package pack = new Package();
            pack.setId(1L);

            when(vehicleRepository.findAllById(any())).thenReturn(List.of(vehicle));
            when(packageRepository.findAllById(any())).thenReturn(List.of(pack));
            when(greedyStrategy.calculateLoad(any(), any())).thenReturn(Map.of(vehicle, List.of(pack)));
            when(loadingPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(loadingPlanMapper.toResponse(any())).thenReturn(loadingPlanResponse);

            var result = service.createPlan(new LoadingPlanRequest(List.of(1L), List.of(1L), StrategyType.GREEDY));

            assertThat(result).hasSize(1);
        }

        @Test
        void shouldThrowWhenVehicleMissing(){

            when(vehicleRepository.findAllById(any())).thenReturn(List.of());
            LoadingPlanRequest request = new LoadingPlanRequest(List.of(1L), List.of(), StrategyType.GREEDY);
            assertThatThrownBy(() -> service.createPlan(request))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void shouldUseGreedyWhenStrategyNull() {
            Vehicle vehicle = new Vehicle();
            Package pack = new Package();

            when(vehicleRepository.findAllById(any())).thenReturn(List.of(vehicle));
            when(packageRepository.findAllById(any())).thenReturn(List.of(pack));
            when(greedyStrategy.calculateLoad(any(), any())).thenReturn(Map.of(vehicle, List.of(pack)));
            when(loadingPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(loadingPlanMapper.toResponse(any())).thenReturn(loadingPlanResponse);

            service.createPlan(new LoadingPlanRequest(List.of(1L), List.of(1L), null));

            verify(greedyStrategy).calculateLoad(any(), any());
        }

        @Test
        void shouldReturnEmptyWhenAllPackagesAssigned() {
            Package pack = new Package();
            pack.setLoadingPlan(new LoadingPlan());

            when(packageRepository.findAllById(any())).thenReturn(List.of(pack));

            List<LoadingPlanResponse> result = service.createPlan(new LoadingPlanRequest(List.of(), List.of(1L), StrategyType.GREEDY));

            assertThat(result).isEmpty();
            verify(greedyStrategy, never()).calculateLoad(any(), any());
        }

        @Test
        void shouldReturnEmptyWhenStrategyReturnsEmptyMap() {
            when(vehicleRepository.findAllById(any())).thenReturn(List.of(new Vehicle()));
            when(packageRepository.findAllById(any())).thenReturn(List.of(new Package()));
            when(greedyStrategy.calculateLoad(any(), any())).thenReturn(Map.of());

            List<LoadingPlanResponse> result = service.createPlan(new LoadingPlanRequest(List.of(1L), List.of(1L), StrategyType.GREEDY));

            assertThat(result).isEmpty();
        }

        @Test
        void shouldCreateMultiplePlans() {
            Vehicle v1 = new Vehicle();
            Vehicle v2 = new Vehicle();
            Package p1 = new Package();
            Package p2 = new Package();

            when(vehicleRepository.findAllById(any())).thenReturn(List.of(v1, v2));
            when(packageRepository.findAllById(any())).thenReturn(List.of(p1, p2));
            when(greedyStrategy.calculateLoad(any(), any())).thenReturn(Map.of(v1, List.of(p1), v2, List.of(p2)));
            when(loadingPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(loadingPlanMapper.toResponse(any())).thenReturn(loadingPlanResponse);

            List<LoadingPlanResponse> result = service.createPlan(new LoadingPlanRequest(List.of(1L, 2L), List.of(1L, 2L), StrategyType.GREEDY));

            assertThat(result).hasSize(2);
            verify(loadingPlanRepository, times(2)).save(any());
        }

        @Test
        void shouldAssignPlanToPackages() {
            Vehicle vehicle = new Vehicle();
            Package pack = new Package();

            when(vehicleRepository.findAllById(any())).thenReturn(List.of(vehicle));
            when(packageRepository.findAllById(any())).thenReturn(List.of(pack));
            when(greedyStrategy.calculateLoad(any(), any())).thenReturn(Map.of(vehicle, List.of(pack)));
            when(loadingPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(loadingPlanMapper.toResponse(any())).thenReturn(loadingPlanResponse);

            service.createPlan(new LoadingPlanRequest(List.of(1L), List.of(1L), StrategyType.GREEDY));

            ArgumentCaptor<LoadingPlan> planCaptor = ArgumentCaptor.forClass(LoadingPlan.class);
            verify(loadingPlanRepository).save(planCaptor.capture());
            assertThat(pack.getLoadingPlan()).isEqualTo(planCaptor.getValue());
        }
    }

    @Nested
    class GetPlanById {

        @Test
        void shouldReturnPlanIfExists() {
            LoadingPlan plan = new LoadingPlan();
            plan.setId(1L);

            when(loadingPlanRepository.findById(1L)).thenReturn(Optional.of(plan));

            when(loadingPlanResponse.id()).thenReturn(1L);

            when(loadingPlanMapper.toResponse(plan)).thenReturn(loadingPlanResponse);

            var result = service.getPlanById(1L);

            assertThat(result.id()).isEqualTo(1L);
        }

        @Test
        void shouldThrowWhenPlanNotFound() {
            when(loadingPlanRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getPlanById(1L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("does not exist");
        }
    }

    @Nested
    class GetAllPlans {

        @Test
        void shouldReturnMappedPage() {
            LoadingPlan plan = new LoadingPlan();
            Page<LoadingPlan> page = new PageImpl<>(List.of(plan));

            when(loadingPlanRepository.findAll(any(PageRequest.class))).thenReturn(page);
            when(loadingPlanMapper.toResponse(plan)).thenReturn(loadingPlanResponse);

            Pageable pageable = PageRequest.of(0,10);
            Page<LoadingPlanResponse> result = service.getAllPlans(pageable);

            assertThat(result.getContent()).hasSize(1);
            verify(loadingPlanRepository).findAll(PageRequest.of(0, 10));
        }
    }
}