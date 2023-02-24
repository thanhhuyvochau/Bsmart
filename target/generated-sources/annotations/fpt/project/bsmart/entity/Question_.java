package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Question.class)
public abstract class Question_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Question, Forum> forum;
	public static volatile ListAttribute<Question, Comment> comments;
	public static volatile SingularAttribute<Question, Boolean> isClosed;
	public static volatile SingularAttribute<Question, User> student;
	public static volatile ListAttribute<Question, Vote> votes;
	public static volatile SingularAttribute<Question, Long> id;
	public static volatile SingularAttribute<Question, String> title;
	public static volatile SingularAttribute<Question, String> content;
	public static volatile SingularAttribute<Question, ForumLesson> forumLesson;

	public static final String FORUM = "forum";
	public static final String COMMENTS = "comments";
	public static final String IS_CLOSED = "isClosed";
	public static final String STUDENT = "student";
	public static final String VOTES = "votes";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String FORUM_LESSON = "forumLesson";

}

