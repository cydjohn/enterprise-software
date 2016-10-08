package edu.stevens.cs548.clinic.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import static javax.persistence.CascadeType.REMOVE;

@Entity
@NamedQueries({
	@NamedQuery(
			name="SearchProviderByNPI",
			query="select prov from Provider prov where provider.npi = :npi"),
	@NamedQuery(
			name="RemoveAllProviders",
			query="delete from Provider provider")
})
@Table(name="PROVIDER")
public class Provider implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	private long npi;
	private String name;
	private String specialization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNpi() {
		return npi;
	}

	public void setNpi(long npi) {
		this.npi = npi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	
	@OneToMany(cascade = REMOVE, mappedBy="provider")
	@OrderBy
	private List<Treatment> treatments;

	protected List<Treatment> getTreatments() {
		return treatments;
	}

	protected void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO (ITreatmentDAO tdao) {
		this.treatmentDAO = tdao;
	}
	
	//6c
	public long addTreatment(Treatment t){
		this.treatmentDAO.addTreatment(t);
		this.getTreatments().add(t);
		if (t.getProvider() != this) {
			t.setProvider(this);
		}
		return t.getId();
	}
	
	//8a
	public List<Long> getTreatmentIds(){
		List<Long> tids = new ArrayList<Long>();
		for (Treatment t : this.getTreatments()){
			tids.add(t.getId());
		}
		return tids;
	}
	
	public void visitTreatments(ITreatmentVisitor visitor){
		for (Treatment t : this.getTreatments()){
			t.visit(visitor);
		}
	}
	
	//8b
	public <T> T exportTreatment(long tid, ITreatmentExporter<T> visitor) throws ITreatmentDAO.TreatmentExn {
		// Export a treatment without violated Aggregate pattern
		// Check that the exported treatment is a treatment for this provider.
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if (t.getProvider() != this) {
			throw new ITreatmentDAO.TreatmentExn("Inappropriate treatment access: provider = " + id + ", treatment = " + tid);
		}
		return t.export(visitor);
	}

	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();
	}
   
}
