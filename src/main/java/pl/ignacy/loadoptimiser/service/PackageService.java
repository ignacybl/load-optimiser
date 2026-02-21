package pl.ignacy.loadoptimiser.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ignacy.loadoptimiser.dto.PackageRequest;
import pl.ignacy.loadoptimiser.dto.PackageResponse;
import pl.ignacy.loadoptimiser.entity.Package;
import pl.ignacy.loadoptimiser.exception.ValidationException;
import pl.ignacy.loadoptimiser.mapper.PackageMapper;
import pl.ignacy.loadoptimiser.repository.PackageRepository;
@Service
@RequiredArgsConstructor
public class PackageService {
    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;

    @Transactional
    public PackageResponse createPackage(PackageRequest packageRequest){
        validatePackage(packageRequest);
        Package pack = packageMapper.toEntity(packageRequest);
        Package savedPack = packageRepository.save(pack);
        return packageMapper.toResponse(savedPack);
    }

    private void validatePackage(PackageRequest PackageRequest) {
        if(PackageRequest.weight() > 1000) {
            throw new ValidationException("Package weight exceeds limit");
        }
        if(PackageRequest.volume() > 10) {
            throw new ValidationException("Package volume exceeds limit");
        }
    }
}
