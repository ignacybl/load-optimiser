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
        Map<Vehicle, List<Package>> assignments = new HashMap<>();

        for (Vehicle v : vehicles) {
            assignments.put(v, new ArrayList<>());
        }
        for (Package pack : packages) {
            for (Vehicle vehicle : vehicles) {
                double currentWeight = assignments.get(vehicle).stream()
                        .mapToDouble(Package::getWeight).sum();
                double currentVolume = assignments.get(vehicle).stream()
                        .mapToDouble(Package::getVolume).sum();
                if (currentWeight + pack.getWeight() <= vehicle.getMaxWeight() && currentVolume + pack.getVolume() <= vehicle.getMaxVolume()) {
                    assignments.get(vehicle).add(pack);
                    break;
                }
            }

        }
        return assignments;
    }

}
