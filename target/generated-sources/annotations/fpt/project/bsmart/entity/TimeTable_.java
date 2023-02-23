package fpt.project.bsmart.entity;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TimeTable.class)
public abstract class TimeTable_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<TimeTable, Instant> date;
	public static volatile SingularAttribute<TimeTable, Integer> slotNumber;
	public static volatile SingularAttribute<TimeTable, Long> id;
	public static volatile SingularAttribute<TimeTable, Class> clazz;
	public static volatile SingularAttribute<TimeTable, ArchetypeTime> archetypeTime;
	public static volatile ListAttribute<TimeTable, Attendance> attendances;

	public static final String DATE = "date";
	public static final String SLOT_NUMBER = "slotNumber";
	public static final String ID = "id";
	public static final String CLAZZ = "clazz";
	public static final String ARCHETYPE_TIME = "archetypeTime";
	public static final String ATTENDANCES = "attendances";

}

