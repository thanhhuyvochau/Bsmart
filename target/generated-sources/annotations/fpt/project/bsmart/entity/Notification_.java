package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Notification.class)
public abstract class Notification_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Notification, Boolean> isView;
	public static volatile SingularAttribute<Notification, User> sender;
	public static volatile ListAttribute<Notification, Notifier> notifiers;
	public static volatile SingularAttribute<Notification, Long> entityId;
	public static volatile SingularAttribute<Notification, Long> id;
	public static volatile SingularAttribute<Notification, NotificationType> type;
	public static volatile SingularAttribute<Notification, String> content;

	public static final String IS_VIEW = "isView";
	public static final String SENDER = "sender";
	public static final String NOTIFIERS = "notifiers";
	public static final String ENTITY_ID = "entityId";
	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String CONTENT = "content";

}

