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
}
