package pl.ignacy.loadoptimiser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.ignacy.loadoptimiser.enums.Priority;

@Entity
@Table(name = "packages")
@Getter@Setter
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private double weight;
    @Column(nullable = false)
    private double volume;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
    @Column(name= "delivery_address")
    private String deliveryAddress;
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private LoadingPlan loadingPlan;
}
