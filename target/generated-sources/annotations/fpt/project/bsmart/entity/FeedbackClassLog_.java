package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EFeedbackClassLogStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FeedbackClassLog.class)
public abstract class FeedbackClassLog_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<FeedbackClassLog, Long> id;
	public static volatile SingularAttribute<FeedbackClassLog, String> content;
	public static volatile SingularAttribute<FeedbackClassLog, Account> account;
	public static volatile SingularAttribute<FeedbackClassLog, EFeedbackClassLogStatus> status;
	public static volatile SingularAttribute<FeedbackClassLog, Class> aClass;

	public static final String ID = "id";
	public static final String CONTENT = "content";
	public static final String ACCOUNT = "account";
	public static final String STATUS = "status";
	public static final String A_CLASS = "aClass";

}

