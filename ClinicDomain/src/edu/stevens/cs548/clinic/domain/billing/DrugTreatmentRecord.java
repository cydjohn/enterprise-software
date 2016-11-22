package edu.stevens.cs548.clinic.domain.billing;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: DrugTreatmentRecord
 *
 */
@Entity
@NamedQuery(
		name="SearchDrugTreatmentRecords",
		query="select t from DrugTreatment t")
public class DrugTreatmentRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2319596137210635349L;

	@Id
	@GeneratedValue
	private long id;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	private String drugName;
	
	private float dosage;
	
	@ManyToOne
	private Subject subject;


	public Subject getSubject() {
		return subject;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public float getDosage() {
		return dosage;
	}

	public void setDosage(float dosage) {
		this.dosage = dosage;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
}
