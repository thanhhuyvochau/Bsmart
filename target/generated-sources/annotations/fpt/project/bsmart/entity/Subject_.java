package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Subject.class)
public abstract class Subject_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile ListAttribute<Subject, Course> courses;
	public static volatile SingularAttribute<Subject, String> code;
	public static volatile SingularAttribute<Subject, String> name;
	public static volatile SingularAttribute<Subject, Long> id;
	public static volatile SingularAttribute<Subject, Category> category;

	public static final String COURSES = "courses";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String CATEGORY = "category";

}

