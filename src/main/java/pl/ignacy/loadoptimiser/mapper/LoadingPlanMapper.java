package pl.ignacy.loadoptimiser.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.ignacy.loadoptimiser.dto.LoadingPlanResponse;
import pl.ignacy.loadoptimiser.entity.LoadingPlan;
import pl.ignacy.loadoptimiser.entity.Package;

@Mapper(componentModel = "spring", uses = {PackageMapper.class})
public interface LoadingPlanMapper {

    @Mapping(target = "plateNumber", source = "vehicle.plateNumber")
    @Mapping(target = "vehicleType", source = "vehicle.vehicleType")
    @Mapping(target = "totalWeight", expression = "java(calculateTotalWeight(entity))")
    @Mapping(target = "totalVolume", expression = "java(calculateTotalVolume(entity))")
    @Mapping(target = "weightFillPercentage", expression = "java(calculateWeightFill(entity))")
    @Mapping(target = "volumeFillPercentage", expression = "java(calculateVolumeFill(entity))")
    LoadingPlanResponse toResponse(LoadingPlan entity);

    default double calculateTotalWeight(LoadingPlan entity) {
        if (entity.getPackages() == null) return 0.0;
        return entity.getPackages().stream()
                .mapToDouble(Package::getWeight)
                .sum();
    }

    default double calculateTotalVolume(LoadingPlan entity) {
        if (entity.getPackages() == null) return 0.0;
        return entity.getPackages().stream()
                .mapToDouble(Package::getVolume)
                .sum();
    }

    default double calculateWeightFill(LoadingPlan entity) {
        if (entity.getVehicle() == null || entity.getVehicle().getMaxWeight() <= 0) return 0.0;
        double total = calculateTotalWeight(entity);
        double percentage = (total / entity.getVehicle().getMaxWeight()) * 100;
        return Math.round(percentage * 100.0) / 100.0;
    }

    default double calculateVolumeFill(LoadingPlan entity) {
        if (entity.getVehicle() == null || entity.getVehicle().getMaxVolume() <= 0) return 0.0;
        double total = calculateTotalVolume(entity);
        double percentage = (total / entity.getVehicle().getMaxVolume()) * 100;
        return Math.round(percentage * 100.0) / 100.0;
    }
}
