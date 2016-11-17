package edu.stevens.cs548.clinic.domain.billing;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-11-16T15:35:41.530-0500")
@StaticMetamodel(Subject.class)
public class Subject_ {
	public static volatile SingularAttribute<Subject, Long> id;
	public static volatile SingularAttribute<Subject, Long> subjectId;
	public static volatile CollectionAttribute<Subject, DrugTreatmentRecord> treatments;
}
