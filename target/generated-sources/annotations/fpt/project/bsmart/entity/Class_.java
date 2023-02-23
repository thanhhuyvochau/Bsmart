package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EClassStatus;
import fpt.project.bsmart.entity.common.EClassType;
import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Class.class)
public abstract class Class_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Class, BigDecimal> unitPrice;
	public static volatile ListAttribute<Class, StudentClass> studentClasses;
	public static volatile SingularAttribute<Class, Long> maxNumberStudent;
	public static volatile SingularAttribute<Class, String> code;
	public static volatile SingularAttribute<Class, Instant> endDate;
	public static volatile SingularAttribute<Class, Long> numberStudent;
	public static volatile ListAttribute<Class, FeedbackClassLog> feedbackClassLogs;
	public static volatile SingularAttribute<Class, Instant> closingDate;
	public static volatile SingularAttribute<Class, Boolean> isActive;
	public static volatile SingularAttribute<Class, ClassLevel> classLevel;
	public static volatile ListAttribute<Class, Transaction> transactions;
	public static volatile ListAttribute<Class, Section> sections;
	public static volatile SingularAttribute<Class, Forum> forum;
	public static volatile SingularAttribute<Class, String> name;
	public static volatile ListAttribute<Class, TimeTable> timeTables;
	public static volatile SingularAttribute<Class, Course> course;
	public static volatile SingularAttribute<Class, Long> moodleClassId;
	public static volatile SingularAttribute<Class, Long> id;
	public static volatile ListAttribute<Class, ClassTeacherCandicate> candicates;
	public static volatile SingularAttribute<Class, Long> minNumberStudent;
	public static volatile SingularAttribute<Class, Instant> startDate;
	public static volatile SingularAttribute<Class, Account> account;
	public static volatile SingularAttribute<Class, EClassType> classType;
	public static volatile SingularAttribute<Class, EClassStatus> status;

	public static final String UNIT_PRICE = "unitPrice";
	public static final String STUDENT_CLASSES = "studentClasses";
	public static final String MAX_NUMBER_STUDENT = "maxNumberStudent";
	public static final String CODE = "code";
	public static final String END_DATE = "endDate";
	public static final String NUMBER_STUDENT = "numberStudent";
	public static final String FEEDBACK_CLASS_LOGS = "feedbackClassLogs";
	public static final String CLOSING_DATE = "closingDate";
	public static final String IS_ACTIVE = "isActive";
	public static final String CLASS_LEVEL = "classLevel";
	public static final String TRANSACTIONS = "transactions";
	public static final String SECTIONS = "sections";
	public static final String FORUM = "forum";
	public static final String NAME = "name";
	public static final String TIME_TABLES = "timeTables";
	public static final String COURSE = "course";
	public static final String MOODLE_CLASS_ID = "moodleClassId";
	public static final String ID = "id";
	public static final String CANDICATES = "candicates";
	public static final String MIN_NUMBER_STUDENT = "minNumberStudent";
	public static final String START_DATE = "startDate";
	public static final String ACCOUNT = "account";
	public static final String CLASS_TYPE = "classType";
	public static final String STATUS = "status";

}

