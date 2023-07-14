package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    @Query(value = "CALL CheckFirstLogin(:email)",
            nativeQuery = true)
    Boolean callCheckFirstLoginProcedure(@Param("email") String userEmail);




}
