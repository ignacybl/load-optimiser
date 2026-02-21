package pl.ignacy.loadoptimiser.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
import pl.ignacy.loadoptimiser.strategy.LoadOptimiserStrategy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoadingPlanService {
    private final LoadingPlanRepository loadingPlanRepository;
    private final VehicleRepository vehicleRepository;
    private final PackageRepository packageRepository;
    private final LoadingPlanMapper loadingPlanMapper;
    private final Map<StrategyType, LoadOptimiserStrategy> strategies;


    @Transactional
    public List<LoadingPlanResponse> createPlan(LoadingPlanRequest request){
        LoadOptimiserStrategy strategy = fetchStrategy(request.strategyType());
        List<Vehicle> vehicles = fetchVehicles(request.vehicleIds());
        List<Package> packages = fetchPackages(request.packagesIds());
        if(packages.isEmpty())return List.of();

        Map<Vehicle, List<Package>> result = strategy.calculateLoad(vehicles, packages);

        List<LoadingPlan> savedPlans = savePlans(result);

        return savedPlans.stream()
                .map(loadingPlanMapper::toResponse)
                .toList();
    }
    public LoadingPlanResponse getPlanById(Long id){
        return loadingPlanRepository.findById(id).map(loadingPlanMapper::toResponse).orElseThrow(() -> new EntityNotFoundException("Plan " + id + " does not exist"));
    }
    public Page<LoadingPlanResponse> getAllPlans(Pageable pageable){
        return loadingPlanRepository.findAll(pageable).map(loadingPlanMapper::toResponse);
    }

    private LoadOptimiserStrategy fetchStrategy(StrategyType type){
        StrategyType strategyType = type != null ? type : StrategyType.GREEDY;
        LoadOptimiserStrategy strategy = strategies.get(strategyType);

        if(strategy == null){
            log.warn("UNKNOWN STRATEGY");
            throw new IllegalArgumentException("Unknown strategy:  " + strategyType);
        }
        return strategy;
    }

    private List<Vehicle> fetchVehicles(List<Long> vehiclesId){
        List<Vehicle> vehicles = vehicleRepository.findAllById(vehiclesId);
        if(vehiclesId.size() != vehicles.size()){
            throw new EntityNotFoundException("One or more vehicles not found");
        }
        return vehicles;
    }

    private List<Package> fetchPackages(List<Long> packageIds){
        List<Package> packages = packageRepository.findAllById(packageIds);

        List<Package> available = packages.stream()
                .filter(pkg -> pkg.getLoadingPlan() == null)
                .toList();

        if(available.isEmpty()){
            log.warn("ALL PACKAGES HAVE ALREADY BEEN PACKED");
            return List.of();
        }
        if(available.size() != packages.size()){
            log.warn("SOME PACKAGES MIGHT HAVE BEEN SKIPPED");
        }
        return available;
    }
    private List<LoadingPlan> savePlans(Map<Vehicle, List<Package>> result){
        List<LoadingPlan> savedPlans = new ArrayList<>();
        for(Map.Entry<Vehicle, List<Package>> entry : result.entrySet()) {
            Vehicle vehicle = entry.getKey();
            List<Package> assignedPackages = entry.getValue();
            if (assignedPackages.isEmpty()) continue;

            LoadingPlan plan = new LoadingPlan();
            plan.setVehicle(vehicle);

            for (Package pack : assignedPackages) {
                pack.setLoadingPlan(plan);
            }
            plan.setPackages(new ArrayList<>(assignedPackages));

            savedPlans.add(loadingPlanRepository.save(plan));
        }
        return savedPlans;
    }
}
