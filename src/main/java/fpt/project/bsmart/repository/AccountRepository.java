package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUsername(String username);

    Page<User> findAccountByRole(Pageable pageable, Role role);

    Page<User> findAccountByRoleAndIsActiveIsFalse(Pageable pageable, Role role);

    Page<User> findAccountByRoleAndIsActiveAndAccountDetailNotNull(Role role, Boolean active, Pageable pageable);

    List<User> findAllByIdInAndIsActiveIsFalse(List<Long> ids);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Boolean existsAccountByUsername(String username);


//    Boolean existsAccountByEmail(String email);

    User findByUsername(String username);


}
