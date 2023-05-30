package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.EAccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {
    Optional<MentorProfile> getMentorProfileByUser(User user);

    Page<MentorProfile> findAll(Specification<MentorProfile> mentorProfileSpecification, Pageable pageable);

    @Query("SELECT e from MentorProfile e WHERE e.status = 'REQUESTING' and e.user.status = false")
    List<MentorProfile> getPendingMentorProfileRequesting();
    @Query("SELECT e from MentorProfile e WHERE  e.status = 'EDITREQUEST' and e.user.status = false")
    List<MentorProfile> getPendingMentorProfileEditRequest();

    List<MentorProfile> findAllByStatus (EAccountStatus status);
}
