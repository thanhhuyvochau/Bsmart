package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ratings.class)
public abstract class Ratings_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Ratings, String> freeTimeInDay;
	public static volatile SingularAttribute<Ratings, Integer> freeTimeInWeek;
	public static volatile SingularAttribute<Ratings, Long> rating;
	public static volatile SingularAttribute<Ratings, Long> id;
	public static volatile SingularAttribute<Ratings, Long> classLevel;
	public static volatile SingularAttribute<Ratings, Long> userId;
	public static volatile SingularAttribute<Ratings, String> learningAbility;
	public static volatile SingularAttribute<Ratings, Long> subjectName;

	public static final String FREE_TIME_IN_DAY = "freeTimeInDay";
	public static final String FREE_TIME_IN_WEEK = "freeTimeInWeek";
	public static final String RATING = "rating";
	public static final String ID = "id";
	public static final String CLASS_LEVEL = "classLevel";
	public static final String USER_ID = "userId";
	public static final String LEARNING_ABILITY = "learningAbility";
	public static final String SUBJECT_NAME = "subjectName";

}

