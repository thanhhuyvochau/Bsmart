package fpt.project.bsmart.entity;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StudentClass.class)
public abstract class StudentClass_ {

	public static volatile SingularAttribute<StudentClass, Boolean> is_enrolled;
	public static volatile SingularAttribute<StudentClass, Long> id;
	public static volatile ListAttribute<StudentClass, Mark> marks;
	public static volatile SingularAttribute<StudentClass, Instant> enrollDate;
	public static volatile SingularAttribute<StudentClass, User> account;
	public static volatile SingularAttribute<StudentClass, Class> aClass;

	public static final String IS_ENROLLED = "is_enrolled";
	public static final String ID = "id";
	public static final String MARKS = "marks";
	public static final String ENROLL_DATE = "enrollDate";
	public static final String ACCOUNT = "account";
	public static final String A_CLASS = "aClass";

}

