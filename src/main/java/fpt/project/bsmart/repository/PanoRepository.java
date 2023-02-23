package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Pano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PanoRepository extends JpaRepository<Pano, Long> {
    boolean existsByName(String name);

}
