package fpt.project.bsmart.repository;



import fpt.project.bsmart.entity.Post;
import fpt.project.bsmart.entity.common.EPageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
        List<Post> findPostByTypeAndIsVisibleIsTrue(EPageContent type) ;


}
