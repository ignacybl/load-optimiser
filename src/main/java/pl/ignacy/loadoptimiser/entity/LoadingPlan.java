package pl.ignacy.loadoptimiser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loading_plans")
@Getter@Setter
public class LoadingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @OneToMany(mappedBy = "loadingPlan", cascade = CascadeType.ALL)
    private List<Package> packages = new ArrayList<>();
    private LocalDateTime createdAt = LocalDateTime.now();
}
