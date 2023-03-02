package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Module;
import fpt.project.bsmart.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
}
