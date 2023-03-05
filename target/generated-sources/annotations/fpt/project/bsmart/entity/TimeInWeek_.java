package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TimeInWeek.class)
public abstract class TimeInWeek_ {

	public static volatile SingularAttribute<TimeInWeek, DayOfWeek> dayOfWeek;
	public static volatile ListAttribute<TimeInWeek, TimeTable> timeTables;
	public static volatile SingularAttribute<TimeInWeek, Long> id;
	public static volatile SingularAttribute<TimeInWeek, Slot> slot;
	public static volatile SingularAttribute<TimeInWeek, Class> clazz;

	public static final String DAY_OF_WEEK = "dayOfWeek";
	public static final String TIME_TABLES = "timeTables";
	public static final String ID = "id";
	public static final String SLOT = "slot";
	public static final String CLAZZ = "clazz";

}

