package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name="PatientServiceBean")
public class PatientService implements IPatientServiceLocal,
		IPatientServiceRemote {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientService.class.getCanonicalName());

	private PatientFactory patientFactory;
	
	@SuppressWarnings("unused")
	private PatientDtoFactory patientDtoFactory;

	private IPatientDAO patientDAO;
	
	/**
	 * Default constructor.
	 */
	public PatientService() {
		// TODO initialize factories
		patientFactory = new PatientFactory();
	}
	
	/*@PersistenceContext(unitName="ClinicDomain")
	private EntityManager em;*/
	@Inject @ClinicDomain EntityManager em;
	
	@PostConstruct
	private void initialize(){
		patientDAO = new PatientDAO(em);
	}

	// TODO use dependency injection and EJB lifecycle methods to initialize DAOs

	/**
	 * @see IPatientService#addPatient(String, Date, long)
	 */
	@Override
	public long addPatient(PatientDto dto) throws PatientServiceExn {
		// Use factory to create patient entity, and persist with DAO
		try {
			Patient patient = patientFactory.createPatient(dto.getPatientId(), dto.getName(), dto.getDob(), dto.getAge());
			patientDAO.addPatient(patient);
			return patient.getId();
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}
	
	/**
	 * @see IPatientService#getPatient(long)
	 */
	@Override
	public PatientDto getPatient(long id) throws PatientServiceExn {
		try{
			Patient patient = patientDAO.getPatient(id);
			return new PatientDto(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}
	
	/**
	 * @see IPatientService#getPatientByPatId(long)
	 */
	@Override
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		try{
			Patient patient = patientDAO.getPatientByPatientId(pid);
			return new PatientDto(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	public class TreatmentExporter implements ITreatmentExporter<TreatmentDto> {
		
		private ObjectFactory factory = new ObjectFactory();
		
		@Override
		public TreatmentDto exportDrugTreatment(long tid, String diagnosis, String drug,
				float dosage, Provider prov) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			dto.setId(tid);
			dto.setProvider(prov.getId());
			DrugTreatmentType drugInfo = factory.createDrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			dto.setDrugTreatment(drugInfo);
			return dto;
		}

		@Override
		public TreatmentDto exportRadiology(long tid, String diagnosis, List<Date> dates, Provider prov) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			dto.setId(tid);
			dto.setProvider(prov.getId());
			RadiologyType radiology = factory.createRadiologyType();
			radiology.setDate(dates);
			dto.setRadiology(radiology);
			return dto;
		}

		@Override
		public TreatmentDto exportSurgery(long tid, String diagnosis, Date date, Provider prov) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			dto.setId(tid);
			dto.setProvider(prov.getId());
			SurgeryType surgery = new SurgeryType();
			surgery.setData(date);
			dto.setSurgery(surgery);
			return dto;
		}
		
	}
	
	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// Export treatment DTO from patient aggregate
		try {
			Patient patient = patientDAO.getPatient(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return patient.exportTreatment(tid, visitor);
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public long createPatient(String name, Date dob, long patientId, int age) throws PatientServiceExn {
		try {
			Patient patient = this.patientFactory.createPatient(patientId, name, dob, age);
			patientDAO.addPatient(patient);
			return patient.getId();
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PatientDto[] getPatientsByNameDob(String name, Date dob) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePatient(String name, long id) throws PatientServiceExn {
		Patient patient;
		try {
			patient = patientDAO.getPatient(id);
			if(!name.equals(patient.getName())) {
				throw new PatientServiceExn(
						"Tried to delete wrong patient: name = "+name + "id = " + id);
			}
			else {
				patientDAO.deletePatient(patient);
			}
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
		
		
	}

	@Override
	public void addDrugTreatments(long id, String diagnosis, String drug, float dosage) throws PatientNotFoundExn {
		try {
			Patient patient = patientDAO.getPatient(id);
			TreatmentFactory treatmentFactory = new TreatmentFactory();
			Treatment drugtreat = treatmentFactory.createDrugTreatment(diagnosis, drug, dosage);
			patient.addTreatment(drugtreat);

		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		}
	}

	@Override
	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void deletePatients() {
		patientDAO.deletePatients();
	}
	
	// TODO inject resource value
	@Resource(name="SiteInfo")
	private String siteInformation;
		

	@Override
	public String siteInfo() {
		return siteInformation;
	}



}
