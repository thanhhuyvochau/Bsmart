package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.common.ESubjectCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Page<Subject> findAll(Specification<Subject>spec, Pageable pageable);

    Subject findByCode(ESubjectCode code);

    Boolean existsByCode(ESubjectCode code);

    Subject findByCategoryMoodleId(Long idSubjectMoodle);
}
