package pl.ignacy.loadoptimiser.strategy;

import pl.ignacy.loadoptimiser.entity.Package;
import pl.ignacy.loadoptimiser.entity.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class PackageAssignmentHelper {
    public static Map<Vehicle, List<Package>> assignPackagesToVehicles(List<Vehicle> vehicles, List<Package> packages) {
        Map<Vehicle, List<Package>> assignments = new HashMap<>();
        for (Vehicle v : vehicles) {
            assignments.put(v, new ArrayList<>());
        }
        for (Package pack : packages) {
            for (Vehicle vehicle : vehicles) {
                double currentWeight = assignments.get(vehicle).stream()
                        .mapToDouble(Package::getWeight)
                        .sum();
                double currentVolume = assignments.get(vehicle).stream()
                        .mapToDouble(Package::getVolume)
                        .sum();

                if (currentWeight + pack.getWeight() <= vehicle.getMaxWeight()
                        && currentVolume + pack.getVolume() <= vehicle.getMaxVolume()) {
                    assignments.get(vehicle).add(pack);
                    break;
                }
            }
        }
        return assignments;
    }
}