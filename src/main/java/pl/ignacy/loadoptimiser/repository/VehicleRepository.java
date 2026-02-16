package pl.ignacy.loadoptimiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ignacy.loadoptimiser.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByPlateNumber(String plateNumber);
}
