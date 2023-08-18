package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ConfigReferralCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigReferralCodeRepository extends JpaRepository<ConfigReferralCode, Long> {
    List<ConfigReferralCode> findByActive(boolean active);
    @Modifying
    @Query("UPDATE ConfigReferralCode c SET c.active = false")
    void deactivateAllConfigs();
}
