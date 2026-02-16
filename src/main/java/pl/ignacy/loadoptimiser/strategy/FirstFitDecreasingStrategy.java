package pl.ignacy.loadoptimiser.strategy;

import org.springframework.stereotype.Component;
import pl.ignacy.loadoptimiser.entity.Package;
import pl.ignacy.loadoptimiser.entity.Vehicle;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component("firstfit")
public class FirstFitDecreasingStrategy implements LoadOptimiserStrategy{

    @Override
    public Map<Vehicle, List<Package>> calculateLoad(List<Vehicle> vehicles, List<Package> packages) {
        List<Package> sortedPackages = packages.stream()
                .sorted(Comparator.comparingDouble(Package::getWeight).reversed())
                .toList();
        return PackageAssignmentHelper.assignPackagesToVehicles(vehicles, sortedPackages);
    }
}
