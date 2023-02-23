package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.Archetype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchetypeRepository extends JpaRepository<Archetype, Long> {

    boolean existsByIdAndCode(Long id, String code);

    boolean existsByCode(String code);

    List<Archetype> findByCreatedByTeacherId(Long teacherId);
}
