package edu.stevens.cs548.clinic.billing;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

//import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

import edu.stevens.cs548.clinic.domain.Treatment;

public class BillingRecord {

	public Treatment getTreatment() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Id@GeneratedValue
	private long id;
	
	private float amount;
	
	@OneToOne
	private Treatment treatment;
	

	private String description;
	
	private Date date;

}
