package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.Role_;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.User_;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.util.StringUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserSpecificationBuilder {
    public static UserSpecificationBuilder specificationBuilder(){return new UserSpecificationBuilder();}
    private final List<Specification<User>> specifications = new ArrayList<>();

    public UserSpecificationBuilder searchByEmail(String email){
        if(StringUtil.isNullOrEmpty(email)){
            return this;
        }
        specifications.add(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.EMAIL), email)));
        return this;
    }

    public UserSpecificationBuilder searchByName(String name){
        if(StringUtil.isNullOrEmpty(name)){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            String search = name.replaceAll("\\s\\s+", " ").trim();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("%").append(search).append("%");
            return criteriaBuilder.like(root.get(User_.FULL_NAME), stringBuilder.toString());
        });
        return this;
    }

    public UserSpecificationBuilder hasRole(EUserRole role){
        specifications.add((root, query, criteriaBuilder) -> {
            Join<User, Role> userRoleJoin = root.join(User_.ROLES);
            List<EUserRole> roles = new ArrayList<>();
            boolean isValidRoleQuery = role != null && (role.equals(EUserRole.STUDENT) || role.equals(EUserRole.TEACHER));
            if(isValidRoleQuery){
                roles.add(role);
            }else{
                roles = Arrays.asList(EUserRole.STUDENT, EUserRole.TEACHER);
            }
            return criteriaBuilder.and(userRoleJoin.get(Role_.CODE).in(roles));
        });
        return this;
    }

    public UserSpecificationBuilder isVerified(Boolean isVerified){
        if(isVerified == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.IS_VERIFIED), isVerified));
        return this;
    }
    public Specification<User> build(){
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }
    private Specification<User> all() {return Specification.where(null);}
}
