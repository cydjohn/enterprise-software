package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;


/**
 * Entity implementation class for Entity: Treatment
 *
 */

@Entity
@NamedQueries({
	@NamedQuery(
		name="RemoveAllTreatments",
		query="delete from Treatment t")
})
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="TTYPE")
@Table(name="TREATMENT")
public abstract class Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	private String diagnosis;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

	@Column(name="TTYPE",length=2)
	private String treatmentType;
	
	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}


	@ManyToOne
	@JoinColumn(name="patient_fx", referencedColumnName = "id")
	private Patient patient;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
		if (!patient.getTreatments().contains(this)) {
			patient.addTreatment(this);
		}
	}
	
	@ManyToOne
	@JoinColumn(name="provider_fx", referencedColumnName = "id")
	private Provider provider;

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
		if(!provider.getTreatments().contains(this)){
			provider.addTreatment(this);
		}
	}
	
	public abstract void visit(ITreatmentVisitor visitor);
	
	public abstract <T> T export(ITreatmentExporter<T> visitor) throws TreatmentExn;
	
	public Treatment() {
		super();
		
	}
   
}
