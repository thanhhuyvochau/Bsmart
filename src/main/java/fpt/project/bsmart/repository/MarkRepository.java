package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findAllByStudent_AccountAndModule_Section_Clazz(Account student, Class clazz);
}
