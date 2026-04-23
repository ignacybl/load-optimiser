package pl.ignacy.loadoptimiser.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.ignacy.load_optimiser_common.enums.PackageCategory;
import pl.ignacy.load_optimiser_common.enums.Priority;

@Entity
@Table(name = "packages")
@Getter@Setter
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Version
    private Long version;
    @Column(nullable = false)
    private double length;
    @Column(nullable = false)
    private double width;
    @Column(nullable = false)
    private double height;
    @Column(nullable = false)
    private double weight;
    @Column(nullable = false)
    private double volume;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
    @Column(name= "delivery_address")
    private String deliveryAddress;

    private boolean fragile;
    @Column(nullable = false)
    private double weightCapacity;

    @Enumerated(EnumType.STRING)
    private PackageCategory packageCategory;
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private LoadingPlan loadingPlan;
}
