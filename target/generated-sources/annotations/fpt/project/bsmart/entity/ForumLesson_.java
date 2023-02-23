package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ForumLesson.class)
public abstract class ForumLesson_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<ForumLesson, Forum> forum;
	public static volatile ListAttribute<ForumLesson, Question> questions;
	public static volatile SingularAttribute<ForumLesson, Section> section;
	public static volatile SingularAttribute<ForumLesson, Long> id;
	public static volatile SingularAttribute<ForumLesson, String> lessonName;

	public static final String FORUM = "forum";
	public static final String QUESTIONS = "questions";
	public static final String SECTION = "section";
	public static final String ID = "id";
	public static final String LESSON_NAME = "lessonName";

}

