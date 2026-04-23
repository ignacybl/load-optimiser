package pl.ignacy.loadoptimiser.mapper;

import org.mapstruct.Mapper;
import pl.ignacy.load_optimiser_common.dto.PackageRequest;
import pl.ignacy.load_optimiser_common.dto.PackageResponse;
import pl.ignacy.loadoptimiser.entity.Package;

@Mapper(componentModel = "spring")
public interface PackageMapper {
    Package toEntity(PackageRequest packageRequest);
    PackageResponse toResponse(Package pack);
}
