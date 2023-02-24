package fpt.project.bsmart.entity;

import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaction.class)
public abstract class Transaction_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Transaction, Class> paymentClass;
	public static volatile SingularAttribute<Transaction, BigDecimal> amount;
	public static volatile SingularAttribute<Transaction, String> orderInfo;
	public static volatile SingularAttribute<Transaction, String> transactionNo;
	public static volatile SingularAttribute<Transaction, Long> id;
	public static volatile SingularAttribute<Transaction, String> vpnCommand;
	public static volatile SingularAttribute<Transaction, User> account;
	public static volatile SingularAttribute<Transaction, Boolean> isSuccess;
	public static volatile SingularAttribute<Transaction, Instant> payDate;

	public static final String PAYMENT_CLASS = "paymentClass";
	public static final String AMOUNT = "amount";
	public static final String ORDER_INFO = "orderInfo";
	public static final String TRANSACTION_NO = "transactionNo";
	public static final String ID = "id";
	public static final String VPN_COMMAND = "vpnCommand";
	public static final String ACCOUNT = "account";
	public static final String IS_SUCCESS = "isSuccess";
	public static final String PAY_DATE = "payDate";

}

