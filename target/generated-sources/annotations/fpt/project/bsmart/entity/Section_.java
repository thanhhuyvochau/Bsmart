package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Section.class)
public abstract class Section_ {

	public static volatile SingularAttribute<Section, String> name;
	public static volatile SingularAttribute<Section, Course> course;
	public static volatile SingularAttribute<Section, Long> id;
	public static volatile ListAttribute<Section, Module> modules;

	public static final String NAME = "name";
	public static final String COURSE = "course";
	public static final String ID = "id";
	public static final String MODULES = "modules";

}

