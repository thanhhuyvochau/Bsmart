package fpt.project.bsmart.entity;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TeachingConfirmation.class)
public abstract class TeachingConfirmation_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<TeachingConfirmation, ClassTeacherCandicate> candidate;
	public static volatile SingularAttribute<TeachingConfirmation, String> code;
	public static volatile SingularAttribute<TeachingConfirmation, Boolean> isAccept;
	public static volatile SingularAttribute<TeachingConfirmation, Instant> expireDate;
	public static volatile SingularAttribute<TeachingConfirmation, Long> id;
	public static volatile SingularAttribute<TeachingConfirmation, EConfirmStatus> status;

	public static final String CANDIDATE = "candidate";
	public static final String CODE = "code";
	public static final String IS_ACCEPT = "isAccept";
	public static final String EXPIRE_DATE = "expireDate";
	public static final String ID = "id";
	public static final String STATUS = "status";

}

