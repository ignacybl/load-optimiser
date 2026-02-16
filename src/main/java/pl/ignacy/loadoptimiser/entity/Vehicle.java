package pl.ignacy.loadoptimiser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.ignacy.loadoptimiser.enums.VehicleType;

@Entity
@Table(name = "vehicles")
@Getter @Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "plate_number", unique = true, nullable = false)
    String plateNumber;
    @Column(name = "max_weight", nullable = false)
    private double maxWeight;
    @Column(name = "max_volume", nullable = false)
    private double maxVolume;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
}
