package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.MentorProfile_;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MentorProfileSpecificationBuilder {
    public static MentorProfileSpecificationBuilder specificationBuilder () {return new MentorProfileSpecificationBuilder();}
    private final List<Specification<MentorProfile>> specifications = new ArrayList<>();

    public MentorProfileSpecificationBuilder searchByUserName(String name){
        if(name == null || name.trim().isEmpty()){
            return this;
        }
        specifications.add(((root, query, criteriaBuilder) -> {
            Join<MentorProfile, User> join = root.join(MentorProfile_.USER, JoinType.LEFT);
            return criteriaBuilder.like(join.get(User_.FULL_NAME), "%" + name + "%");
        }));
        return this;
    }

    public Specification<MentorProfile> build(){
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<MentorProfile> all(){
        return Specification.where(null);
    }
}
