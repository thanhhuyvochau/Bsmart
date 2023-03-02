package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Category.class)
public abstract class Category_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Category, String> code;
	public static volatile ListAttribute<Category, Subject> subjects;
	public static volatile SingularAttribute<Category, String> name;
	public static volatile SingularAttribute<Category, Long> id;

	public static final String CODE = "code";
	public static final String SUBJECTS = "subjects";
	public static final String NAME = "name";
	public static final String ID = "id";

}

