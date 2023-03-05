package fpt.project.bsmart.entity;

import java.time.LocalTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Slot.class)
public abstract class Slot_ {

	public static volatile SingularAttribute<Slot, String> code;
	public static volatile SingularAttribute<Slot, String> name;
	public static volatile SingularAttribute<Slot, LocalTime> startTime;
	public static volatile SingularAttribute<Slot, Long> id;
	public static volatile SingularAttribute<Slot, LocalTime> endTime;

	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String START_TIME = "startTime";
	public static final String ID = "id";
	public static final String END_TIME = "endTime";

}

