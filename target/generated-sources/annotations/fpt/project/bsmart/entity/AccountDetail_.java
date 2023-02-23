package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EAccountDetailStatus;
import fpt.project.bsmart.entity.common.EGenderType;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountDetail.class)
public abstract class AccountDetail_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<AccountDetail, Voice> voice;
	public static volatile SingularAttribute<AccountDetail, String> lastName;
	public static volatile SingularAttribute<AccountDetail, Instant> birthDay;
	public static volatile SingularAttribute<AccountDetail, String> trainingSchoolName;
	public static volatile ListAttribute<AccountDetail, AccountDetailSubject> accountDetailSubjects;
	public static volatile SingularAttribute<AccountDetail, EGenderType> gender;
	public static volatile SingularAttribute<AccountDetail, String> level;
	public static volatile SingularAttribute<AccountDetail, String> idCard;
	public static volatile ListAttribute<AccountDetail, Resource> resources;
	public static volatile SingularAttribute<AccountDetail, Boolean> isActive;
	public static volatile SingularAttribute<AccountDetail, String> currentAddress;
	public static volatile SingularAttribute<AccountDetail, String> firstName;
	public static volatile SingularAttribute<AccountDetail, String> password;
	public static volatile SingularAttribute<AccountDetail, String> majors;
	public static volatile SingularAttribute<AccountDetail, String> phone;
	public static volatile SingularAttribute<AccountDetail, Long> id;
	public static volatile ListAttribute<AccountDetail, FeedbackAccountLog> feedbackAccountLogs;
	public static volatile SingularAttribute<AccountDetail, String> email;
	public static volatile SingularAttribute<AccountDetail, Account> account;
	public static volatile SingularAttribute<AccountDetail, EAccountDetailStatus> status;
	public static volatile ListAttribute<AccountDetail, AccountDetailClassLevel> accountDetailClassLevels;

	public static final String VOICE = "voice";
	public static final String LAST_NAME = "lastName";
	public static final String BIRTH_DAY = "birthDay";
	public static final String TRAINING_SCHOOL_NAME = "trainingSchoolName";
	public static final String ACCOUNT_DETAIL_SUBJECTS = "accountDetailSubjects";
	public static final String GENDER = "gender";
	public static final String LEVEL = "level";
	public static final String ID_CARD = "idCard";
	public static final String RESOURCES = "resources";
	public static final String IS_ACTIVE = "isActive";
	public static final String CURRENT_ADDRESS = "currentAddress";
	public static final String FIRST_NAME = "firstName";
	public static final String PASSWORD = "password";
	public static final String MAJORS = "majors";
	public static final String PHONE = "phone";
	public static final String ID = "id";
	public static final String FEEDBACK_ACCOUNT_LOGS = "feedbackAccountLogs";
	public static final String EMAIL = "email";
	public static final String ACCOUNT = "account";
	public static final String STATUS = "status";
	public static final String ACCOUNT_DETAIL_CLASS_LEVELS = "accountDetailClassLevels";

}

