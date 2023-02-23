package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Answer.class)
public abstract class Answer_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Answer, String> answer;
	public static volatile SingularAttribute<Answer, SurveyQuestion> surveyQuestion;
	public static volatile SingularAttribute<Answer, Long> id;
	public static volatile SingularAttribute<Answer, Boolean> isVisible;
	public static volatile ListAttribute<Answer, StudentAnswer> studentAnswers;

	public static final String ANSWER = "answer";
	public static final String SURVEY_QUESTION = "surveyQuestion";
	public static final String ID = "id";
	public static final String IS_VISIBLE = "isVisible";
	public static final String STUDENT_ANSWERS = "studentAnswers";

}

