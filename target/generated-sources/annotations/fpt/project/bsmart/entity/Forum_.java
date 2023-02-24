package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Forum.class)
public abstract class Forum_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile ListAttribute<Forum, ForumLesson> forumLessons;
	public static volatile SingularAttribute<Forum, String> code;
	public static volatile SingularAttribute<Forum, Subject> subject;
	public static volatile SingularAttribute<Forum, String> name;
	public static volatile ListAttribute<Forum, Question> questions;
	public static volatile SingularAttribute<Forum, Long> id;
	public static volatile SingularAttribute<Forum, EForumType> type;
	public static volatile SingularAttribute<Forum, Class> clazz;

	public static final String FORUM_LESSONS = "forumLessons";
	public static final String CODE = "code";
	public static final String SUBJECT = "subject";
	public static final String NAME = "name";
	public static final String QUESTIONS = "questions";
	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String CLAZZ = "clazz";

}

