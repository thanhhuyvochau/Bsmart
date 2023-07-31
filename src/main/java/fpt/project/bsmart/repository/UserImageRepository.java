package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.UserImage;
import fpt.project.bsmart.entity.constant.EImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {


    List<UserImage> findByUserAndTypeAndStatusAndVerified(User user, EImageType type, Boolean status , Boolean verified );


    List<UserImage> findByUserAndTypeAndStatus(User user, EImageType type, Boolean b);

    Optional<UserImage> findByIdAndUserAndTypeAndStatus(Long id , User user, EImageType type, Boolean b);

}
