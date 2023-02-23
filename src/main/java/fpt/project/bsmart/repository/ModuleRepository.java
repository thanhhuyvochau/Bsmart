package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.EModuleType;
import fpt.project.bsmart.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module,Long> {
    List<Module> findAllByType(EModuleType eModuleType ) ;
}
