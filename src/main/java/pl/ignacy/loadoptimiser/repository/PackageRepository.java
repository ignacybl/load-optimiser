package pl.ignacy.loadoptimiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ignacy.loadoptimiser.entity.Package;

public interface PackageRepository extends JpaRepository <Package, Long>{
}
