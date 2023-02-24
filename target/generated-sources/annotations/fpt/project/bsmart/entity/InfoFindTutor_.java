package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InfoFindTutor.class)
public abstract class InfoFindTutor_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile ListAttribute<InfoFindTutor, InfoFindTutorAccount> infoFindTutorAccounts;
	public static volatile ListAttribute<InfoFindTutor, InfoFindTutorSubject> infoFindTutorSubjects;
	public static volatile SingularAttribute<InfoFindTutor, String> address;
	public static volatile SingularAttribute<InfoFindTutor, String> phone;
	public static volatile SingularAttribute<InfoFindTutor, String> fullName;
	public static volatile SingularAttribute<InfoFindTutor, String> description;
	public static volatile SingularAttribute<InfoFindTutor, Long> id;
	public static volatile SingularAttribute<InfoFindTutor, EClassLevel> classLevel;
	public static volatile SingularAttribute<InfoFindTutor, String> email;

	public static final String INFO_FIND_TUTOR_ACCOUNTS = "infoFindTutorAccounts";
	public static final String INFO_FIND_TUTOR_SUBJECTS = "infoFindTutorSubjects";
	public static final String ADDRESS = "address";
	public static final String PHONE = "phone";
	public static final String FULL_NAME = "fullName";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String CLASS_LEVEL = "classLevel";
	public static final String EMAIL = "email";

}

