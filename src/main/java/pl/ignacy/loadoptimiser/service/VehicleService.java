package pl.ignacy.loadoptimiser.service;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ignacy.loadoptimiser.dto.VehicleRequest;
import pl.ignacy.loadoptimiser.dto.VehicleResponse;
import pl.ignacy.loadoptimiser.entity.Vehicle;
import pl.ignacy.loadoptimiser.exception.ValidationException;
import pl.ignacy.loadoptimiser.mapper.VehicleMapper;
import pl.ignacy.loadoptimiser.repository.VehicleRepository;

@RequiredArgsConstructor
@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponse createVehicle(VehicleRequest vehicleRequest){
        validatePlate(vehicleRequest);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleRequest);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(savedVehicle);
    }


    private void validatePlate(VehicleRequest vehicleRequest){
        if(vehicleRepository.existsByPlateNumber(vehicleRequest.plateNumber())){
            throw new ValidationException("Plate number already exists");
        }
    }
}
