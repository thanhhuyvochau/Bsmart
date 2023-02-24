package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Resource.class)
public abstract class Resource_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Resource, AccountDetail> accountDetail;
	public static volatile SingularAttribute<Resource, String> name;
	public static volatile ListAttribute<Resource, Course> course;
	public static volatile ListAttribute<Resource, Pano> panos;
	public static volatile SingularAttribute<Resource, Long> id;
	public static volatile ListAttribute<Resource, User> accounts;
	public static volatile SingularAttribute<Resource, String> url;
	public static volatile SingularAttribute<Resource, EResourceType> resourceType;

	public static final String ACCOUNT_DETAIL = "accountDetail";
	public static final String NAME = "name";
	public static final String COURSE = "course";
	public static final String PANOS = "panos";
	public static final String ID = "id";
	public static final String ACCOUNTS = "accounts";
	public static final String URL = "url";
	public static final String RESOURCE_TYPE = "resourceType";

}

