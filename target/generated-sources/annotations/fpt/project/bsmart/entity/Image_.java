package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EImageType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Image.class)
public abstract class Image_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Image, String> note;
	public static volatile SingularAttribute<Image, Course> course;
	public static volatile SingularAttribute<Image, Long> id;
	public static volatile SingularAttribute<Image, EImageType> type;
	public static volatile SingularAttribute<Image, User> user;
	public static volatile SingularAttribute<Image, String> url;
	public static volatile SingularAttribute<Image, Boolean> status;

	public static final String NOTE = "note";
	public static final String COURSE = "course";
	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String USER = "user";
	public static final String URL = "url";
	public static final String STATUS = "status";

}

