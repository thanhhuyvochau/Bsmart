package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RequestReply.class)
public abstract class RequestReply_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<RequestReply, Request> request;
	public static volatile SingularAttribute<RequestReply, Long> id;
	public static volatile SingularAttribute<RequestReply, String> content;
	public static volatile SingularAttribute<RequestReply, String> url;
	public static volatile SingularAttribute<RequestReply, Account> account;

	public static final String REQUEST = "request";
	public static final String ID = "id";
	public static final String CONTENT = "content";
	public static final String URL = "url";
	public static final String ACCOUNT = "account";

}

