package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ETypeLearn;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Course.class)
public abstract class Course_ {

	public static volatile SingularAttribute<Course, Image> image;
	public static volatile SingularAttribute<Course, User> mentor;
	public static volatile SingularAttribute<Course, String> code;
	public static volatile SingularAttribute<Course, ECourseLevel> level;
	public static volatile SingularAttribute<Course, Subject> subject;
	public static volatile ListAttribute<Course, Class> classes;
	public static volatile SingularAttribute<Course, String> description;
	public static volatile SingularAttribute<Course, ETypeLearn> type;
	public static volatile ListAttribute<Course, Section> sections;
	public static volatile SingularAttribute<Course, Double> referenceDiscount;
	public static volatile SingularAttribute<Course, String> name;
	public static volatile SingularAttribute<Course, Long> id;
	public static volatile SingularAttribute<Course, Boolean> status;

	public static final String IMAGE = "image";
	public static final String MENTOR = "mentor";
	public static final String CODE = "code";
	public static final String LEVEL = "level";
	public static final String SUBJECT = "subject";
	public static final String CLASSES = "classes";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String SECTIONS = "sections";
	public static final String REFERENCE_DISCOUNT = "referenceDiscount";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String STATUS = "status";

}

