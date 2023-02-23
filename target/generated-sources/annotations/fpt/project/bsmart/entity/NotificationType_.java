package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NotificationType.class)
public abstract class NotificationType_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<NotificationType, String> template;
	public static volatile SingularAttribute<NotificationType, String> code;
	public static volatile SingularAttribute<NotificationType, Long> id;
	public static volatile SingularAttribute<NotificationType, String> title;
	public static volatile SingularAttribute<NotificationType, String> entity;
	public static volatile SingularAttribute<NotificationType, String> url;

	public static final String TEMPLATE = "template";
	public static final String CODE = "code";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String ENTITY = "entity";
	public static final String URL = "url";

}

