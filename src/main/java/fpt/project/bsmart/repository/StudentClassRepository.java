package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {

        List<StudentClass> findStudentClassByAccount(Account account) ;
}
