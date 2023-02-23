package fpt.project.bsmart.entity;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BaseEntity.class)
public abstract class BaseEntity_ {

	public static volatile SingularAttribute<BaseEntity, String> createdBy;
	public static volatile SingularAttribute<BaseEntity, Instant> created;
	public static volatile SingularAttribute<BaseEntity, String> lastModifiedBy;
	public static volatile SingularAttribute<BaseEntity, Instant> lastModified;

	public static final String CREATED_BY = "createdBy";
	public static final String CREATED = "created";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String LAST_MODIFIED = "lastModified";

}

