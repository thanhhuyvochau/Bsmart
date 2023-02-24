package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SurveyQuestion.class)
public abstract class SurveyQuestion_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<SurveyQuestion, String> question;
	public static volatile ListAttribute<SurveyQuestion, Answer> answers;
	public static volatile SingularAttribute<SurveyQuestion, Long> id;
	public static volatile SingularAttribute<SurveyQuestion, Boolean> isVisible;
	public static volatile ListAttribute<SurveyQuestion, StudentAnswer> studentAnswers;
	public static volatile SingularAttribute<SurveyQuestion, EQuestionType> questionType;

	public static final String QUESTION = "question";
	public static final String ANSWERS = "answers";
	public static final String ID = "id";
	public static final String IS_VISIBLE = "isVisible";
	public static final String STUDENT_ANSWERS = "studentAnswers";
	public static final String QUESTION_TYPE = "questionType";

}

