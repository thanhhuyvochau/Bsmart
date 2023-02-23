package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface VoiceRepository extends JpaRepository<Voice,Long> {


}
