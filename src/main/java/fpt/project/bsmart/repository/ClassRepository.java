package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
}
