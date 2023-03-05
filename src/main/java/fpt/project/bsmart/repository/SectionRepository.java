package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
