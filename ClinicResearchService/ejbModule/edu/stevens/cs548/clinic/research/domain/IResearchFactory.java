package edu.stevens.cs548.clinic.research.domain;

import edu.stevens.cs548.clinic.domain.billing.DrugTreatmentRecord;
import edu.stevens.cs548.clinic.domain.billing.Subject;

public interface IResearchFactory {
	
	public DrugTreatmentRecord createDrugTreatmentRecord();
	
	public Subject createSubject();
	
}
