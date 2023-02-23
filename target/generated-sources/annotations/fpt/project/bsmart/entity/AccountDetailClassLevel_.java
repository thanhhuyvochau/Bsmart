package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountDetailClassLevel.class)
public abstract class AccountDetailClassLevel_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<AccountDetailClassLevel, AccountDetail> accountDetail;
	public static volatile SingularAttribute<AccountDetailClassLevel, AccountDetailClassLevelKey> id;
	public static volatile SingularAttribute<AccountDetailClassLevel, ClassLevel> classLevel;

	public static final String ACCOUNT_DETAIL = "accountDetail";
	public static final String ID = "id";
	public static final String CLASS_LEVEL = "classLevel";

}

