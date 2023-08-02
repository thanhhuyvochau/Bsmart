package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.MentorSkill;
import fpt.project.bsmart.entity.Skill;
import fpt.project.bsmart.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorSkillRepository extends JpaRepository<MentorSkill, Long> {

    List<MentorSkill> findByMentorProfileAndStatusAndVerified (MentorProfile mentorProfile , Boolean Status, Boolean verified ) ;

    List<MentorSkill> findBySkill_InAndStatus (List<Subject> skillIds, Boolean Status) ;

    Optional<MentorSkill> findByMentorProfileAndSkillIdAndStatusAndVerified (MentorProfile mentorProfile ,Long skillIds, Boolean Status ,Boolean verified) ;
    List<MentorSkill> findByStatusAndVerified  (Boolean Status, Boolean verified) ;

}
