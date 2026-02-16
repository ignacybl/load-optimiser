package pl.ignacy.loadoptimiser.mapper;

import org.mapstruct.Mapper;
import pl.ignacy.loadoptimiser.dto.VehicleRequest;
import pl.ignacy.loadoptimiser.dto.VehicleResponse;
import pl.ignacy.loadoptimiser.entity.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    Vehicle toEntity(VehicleRequest vehicleRequest);
    VehicleResponse toResponse(Vehicle vehicle);
}
