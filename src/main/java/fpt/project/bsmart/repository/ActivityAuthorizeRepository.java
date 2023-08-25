package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.ActivityAuthorize;
import fpt.project.bsmart.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityAuthorizeRepository extends JpaRepository<ActivityAuthorize, Long> {
    List<ActivityAuthorize> findAllByAuthorizeClassAndActivity(Class clazz, Activity activity);

    List<ActivityAuthorize> findAllByAuthorizeClass(Class clazz);
}
