package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.InfoFindTutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoFindTutorRepository extends JpaRepository<InfoFindTutor,Long> {

}
