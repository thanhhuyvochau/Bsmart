package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ReferralCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReferralCodeRepository extends JpaRepository<ReferralCode, Long> {

}
