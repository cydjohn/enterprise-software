package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.Date;


public interface ITreatmentExporter<T> {
	
	public T exportDrugTreatment(long tid,String diagnosis,String drug,float dosage,Provider provider);
	
	public T exportRadiology(long tid,String diagnosis,List<Date> dates,Provider provider);
	
	public T exportSurgery(long tid,String diagnosis,Date date,Provider provider);

}
