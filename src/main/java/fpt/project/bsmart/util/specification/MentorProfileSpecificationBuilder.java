package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.constant.EMentorProfileStatus;
import fpt.project.bsmart.util.SpecificationUtil;
import fpt.project.bsmart.util.StringUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
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

    public MentorProfileSpecificationBuilder searchBySkill(List<Long> skillIds){
        if(skillIds == null || skillIds.isEmpty()){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<MentorProfile, MentorSkill> join = root.join(MentorProfile_.SKILLS, JoinType.LEFT);
            Path<Object> objectPath = join.get(MentorSkill_.SKILL);
            return criteriaBuilder.and(objectPath.in(skillIds));
        });
        return this;
    }

    public MentorProfileSpecificationBuilder queryLike(String q){
        if(StringUtil.isNullOrEmpty(q)){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<MentorProfile, User> mentorProfileUserJoin = root.join(MentorProfile_.USER, JoinType.INNER);
            Expression<String> name = mentorProfileUserJoin.get(User_.FULL_NAME);
            Expression<String> email = mentorProfileUserJoin.get(User_.EMAIL);
            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ", name, email);
            String search = q.replaceAll("\\s\\s+", " ").trim();
            return criteriaBuilder.like(stringExpression,'%' + search + '%');
        });
        return this;
    }

    public MentorProfileSpecificationBuilder queryByStatus(EMentorProfileStatus status){
        if(status == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(MentorProfile_.STATUS), status));
        return this;
    }


    public MentorProfileSpecificationBuilder queryByStatusInterviewed(Boolean isInterviewed){
        if(isInterviewed == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(MentorProfile_.IS_INTERVIEWED), isInterviewed));
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
