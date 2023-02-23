package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Voice.class)
public abstract class Voice_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile ListAttribute<Voice, AccountDetail> accountDetails;
	public static volatile SingularAttribute<Voice, String> name;
	public static volatile SingularAttribute<Voice, Long> id;

	public static final String ACCOUNT_DETAILS = "accountDetails";
	public static final String NAME = "name";
	public static final String ID = "id";

}

