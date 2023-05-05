package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    Page<Class> findAll (Specification<Class> builder, Pageable pageable);
}
