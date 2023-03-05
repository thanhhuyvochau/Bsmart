package fpt.project.bsmart.entity;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TimeTable.class)
public abstract class TimeTable_ {

	public static volatile SingularAttribute<TimeTable, Instant> date;
	public static volatile SingularAttribute<TimeTable, TimeInWeek> timeInWeek;
	public static volatile SingularAttribute<TimeTable, String> classURL;
	public static volatile SingularAttribute<TimeTable, String> classRoom;
	public static volatile SingularAttribute<TimeTable, Long> id;
	public static volatile SingularAttribute<TimeTable, Integer> currentSlotNums;

	public static final String DATE = "date";
	public static final String TIME_IN_WEEK = "timeInWeek";
	public static final String CLASS_UR_L = "classURL";
	public static final String CLASS_ROOM = "classRoom";
	public static final String ID = "id";
	public static final String CURRENT_SLOT_NUMS = "currentSlotNums";

}

