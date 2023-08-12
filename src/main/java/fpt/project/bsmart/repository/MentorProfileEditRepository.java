package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.MentorProfileEdit;

import fpt.project.bsmart.entity.constant.EMentorProfileEditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorProfileEditRepository extends JpaRepository<MentorProfileEdit, Long> {
   MentorProfileEdit findByStatus(EMentorProfileEditStatus status) ;
   List<MentorProfileEdit> findAllByMentorProfileAndStatus (MentorProfile mentorProfile, EMentorProfileEditStatus status);
   MentorProfileEdit findByMentorProfileAndStatus(MentorProfile mentorProfile ,EMentorProfileEditStatus status) ;

}
