package pl.ignacy.loadoptimiser.strategy;

import org.springframework.stereotype.Component;
import pl.ignacy.loadoptimiser.entity.Package;
import pl.ignacy.loadoptimiser.entity.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("greedy")
public class GreedyStrategy implements LoadOptimiserStrategy{
    @Override
    public Map<Vehicle, List<Package>> calculateLoad(List<Vehicle> vehicles, List<Package> packages) {
        return PackageAssignmentHelper.assignPackagesToVehicles(vehicles, packages);
    }
}
