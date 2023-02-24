package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.common.EAccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByCode(EAccountRole eAccountRole);
}
