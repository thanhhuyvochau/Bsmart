package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Course.class)
public abstract class Course_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Course, String> code;
	public static volatile SingularAttribute<Course, Resource> resource;
	public static volatile SingularAttribute<Course, Subject> subject;
	public static volatile ListAttribute<Course, Class> classes;
	public static volatile SingularAttribute<Course, String> name;
	public static volatile SingularAttribute<Course, String> description;
	public static volatile ListAttribute<Course, TeacherCourse> teacherCourses;
	public static volatile SingularAttribute<Course, Long> id;
	public static volatile SingularAttribute<Course, String> title;
	public static volatile SingularAttribute<Course, Boolean> isActive;

	public static final String CODE = "code";
	public static final String RESOURCE = "resource";
	public static final String SUBJECT = "subject";
	public static final String CLASSES = "classes";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String TEACHER_COURSES = "teacherCourses";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String IS_ACTIVE = "isActive";

}

