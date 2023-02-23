package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.AccountDetail;
import fpt.project.bsmart.entity.AccountDetailSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDetailSubjectRepository extends JpaRepository<AccountDetailSubject, Long> {
    List<AccountDetailSubject> findAllByAccountDetail(AccountDetail accountDetail);


}
