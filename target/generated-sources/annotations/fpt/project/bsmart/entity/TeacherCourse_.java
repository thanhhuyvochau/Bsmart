package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TeacherCourse.class)
public abstract class TeacherCourse_ {

	public static volatile SingularAttribute<TeacherCourse, Boolean> isAllowed;
	public static volatile SingularAttribute<TeacherCourse, Course> course;
	public static volatile SingularAttribute<TeacherCourse, TeacherCourseKey> id;
	public static volatile SingularAttribute<TeacherCourse, Account> account;

	public static final String IS_ALLOWED = "isAllowed";
	public static final String COURSE = "course";
	public static final String ID = "id";
	public static final String ACCOUNT = "account";

}

