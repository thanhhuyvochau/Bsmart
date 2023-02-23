package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Mark.class)
public abstract class Mark_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Mark, StudentClass> student;
	public static volatile SingularAttribute<Mark, Module> module;
	public static volatile SingularAttribute<Mark, Float> minMark;
	public static volatile SingularAttribute<Mark, Long> id;
	public static volatile SingularAttribute<Mark, Float> maxMark;
	public static volatile SingularAttribute<Mark, Float> mark;

	public static final String STUDENT = "student";
	public static final String MODULE = "module";
	public static final String MIN_MARK = "minMark";
	public static final String ID = "id";
	public static final String MAX_MARK = "maxMark";
	public static final String MARK = "mark";

}

