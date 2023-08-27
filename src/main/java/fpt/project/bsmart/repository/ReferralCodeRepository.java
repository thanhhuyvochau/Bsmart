package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ReferralCode;
import fpt.project.bsmart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReferralCodeRepository extends JpaRepository<ReferralCode, Long> {
    Optional<ReferralCode> findByCode(String code);

    Page<ReferralCode> findAllByUser(User user, Pageable pageable);
}
