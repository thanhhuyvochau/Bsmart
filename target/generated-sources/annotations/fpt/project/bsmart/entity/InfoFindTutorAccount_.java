package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InfoFindTutorAccount.class)
public abstract class InfoFindTutorAccount_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<InfoFindTutorAccount, Account> teacher;
	public static volatile SingularAttribute<InfoFindTutorAccount, InfoFindTutorAccountKey> id;
	public static volatile SingularAttribute<InfoFindTutorAccount, InfoFindTutor> infoFindTutor;

	public static final String TEACHER = "teacher";
	public static final String ID = "id";
	public static final String INFO_FIND_TUTOR = "infoFindTutor";

}

