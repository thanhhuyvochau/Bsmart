package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Skill.class)
public abstract class Skill_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Skill, MentorProfile> mentor;
	public static volatile SingularAttribute<Skill, String> name;
	public static volatile SingularAttribute<Skill, String> description;
	public static volatile SingularAttribute<Skill, String> skillTag;
	public static volatile SingularAttribute<Skill, Long> id;
	public static volatile SingularAttribute<Skill, String> certificateUrl;

	public static final String MENTOR = "mentor";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String SKILL_TAG = "skillTag";
	public static final String ID = "id";
	public static final String CERTIFICATE_URL = "certificateUrl";

}

