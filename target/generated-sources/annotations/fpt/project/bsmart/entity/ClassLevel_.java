package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClassLevel.class)
public abstract class ClassLevel_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<ClassLevel, EClassLevel> code;
	public static volatile SingularAttribute<ClassLevel, String> name;
	public static volatile SingularAttribute<ClassLevel, Long> id;
	public static volatile ListAttribute<ClassLevel, AccountDetailClassLevel> accountDetailClassLevels;

	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String ACCOUNT_DETAIL_CLASS_LEVELS = "accountDetailClassLevels";

}

