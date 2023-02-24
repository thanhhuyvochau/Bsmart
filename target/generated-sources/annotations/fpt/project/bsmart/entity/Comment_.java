package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Comment.class)
public abstract class Comment_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Comment, Question> question;
	public static volatile ListAttribute<Comment, Comment> subComments;
	public static volatile SingularAttribute<Comment, Comment> parentComment;
	public static volatile ListAttribute<Comment, Vote> votes;
	public static volatile SingularAttribute<Comment, Long> id;
	public static volatile SingularAttribute<Comment, String> content;
	public static volatile SingularAttribute<Comment, User> account;

	public static final String QUESTION = "question";
	public static final String SUB_COMMENTS = "subComments";
	public static final String PARENT_COMMENT = "parentComment";
	public static final String VOTES = "votes";
	public static final String ID = "id";
	public static final String CONTENT = "content";
	public static final String ACCOUNT = "account";

}

