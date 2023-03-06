package fpt.project.bsmart.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Wallet.class)
public abstract class Wallet_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Wallet, User> owner;
	public static volatile SingularAttribute<Wallet, BigDecimal> balance;
	public static volatile SingularAttribute<Wallet, Long> id;
	public static volatile SingularAttribute<Wallet, BigDecimal> previous_balance;

	public static final String OWNER = "owner";
	public static final String BALANCE = "balance";
	public static final String ID = "id";
	public static final String PREVIOUS_BALANCE = "previous_balance";

}

