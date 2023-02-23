package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountDetailSubject.class)
public abstract class AccountDetailSubject_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<AccountDetailSubject, AccountDetail> accountDetail;
	public static volatile SingularAttribute<AccountDetailSubject, Subject> subject;
	public static volatile SingularAttribute<AccountDetailSubject, AccountDetailSubjectKey> id;

	public static final String ACCOUNT_DETAIL = "accountDetail";
	public static final String SUBJECT = "subject";
	public static final String ID = "id";

}

