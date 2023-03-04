package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Category;
import fpt.project.bsmart.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
