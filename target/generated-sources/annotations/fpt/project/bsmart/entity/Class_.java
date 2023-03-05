package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.ETypeLearn;
import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Class.class)
public abstract class Class_ {

	public static volatile SingularAttribute<Class, Instant> expectedStartDate;
	public static volatile ListAttribute<Class, TimeInWeek> timeInWeeks;
	public static volatile SingularAttribute<Class, String> code;
	public static volatile SingularAttribute<Class, Instant> endDate;
	public static volatile SingularAttribute<Class, Integer> maxStudentNumber;
	public static volatile SingularAttribute<Class, BigDecimal> price;
	public static volatile SingularAttribute<Class, Integer> numberOfStudent;
	public static volatile SingularAttribute<Class, ETypeLearn> typeLearn;
	public static volatile SingularAttribute<Class, Course> course;
	public static volatile SingularAttribute<Class, Integer> minStudentNumber;
	public static volatile SingularAttribute<Class, Long> id;
	public static volatile SingularAttribute<Class, Instant> startDate;

	public static final String EXPECTED_START_DATE = "expectedStartDate";
	public static final String TIME_IN_WEEKS = "timeInWeeks";
	public static final String CODE = "code";
	public static final String END_DATE = "endDate";
	public static final String MAX_STUDENT_NUMBER = "maxStudentNumber";
	public static final String PRICE = "price";
	public static final String NUMBER_OF_STUDENT = "numberOfStudent";
	public static final String TYPE_LEARN = "typeLearn";
	public static final String COURSE = "course";
	public static final String MIN_STUDENT_NUMBER = "minStudentNumber";
	public static final String ID = "id";
	public static final String START_DATE = "startDate";

}

