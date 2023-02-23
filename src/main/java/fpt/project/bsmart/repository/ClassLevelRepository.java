package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ClassLevel;
import fpt.project.bsmart.entity.common.EClassLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClassLevelRepository extends JpaRepository<ClassLevel, Long> {

  Optional<ClassLevel> findByCode(EClassLevel classLevel);


}
