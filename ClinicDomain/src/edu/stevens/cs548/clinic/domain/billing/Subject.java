package edu.stevens.cs548.clinic.domain.billing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Subject
 *
 */
@Entity

public class Subject implements Serializable {

	public Subject() {
		super();
		treatments = new ArrayList<DrugTreatmentRecord>();
	}

	/*
	 * This will be same as patient id in Clinic database
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/*
	 * Anonymize patient (randomly generated when assigned)
	 */
	private long subjectId;
	@OneToMany(mappedBy = "subject")
	@OrderBy
	private Collection<DrugTreatmentRecord> treatments;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	public Collection<DrugTreatmentRecord> getTreatments() {
		return treatments;
	}
	public void setTreatments(Collection<DrugTreatmentRecord> treatments) {
		this.treatments = treatments;
	}
	
	
	

}
