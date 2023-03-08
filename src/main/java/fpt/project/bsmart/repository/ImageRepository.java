package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Image;
import fpt.project.bsmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {




}
