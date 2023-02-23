package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.ERequestStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Request.class)
public abstract class Request_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Request, String> reason;
	public static volatile ListAttribute<Request, RequestReply> requestReplies;
	public static volatile SingularAttribute<Request, RequestType> requestType;
	public static volatile SingularAttribute<Request, Long> id;
	public static volatile SingularAttribute<Request, String> title;
	public static volatile SingularAttribute<Request, String> url;
	public static volatile SingularAttribute<Request, Account> account;
	public static volatile SingularAttribute<Request, ERequestStatus> status;

	public static final String REASON = "reason";
	public static final String REQUEST_REPLIES = "requestReplies";
	public static final String REQUEST_TYPE = "requestType";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String URL = "url";
	public static final String ACCOUNT = "account";
	public static final String STATUS = "status";

}

