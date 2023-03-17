package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {
    public Optional<MentorProfile> getMentorProfileByUser(User user);
    @Query("SELECT e from MentorProfile e WHERE e.status = true and e.user.status = false ")
    public List<MentorProfile> getPendingMentorProfileList();
}
