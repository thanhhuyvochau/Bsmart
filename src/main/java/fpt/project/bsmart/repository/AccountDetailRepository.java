package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.AccountDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountDetailRepository extends JpaRepository<AccountDetail, Long> {

    Page<AccountDetail> findAllByIsActiveIsTrue(Pageable pageable);

    List<AccountDetail> findAllByIdInAndIsActiveIsFalse(List<Long> ids);

    //   Optional<AccountDetail> findAllByIdsInAndIsActiveIsFalse(long id);
    Page<AccountDetail> findAllByIsActiveIsFalse(Pageable pageable);

    Boolean existsAccountByEmail(String email);

    Boolean existsAccountDetailByIdCard(String idCart);

    Boolean existsAccountDetailByPhone(String phone);

    Boolean existsAccountDetailByEmail(String mail);

    AccountDetail findByAccount(Account account);

    Page<AccountDetail> findAllByIsActive(Boolean isActive, Pageable pageable);

}
