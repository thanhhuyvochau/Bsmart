package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EPageContent;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Post.class)
public abstract class Post_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Post, Long> id;
	public static volatile SingularAttribute<Post, Boolean> isVisible;
	public static volatile SingularAttribute<Post, EPageContent> type;
	public static volatile SingularAttribute<Post, String> content;

	public static final String ID = "id";
	public static final String IS_VISIBLE = "isVisible";
	public static final String TYPE = "type";
	public static final String CONTENT = "content";

}

