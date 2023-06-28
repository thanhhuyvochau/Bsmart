package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Image;
import fpt.project.bsmart.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {




}
