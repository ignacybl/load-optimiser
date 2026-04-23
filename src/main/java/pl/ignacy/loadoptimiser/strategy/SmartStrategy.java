package pl.ignacy.loadoptimiser.strategy;

import org.springframework.stereotype.Component;
import pl.ignacy.load_optimiser_common.enums.StrategyType;
import pl.ignacy.loadoptimiser.entity.Package;
import pl.ignacy.loadoptimiser.entity.Vehicle;

import java.util.*;

@Component
public class SmartStrategy implements LoadOptimiserStrategy{
    @Override
    public StrategyType getType() {
        return StrategyType.SMART;
    }

    @Override
    public Map<Vehicle, List<Package>> calculateLoad(List<Vehicle> vehicles, List<Package> packages) {
        Map<Vehicle, Map<Long,Package>> preResult = new HashMap<>();
        for(Vehicle vehicle: vehicles){
            preResult.put(vehicle, new HashMap<>());
        }
        packages.sort(Comparator.comparing(Package::isFragile).thenComparing(Package::getWeight).reversed().thenComparing(Package::getPriority));


        for(Package pkg : packages){
            Vehicle bestVehicle = findBestVehicle(pkg, vehicles, preResult);

            if(bestVehicle!= null){
                preResult.get(bestVehicle).putIfAbsent(pkg.getId(), pkg);
            }
        }
        Map<Vehicle, List<Package>> result = new HashMap<>();
        for(Vehicle v : vehicles){
            result.put(v, new ArrayList<>(preResult.get(v).values()));
        }
        return result;
    }
    private Vehicle findBestVehicle(Package pkg, List<Vehicle> vehicles, Map<Vehicle, Map<Long, Package>> currentLoad){
        Vehicle best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for(Vehicle v : vehicles){
            if(!fitsDimensionally(pkg, v)) continue;
            if(!fitsWeight(pkg, v, currentLoad.get(v).values()))continue;
            if(!fitsFragile(pkg, currentLoad.get(v).values()))continue;

            double score = scoreVehicle(pkg, v, currentLoad.get(v).values());

            if(score>bestScore){
                bestScore = score;
                best = v;
            }
        }
        return best;
    }
    private boolean fitsDimensionally(Package pkg, Vehicle v){

        return pkg.getLength() <= v.getLength() && pkg.getWidth() <= v.getWidth()
                && pkg.getHeight() <= v.getHeight();
    }
    private boolean fitsWeight(Package pkg, Vehicle v, Collection<Package> alreadyPacked){
        double currentWeight = alreadyPacked.stream().mapToDouble(Package::getWeight).sum();
        return currentWeight + pkg.getWeight() <= v.getMaxWeight();
    }
    private boolean fitsFragile(Package pkg, Collection<Package> alreadyPacked){
        if(pkg.getWeight() > 50){
            boolean hasFragile = alreadyPacked.stream().anyMatch(Package::isFragile);
            if(hasFragile) return false;
        }
        return true;
    }
    private double scoreVehicle(Package pkg, Vehicle v, Collection<Package> alreadyPacked){
        double score = 0;

        double currentVolume = alreadyPacked.stream().mapToDouble(Package::getVolume).sum();
        double volumeEfficiency = currentVolume/v.getMaxVolume();
        score += volumeEfficiency * 10;

        return score;
    }
}
