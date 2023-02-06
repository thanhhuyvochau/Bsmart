package com.project.Bsmart.repository;

import com.project.Bsmart.entity.Role;
import com.project.Bsmart.entity.User;
import com.project.Bsmart.entity.common.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
