package edu.stevens.cs548.clinic.domain.research;

import edu.stevens.cs548.clinic.domain.DrugTreatment;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-11-12T16:05:15.932-0500")
@StaticMetamodel(Drug.class)
public class Drug_ {
	public static volatile SingularAttribute<Drug, Long> id;
	public static volatile SingularAttribute<Drug, String> name;
	public static volatile ListAttribute<Drug, DrugTreatment> treatments;
}
