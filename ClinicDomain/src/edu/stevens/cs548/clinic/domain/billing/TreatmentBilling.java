package edu.stevens.cs548.clinic.domain.billing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import edu.stevens.cs548.clinic.domain.Treatment;

@Entity
public class TreatmentBilling implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public TreatmentBilling() {
		super();
	}
	
	@Id@GeneratedValue
	private long id;
	
	private float amount;
	
	@OneToOne
	private Treatment treatment;
}
