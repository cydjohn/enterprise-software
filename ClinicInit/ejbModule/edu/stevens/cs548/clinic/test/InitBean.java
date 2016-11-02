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
import edu.stevens.cs548.clinic.domain.PatientDAO;
//import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
//import edu.stevens.cs548.clinic.domain.IProviderDAO;
//import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
//import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
//import edu.stevens.cs548.clinic.domain.Patient;
//import edu.stevens.cs548.clinic.domain.PatientDAO;
//import edu.stevens.cs548.clinic.domain.PatientFactory;
//import edu.stevens.cs548.clinic.domain.Provider;
//import edu.stevens.cs548.clinic.domain.ProviderDAO;
//import edu.stevens.cs548.clinic.domain.ProviderFactory;
//import edu.stevens.cs548.clinic.domain.Treatment;
//import edu.stevens.cs548.clinic.domain.TreatmentDAO;
//import edu.stevens.cs548.clinic.domain.TreatmentFactory;
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
	public InitBean() {}
	
	@Inject @ClinicDomain EntityManager em;
	
	@Inject IPatientServiceLocal patientService;
	@Inject IProviderServiceLocal providerService;

	@PostConstruct
	private void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		logger.info("Your name here: Yudong Cao 2016-10-16");
		
		PatientDAO patientDAO = new PatientDAO(em);

		try {
			patientDAO.deletePatients();
//			patientService.deletePatients();

			Calendar calendar = Calendar.getInstance();
			calendar.set(1993, 10, 2);
			
			PatientDtoFactory patFac = new PatientDtoFactory();
			PatientDto patient1 = patFac.createPatientDto();
			patient1.setName("James");
			patient1.setPatientId(1234567890);
			patient1.setDob(calendar.getTime());
			patient1.setAge(22);
			
			long patId = patientService.addPatient(patient1);
			String seanName = patientService.getPatient(patId).getName();
			long seanId = patientService.getPatient(patId).getId();
			logger.info("Added patient "+seanName+" with id "+seanId);
			
			ProviderDtoFactory proFac = new ProviderDtoFactory();
			ProviderDto provider1 = proFac.createProviderDto();
			provider1.setName("Kobe");
			provider1.setNpi(1234567890);
			provider1.setSpecialization("fever");
			long proId = providerService.addProvider(provider1);
			
			String janeName = providerService.getProvider(proId).getName();
			long janeId = providerService.getProvider(proId).getId();
			logger.info("Added provider "+janeName+" with id "+janeId);
			
			TreatmentDtoFactory treatFac = new TreatmentDtoFactory();
			
			TreatmentDto treatDto = treatFac.createSurgeryDto();
			treatDto.setDiagnosis("hehhe");
			treatDto.setPatient(seanId);
			treatDto.setProvider(provider1.getNpi());
			SurgeryType surgeryType = new SurgeryType();
			surgeryType.setData(new Date());
			treatDto.setSurgery(surgeryType);
			long treatId = providerService.addTreatmentForPat(treatDto, patId, provider1.getNpi());
			
			logger.info("patient's name:" + patId + " and provider with npi:" + providerService.getProvider(proId).getNpi() + "add drug treatment with id:" + treatId);
			
			String diag = patientService.getTreatment(patId, treatId).getDiagnosis();
			logger.info("new treatment diagnosis is " + diag);
				
		} catch (PatientServiceExn e) {
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
