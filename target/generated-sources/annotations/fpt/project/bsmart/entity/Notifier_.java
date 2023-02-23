package fpt.project.bsmart.entity;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Notifier.class)
public abstract class Notifier_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Notifier, Notification> notification;
	public static volatile SingularAttribute<Notifier, Account> notifier;
	public static volatile SingularAttribute<Notifier, Long> id;
	public static volatile SingularAttribute<Notifier, Instant> viewAt;

	public static final String NOTIFICATION = "notification";
	public static final String NOTIFIER = "notifier";
	public static final String ID = "id";
	public static final String VIEW_AT = "viewAt";

}

