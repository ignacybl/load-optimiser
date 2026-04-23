package pl.ignacy.loadoptimiser.strategy;

import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.springframework.stereotype.Component;
import pl.ignacy.load_optimiser_common.enums.StrategyType;
import pl.ignacy.loadoptimiser.entity.Package;
import pl.ignacy.loadoptimiser.entity.Vehicle;

import java.util.List;
import java.util.Map;

@Component
public class GreedyStrategy implements LoadOptimiserStrategy{
    @Override
    public StrategyType getType() {
        return StrategyType.GREEDY;
    }

    @Override
    public Map<Vehicle, List<Package>> calculateLoad(List<Vehicle> vehicles, List<Package> packages) {
        return PackageAssignmentHelper.assignPackagesToVehicles(vehicles, packages);
    }
}
