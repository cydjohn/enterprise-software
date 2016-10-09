package edu.stevens.cs548.clinic.test;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;

/**
 * Session Bean implementation class TestBean
 */
@Singleton
@LocalBean
@Startup
public class InitBean {

	private static Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());

	/**
	 * Default constructor.
	 */
	public InitBean() {
	}
	
	// TODO inject an EM
	@PersistenceContext(unitName="ClinicDomain")
	EntityManager em;

	@PostConstruct
	private void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		logger.info("Your name here: caoyudong 2016-10-09");

		try {

			Calendar calendar = Calendar.getInstance();
			calendar.set(1984, 3, 1);
			
			IPatientDAO patientDAO = new PatientDAO(em);
			IProviderDAO providerDAO = new ProviderDAO(em);
			ITreatmentDAO treatmentDAO = new TreatmentDAO(em);

			PatientFactory patientFactory = new PatientFactory();
			ProviderFactory providerFactory = new ProviderFactory();
			TreatmentFactory treatmentFactory = new TreatmentFactory();
			
			/*
			 * Clear the database and populate with fresh data.
			 * 
			 * If we ensure that deletion of patients cascades deletes of treatments,
			 * then we only need to delete patients.
			 */
			
			patientDAO.deletePatients();
			
			Patient john = patientFactory.createPatient(12345678L, "John Doe", calendar.getTime(), 32);
			patientDAO.addPatient(john);
			
			logger.info("Added patient "+john.getName()+" with id "+john.getId());
			
			// TODO add more testing, including treatments and providers
			Provider park = providerFactory.createProvider(00000001, "Peter Park", "cold");
			try {
				providerDAO.addProvider(park);
				logger.info("Added provider "+ park.getName() +" with id "+park.getId());
				
				Treatment drug = treatmentFactory.createDrugTreatment("SOS", "xxx", 12);
				drug.setProvider(park);
				
				Treatment surgery = treatmentFactory.createSurgery("help", new Date());
				surgery.setProvider(park);
				
				
				john.addTreatment(drug);
				logger.info("Added "+ drug.getTreatmentType() +" treatment with id "+drug.getId() + " to " + drug.getPatient().getName());
				
				john.addTreatment(surgery);
				logger.info("Added "+ surgery.getTreatmentType() +" treatment with id "+surgery.getId() + " to " + surgery.getPatient().getName());
				
			} catch (ProviderExn e) {
				IllegalStateException ex = new IllegalStateException("Failed to add provider record.");
				ex.initCause(e);
				throw ex;
			}
		} catch (PatientExn e) {

			// logger.log(Level.SEVERE, "Failed to add patient record.", e);
			IllegalStateException ex = new IllegalStateException("Failed to add patient record.");
			ex.initCause(e);
			throw ex;
			
		} 
			
	}

}
