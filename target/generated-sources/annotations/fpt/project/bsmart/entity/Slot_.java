package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.ESlotCode;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Slot.class)
public abstract class Slot_ {

	public static volatile ListAttribute<Slot, ArchetypeTime> archetypeTimes;
	public static volatile SingularAttribute<Slot, ESlotCode> code;
	public static volatile SingularAttribute<Slot, String> name;
	public static volatile SingularAttribute<Slot, String> startTime;
	public static volatile SingularAttribute<Slot, Long> id;
	public static volatile SingularAttribute<Slot, String> endTime;

	public static final String ARCHETYPE_TIMES = "archetypeTimes";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String START_TIME = "startTime";
	public static final String ID = "id";
	public static final String END_TIME = "endTime";

}

