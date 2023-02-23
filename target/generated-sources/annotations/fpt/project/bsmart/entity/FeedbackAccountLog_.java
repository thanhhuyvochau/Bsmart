package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EFeedbackAccountLogStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FeedbackAccountLog.class)
public abstract class FeedbackAccountLog_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<FeedbackAccountLog, AccountDetail> accountDetail;
	public static volatile SingularAttribute<FeedbackAccountLog, Long> id;
	public static volatile SingularAttribute<FeedbackAccountLog, String> content;
	public static volatile SingularAttribute<FeedbackAccountLog, Account> account;
	public static volatile SingularAttribute<FeedbackAccountLog, EFeedbackAccountLogStatus> status;

	public static final String ACCOUNT_DETAIL = "accountDetail";
	public static final String ID = "id";
	public static final String CONTENT = "content";
	public static final String ACCOUNT = "account";
	public static final String STATUS = "status";

}

