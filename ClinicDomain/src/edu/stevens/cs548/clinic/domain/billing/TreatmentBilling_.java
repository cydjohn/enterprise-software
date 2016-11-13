package edu.stevens.cs548.clinic.domain.billing;

import edu.stevens.cs548.clinic.domain.Treatment;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-11-12T16:03:03.153-0500")
@StaticMetamodel(TreatmentBilling.class)
public class TreatmentBilling_ {
	public static volatile SingularAttribute<TreatmentBilling, Long> id;
	public static volatile SingularAttribute<TreatmentBilling, Float> amount;
	public static volatile SingularAttribute<TreatmentBilling, Treatment> treatment;
}
