package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Attendance.class)
public abstract class Attendance_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Attendance, StudentClassKey> studentClassKeyId;
	public static volatile SingularAttribute<Attendance, Boolean> isPresent;
	public static volatile SingularAttribute<Attendance, TimeTable> timeTable;
	public static volatile SingularAttribute<Attendance, Long> id;

	public static final String STUDENT_CLASS_KEY_ID = "studentClassKeyId";
	public static final String IS_PRESENT = "isPresent";
	public static final String TIME_TABLE = "timeTable";
	public static final String ID = "id";

}

