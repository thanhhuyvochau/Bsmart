package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.ArchetypeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchetypeTimeRepository extends JpaRepository<ArchetypeTime, Long> {


}
