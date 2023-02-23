package fpt.project.bsmart.entity;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Pano.class)
public abstract class Pano_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Pano, Resource> resource;
	public static volatile SingularAttribute<Pano, String> name;
	public static volatile SingularAttribute<Pano, Instant> publishDate;
	public static volatile SingularAttribute<Pano, String> linkUrl;
	public static volatile SingularAttribute<Pano, Long> id;
	public static volatile SingularAttribute<Pano, Boolean> isVisible;
	public static volatile SingularAttribute<Pano, Instant> expirationDate;

	public static final String RESOURCE = "resource";
	public static final String NAME = "name";
	public static final String PUBLISH_DATE = "publishDate";
	public static final String LINK_URL = "linkUrl";
	public static final String ID = "id";
	public static final String IS_VISIBLE = "isVisible";
	public static final String EXPIRATION_DATE = "expirationDate";

}

