package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountDetailClassLevelRepository extends JpaRepository<AccountDetailClassLevel, Long> {
    AccountDetailClassLevel findByAccountDetailAndClassLevel(AccountDetail accountDetail, ClassLevel classLevel);

    List<AccountDetailClassLevel> findAllByAccountDetail(AccountDetail accountDetail);
}
