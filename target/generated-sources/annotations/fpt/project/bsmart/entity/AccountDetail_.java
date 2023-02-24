package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EGenderType;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountDetail.class)
public abstract class AccountDetail_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<AccountDetail, String> lastName;
	public static volatile SingularAttribute<AccountDetail, Instant> birthDay;
	public static volatile SingularAttribute<AccountDetail, String> trainingSchoolName;
	public static volatile SingularAttribute<AccountDetail, EGenderType> gender;
	public static volatile SingularAttribute<AccountDetail, String> level;
	public static volatile SingularAttribute<AccountDetail, String> idCard;
	public static volatile SingularAttribute<AccountDetail, Boolean> isActive;
	public static volatile SingularAttribute<AccountDetail, String> currentAddress;
	public static volatile SingularAttribute<AccountDetail, String> firstName;
	public static volatile SingularAttribute<AccountDetail, String> password;
	public static volatile SingularAttribute<AccountDetail, String> majors;
	public static volatile SingularAttribute<AccountDetail, String> phone;
	public static volatile SingularAttribute<AccountDetail, Long> id;
	public static volatile SingularAttribute<AccountDetail, User> user;
	public static volatile SingularAttribute<AccountDetail, String> email;
	public static volatile SingularAttribute<AccountDetail, EAccountDetailStatus> status;

	public static final String LAST_NAME = "lastName";
	public static final String BIRTH_DAY = "birthDay";
	public static final String TRAINING_SCHOOL_NAME = "trainingSchoolName";
	public static final String GENDER = "gender";
	public static final String LEVEL = "level";
	public static final String ID_CARD = "idCard";
	public static final String IS_ACTIVE = "isActive";
	public static final String CURRENT_ADDRESS = "currentAddress";
	public static final String FIRST_NAME = "firstName";
	public static final String PASSWORD = "password";
	public static final String MAJORS = "majors";
	public static final String PHONE = "phone";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String EMAIL = "email";
	public static final String STATUS = "status";

}

