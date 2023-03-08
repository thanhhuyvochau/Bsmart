package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MentorProfile.class)
public abstract class MentorProfile_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile ListAttribute<MentorProfile, Skill> skills;
	public static volatile SingularAttribute<MentorProfile, String> yearsOfExperience;
	public static volatile SingularAttribute<MentorProfile, String> introduce;
	public static volatile SingularAttribute<MentorProfile, Long> id;
	public static volatile SingularAttribute<MentorProfile, User> user;
	public static volatile SingularAttribute<MentorProfile, Boolean> status;

	public static final String SKILLS = "skills";
	public static final String YEARS_OF_EXPERIENCE = "yearsOfExperience";
	public static final String INTRODUCE = "introduce";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String STATUS = "status";

}

