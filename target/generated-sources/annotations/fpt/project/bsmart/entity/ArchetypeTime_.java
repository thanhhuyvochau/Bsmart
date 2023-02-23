package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ArchetypeTime.class)
public abstract class ArchetypeTime_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<ArchetypeTime, Archetype> archetype;
	public static volatile SingularAttribute<ArchetypeTime, DayOfWeek> dayOfWeek;
	public static volatile ListAttribute<ArchetypeTime, TimeTable> timeTables;
	public static volatile SingularAttribute<ArchetypeTime, Long> id;
	public static volatile SingularAttribute<ArchetypeTime, Slot> slot;

	public static final String ARCHETYPE = "archetype";
	public static final String DAY_OF_WEEK = "dayOfWeek";
	public static final String TIME_TABLES = "timeTables";
	public static final String ID = "id";
	public static final String SLOT = "slot";

}

