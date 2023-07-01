package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ClassImage;
import fpt.project.bsmart.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClassImageRepository extends JpaRepository<ClassImage, Long> {




}
