package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RequestType.class)
public abstract class RequestType_ {

	public static volatile SingularAttribute<RequestType, String> name;
	public static volatile SingularAttribute<RequestType, Long> id;
	public static volatile ListAttribute<RequestType, Request> requests;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String REQUESTS = "requests";

}

