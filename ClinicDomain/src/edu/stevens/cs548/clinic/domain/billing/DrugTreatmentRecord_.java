package edu.stevens.cs548.clinic.domain.billing;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-11-16T15:35:22.060-0500")
@StaticMetamodel(DrugTreatmentRecord.class)
public class DrugTreatmentRecord_ {
	public static volatile SingularAttribute<DrugTreatmentRecord, Long> id;
	public static volatile SingularAttribute<DrugTreatmentRecord, Date> date;
	public static volatile SingularAttribute<DrugTreatmentRecord, String> drugName;
	public static volatile SingularAttribute<DrugTreatmentRecord, Float> dosage;
	public static volatile SingularAttribute<DrugTreatmentRecord, Subject> subject;
}
