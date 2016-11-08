package edu.stevens.cs548.clinic.test;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

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
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.ClinicDomain;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;

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
	/*@PersistenceContext(unitName = "ClinicDomain")
	EntityManager em;*/
	
	@Inject @ClinicDomain EntityManager em;
	
	@Inject IPatientServiceLocal patientService;
	@Inject IProviderServiceLocal providerService;

	@PostConstruct
	private void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		logger.info("Your name here: Yudong Cao 2016-11-08");

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
			
			Patient john = patientFactory.createPatient(12345678, "John Doe", calendar.getTime(), 32);
			patientDAO.addPatient(john);
			
			logger.info("Added patient "+john.getName()+" with id "+john.getId());
			
			Provider park = providerFactory.createProvider(00000001, "Peter Park", "cold");
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
				
			
			PatientDtoFactory patFac = new PatientDtoFactory();
			PatientDto sean = patFac.createPatientDto();
			sean.setName("Sean Chen");
			sean.setPatientId(666888999);
			sean.setDob(calendar.getTime());
			sean.setAge(32);
			
			long patId = patientService.addPatient(sean);
			String seanName = patientService.getPatient(patId).getName();
			long seanId = patientService.getPatient(patId).getId();
			logger.info("Added patient "+seanName+" with id "+seanId);
			
			ProviderDtoFactory proFac = new ProviderDtoFactory();
			ProviderDto jane = proFac.createProviderDto();
			jane.setName("Jane Shi");
			jane.setNpi(00000002);
			jane.setSpecialization("fever");
			long proId = providerService.addProvider(jane);
			
			String janeName = providerService.getProvider(proId).getName();
			long janeId = providerService.getProvider(proId).getId();
			logger.info("Added provider "+janeName+" with id "+janeId);
			
			TreatmentDtoFactory treatFac = new TreatmentDtoFactory();
			
			TreatmentDto treatDto = treatFac.createSurgeryDto();
			treatDto.setDiagnosis("AAA");
			treatDto.setPatient(seanId);
			treatDto.setProvider(janeId);//getNpi()
			DrugTreatmentType drugTreatmentType = new DrugTreatmentType();
			drugTreatmentType.setName("yyyyyyy");
			drugTreatmentType.setDosage(25);
			treatDto.setDrugTreatment(drugTreatmentType);;
			long treatId = providerService.addTreatmentForPat(treatDto, patId,janeId);// jane.getNpi()
			
			logger.info("patient's name:" + patId + " and provider with npi:" + providerService.getProvider(proId).getNpi() + "add drug treatment with id:" + treatId);
			
			String diag = patientService.getTreatment(patId, treatId).getDiagnosis();
			logger.info("new treatment diagnosis is " + diag);
				
		} catch (ProviderExn e) {
			IllegalStateException ex = new IllegalStateException("Failed to add provider record.");
			ex.initCause(e);
			throw ex;
		} catch (PatientServiceExn e) {
			IllegalStateException ex = new IllegalStateException("Failed to add patient record.");
			ex.initCause(e);
			throw ex;
		} catch (PatientExn e) {

			// logger.log(Level.SEVERE, "Failed to add patient record.", e);
			IllegalStateException ex = new IllegalStateException("Failed to add patient record.");
			ex.initCause(e);
			throw ex;
			
		} catch (ProviderServiceExn e) {
			IllegalStateException ex = new IllegalStateException("Failed to add provider record.");
			ex.initCause(e);
			throw ex;
		} 
			
	}

}
