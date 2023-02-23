package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InfoFindTutorSubject.class)
public abstract class InfoFindTutorSubject_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<InfoFindTutorSubject, Subject> subject;
	public static volatile SingularAttribute<InfoFindTutorSubject, InformationFindTutorSubjectKey> id;
	public static volatile SingularAttribute<InfoFindTutorSubject, InfoFindTutor> infoFindTutor;

	public static final String SUBJECT = "subject";
	public static final String ID = "id";
	public static final String INFO_FIND_TUTOR = "infoFindTutor";

}

