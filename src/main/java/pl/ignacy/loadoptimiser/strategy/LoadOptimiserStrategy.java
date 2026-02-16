package pl.ignacy.loadoptimiser.strategy;

import pl.ignacy.loadoptimiser.entity.Package;
import pl.ignacy.loadoptimiser.entity.Vehicle;

import java.util.List;
import java.util.Map;

public interface LoadOptimiserStrategy {
    Map<Vehicle, List<Package>> calculateLoad(List<Vehicle> vehicles, List<Package> packages);
}
