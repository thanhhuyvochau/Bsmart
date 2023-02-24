package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<User, Boolean> isKeycloak;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, Role> role;
	public static volatile SingularAttribute<User, String> keycloakUserId;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, Boolean> isActive;
	public static volatile SingularAttribute<User, String> username;

	public static final String IS_KEYCLOAK = "isKeycloak";
	public static final String PASSWORD = "password";
	public static final String ROLE = "role";
	public static final String KEYCLOAK_USER_ID = "keycloakUserId";
	public static final String ID = "id";
	public static final String IS_ACTIVE = "isActive";
	public static final String USERNAME = "username";

}

