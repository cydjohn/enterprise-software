package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.logging.Logger;

public class PatientDAO implements IPatientDAO {
	
	@PersistenceContext
	private EntityManager em;
	private TreatmentDAO treatmentDAO;
	
	public PatientDAO(EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientDAO.class.getCanonicalName());
	
	public void addPatient(Patient patient) throws PatientExn{
		long pid = patient.getPatientId();
		TypedQuery<Patient> query = em.createNamedQuery("SearchPatientByPatientID", Patient.class).setParameter("pid", pid);
		List<Patient> patients = query.getResultList();
		if (patients.size() < 1) {
			em.persist(patient);
			patient.setTreatmentDAO(this.treatmentDAO);
		}
		else {
			Patient patient2 = patients.get(0);
			throw new PatientExn("Insertion: Patient with patient id(" + 
								pid +") already exists.\n** Name: "+
								patient2.getName());
		}
	}

	public Patient getPatientByDbId(long id) throws PatientExn {
		Patient patient = em.find(Patient.class, id);
		if (patient==null) {
			throw new PatientExn("Patient not found: primary key = "+ id);
		}
		else {
			patient.setTreatmentDAO(this.treatmentDAO);
			return patient;
		}
	}
	
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		TypedQuery<Patient> query = 
				em.createNamedQuery("SearchPatientByPatientID",Patient.class)
				.setParameter("pid", pid);
		List<Patient> patients = query.getResultList();
		if (patients.size() > 1) {
			throw new PatientExn("Duplicate patient records: patient id =" + pid);
		}
		else if (patients.size() < 1){
			throw new PatientExn("Patient not found: patient id =" + pid);
		}
		else {
			Patient patient = patients.get(0);
			patient.setTreatmentDAO(this.treatmentDAO);
			return patient;
		}
	}

	public List<Patient> getPatientByNameDob(String name, Date dob) {
		TypedQuery<Patient> query = 
				em.createNamedQuery("SearchPatientByNameDOB",Patient.class)
				.setParameter("name", name)
				.setParameter("dob", dob);
		List<Patient> patients = query.getResultList();
		for(Patient patient:patients) {
			patient.setTreatmentDAO(this.treatmentDAO);
		}
		return patients;
	}



	public void deletePatient(Patient patient) throws PatientExn {
//		em.createQuery("delete from Treatment t where t.patient.id = :id")
//			.setParameter("id", patient.getId())
//			.executeUpdate();
		em.remove(patient);
	}

}
