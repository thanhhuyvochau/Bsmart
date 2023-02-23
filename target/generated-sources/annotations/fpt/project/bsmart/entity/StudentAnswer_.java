package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StudentAnswer.class)
public abstract class StudentAnswer_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<StudentAnswer, String> openAnswer;
	public static volatile SingularAttribute<StudentAnswer, Answer> answer;
	public static volatile SingularAttribute<StudentAnswer, Account> student;
	public static volatile SingularAttribute<StudentAnswer, SurveyQuestion> surveyQuestion;
	public static volatile SingularAttribute<StudentAnswer, Long> id;

	public static final String OPEN_ANSWER = "openAnswer";
	public static final String ANSWER = "answer";
	public static final String STUDENT = "student";
	public static final String SURVEY_QUESTION = "surveyQuestion";
	public static final String ID = "id";

}

