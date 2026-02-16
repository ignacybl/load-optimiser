package pl.ignacy.loadoptimiser.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.ignacy.loadoptimiser.dto.LoadingPlanRequest;
import pl.ignacy.loadoptimiser.dto.LoadingPlanResponse;
import pl.ignacy.loadoptimiser.entity.LoadingPlan;
import pl.ignacy.loadoptimiser.entity.Package;
import pl.ignacy.loadoptimiser.entity.Vehicle;
import pl.ignacy.loadoptimiser.mapper.LoadingPlanMapper;
import pl.ignacy.loadoptimiser.repository.LoadingPlanRepository;
import pl.ignacy.loadoptimiser.repository.PackageRepository;
import pl.ignacy.loadoptimiser.repository.VehicleRepository;
import pl.ignacy.loadoptimiser.strategy.LoadOptimiserStrategy;

import java.util.ArrayList;
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
    private final Map<String, LoadOptimiserStrategy> strategies;

    @Transactional
    public List<LoadingPlanResponse> createPlan(LoadingPlanRequest request){

        String type = request.strategyType() != null ? request.strategyType() : "greedy";
        LoadOptimiserStrategy strategy = strategies.get(type);

        if(strategy == null){
            log.warn("UNKNOWN STRATEGY");
            throw new IllegalArgumentException("Unknown strategy:  " + type);
        }

        List<Vehicle> vehicles = vehicleRepository.findAllById(request.vehicleIds());
        List<Package> availablePackages = packageRepository.findAllById(request.packagesIds())
                .stream().filter(pkg -> pkg.getLoadingPlan() == null).toList();

        if(availablePackages.isEmpty()) return List.of();

        if(availablePackages.size() < request.packagesIds().size()){
            log.warn("SOME PACKAGES MIGHT HAVE BEEN SKIPPED:)");
        }

        Map<Vehicle, List<Package>> result = strategy.calculateLoad(vehicles, availablePackages);

        List<LoadingPlan> savedPlans = new ArrayList<>();
        for(Map.Entry<Vehicle, List<Package>> entry : result.entrySet()){
            Vehicle vehicle = entry.getKey();
            List<Package> assignedPackages =  entry.getValue();
            if(assignedPackages.isEmpty()) continue;

            LoadingPlan plan = new LoadingPlan();
            plan.setVehicle(vehicle);

            for(Package pack : assignedPackages){
                pack.setLoadingPlan(plan);
            }
            plan.setPackages(new ArrayList<>(assignedPackages));

            savedPlans.add(loadingPlanRepository.save(plan));
        }
        return savedPlans.stream()
                .map(loadingPlanMapper::toResponse)
                .toList();
    }
}
