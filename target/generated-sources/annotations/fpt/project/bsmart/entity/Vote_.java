package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Vote.class)
public abstract class Vote_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Vote, Question> question;
	public static volatile SingularAttribute<Vote, Comment> comment;
	public static volatile SingularAttribute<Vote, Long> id;
	public static volatile SingularAttribute<Vote, Boolean> vote;
	public static volatile SingularAttribute<Vote, Account> account;

	public static final String QUESTION = "question";
	public static final String COMMENT = "comment";
	public static final String ID = "id";
	public static final String VOTE = "vote";
	public static final String ACCOUNT = "account";

}

