package fpt.project.bsmart.entity;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<User, Instant> birthday;
	public static volatile SingularAttribute<User, String> instagramLink;
	public static volatile SingularAttribute<User, String> address;
	public static volatile SingularAttribute<User, Wallet> wallet;
	public static volatile ListAttribute<User, Role> roles;
	public static volatile ListAttribute<User, Image> userImages;
	public static volatile SingularAttribute<User, String> fullName;
	public static volatile SingularAttribute<User, String> facebookLink;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> phone;
	public static volatile SingularAttribute<User, String> twitterLink;
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, Boolean> status;

	public static final String BIRTHDAY = "birthday";
	public static final String INSTAGRAM_LINK = "instagramLink";
	public static final String ADDRESS = "address";
	public static final String WALLET = "wallet";
	public static final String ROLES = "roles";
	public static final String USER_IMAGES = "userImages";
	public static final String FULL_NAME = "fullName";
	public static final String FACEBOOK_LINK = "facebookLink";
	public static final String PASSWORD = "password";
	public static final String PHONE = "phone";
	public static final String TWITTER_LINK = "twitterLink";
	public static final String ID = "id";
	public static final String EMAIL = "email";
	public static final String USERNAME = "username";
	public static final String STATUS = "status";

}

