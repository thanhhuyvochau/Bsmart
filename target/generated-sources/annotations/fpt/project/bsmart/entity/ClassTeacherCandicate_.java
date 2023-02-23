package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.ECandicateStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClassTeacherCandicate.class)
public abstract class ClassTeacherCandicate_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<ClassTeacherCandicate, Account> teacher;
	public static volatile SingularAttribute<ClassTeacherCandicate, Long> id;
	public static volatile SingularAttribute<ClassTeacherCandicate, Class> clazz;
	public static volatile SingularAttribute<ClassTeacherCandicate, ECandicateStatus> status;
	public static volatile ListAttribute<ClassTeacherCandicate, TeachingConfirmation> teachingConfirmations;

	public static final String TEACHER = "teacher";
	public static final String ID = "id";
	public static final String CLAZZ = "clazz";
	public static final String STATUS = "status";
	public static final String TEACHING_CONFIRMATIONS = "teachingConfirmations";

}

