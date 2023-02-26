package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EUserRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Role.class)
public abstract class Role_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Role, EUserRole> code;
	public static volatile SingularAttribute<Role, String> name;
	public static volatile SingularAttribute<Role, Long> id;
	public static volatile ListAttribute<Role, User> users;

	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String USERS = "users";

}

