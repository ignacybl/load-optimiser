package pl.ignacy.loadoptimiser.mapper;

import org.mapstruct.Mapper;
import pl.ignacy.loadoptimiser.dto.PackageRequest;
import pl.ignacy.loadoptimiser.dto.PackageResponse;
import pl.ignacy.loadoptimiser.entity.Package;

@Mapper(componentModel = "spring")
public interface PackageMapper {
    Package toEntity(PackageRequest packageRequest);
    PackageResponse toResponse(Package pack);
}
