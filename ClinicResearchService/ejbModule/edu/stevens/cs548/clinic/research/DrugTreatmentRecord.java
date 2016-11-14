package edu.stevens.cs548.clinic.research;

import java.util.Date;

import edu.stevens.cs548.clinic.domain.DrugTreatment;

public class DrugTreatmentRecord extends DrugTreatment {

	private Subject subject;
	
	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	private Date date;
	
	private String drugName;
	

}
