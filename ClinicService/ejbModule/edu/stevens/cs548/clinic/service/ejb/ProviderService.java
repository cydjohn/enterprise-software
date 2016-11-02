package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;


@Stateless(name="ProviderServiceBean")
public class ProviderService implements IProviderServiceLocal, IProviderServiceRemote{

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientService.class.getCanonicalName());
	
	private ProviderFactory providerFactory;
	
	private IProviderDAO providerDAO;
	private IPatientDAO patientDAO;
	
	public ProviderService(){
		providerFactory = new ProviderFactory();
	}
	
	/*@PersistenceContext(unitName="ClinicDomain")
	private EntityManager em;*/
	@Inject @ClinicDomain EntityManager em;
	
	@PostConstruct
	private void initialize(){
		providerDAO = new ProviderDAO(em);
		patientDAO = new PatientDAO(em);
	}
	
	@Override
	public long addProvider(ProviderDto prov) throws ProviderServiceExn {
		try{
			Provider provider = providerFactory.createProvider(prov.getNpi(), prov.getName(), prov.getSpecialization());
			providerDAO.addProvider(provider);
			return provider.getId();
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public ProviderDto getProvider(long id) throws ProviderServiceExn {
		try{
			Provider prov = providerDAO.getProvider(id);
			return new ProviderDto(prov);
		} catch (ProviderExn e){
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public ProviderDto getProviderByNPI(long npi) throws ProviderServiceExn {
		try{
			Provider prov = providerDAO.getProviderByNPI(npi);
			return new ProviderDto(prov);
		} catch (ProviderExn e){
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public long addTreatmentForPat(TreatmentDto treatment, long pid, long npi)
			throws TreatmentNotFoundExn, PatientNotFoundExn, ProviderServiceExn {
		try {
			Patient pat = patientDAO.getPatient(pid);
			Provider prov = providerDAO.getProviderByNPI(npi);
			TreatmentFactory treatmentFactory = new TreatmentFactory();
			if (npi != treatment.getProvider()){
				throw new ProviderServiceExn("The provider can not add this treatment:npi = "+npi);
			}
			if (treatment.getDrugTreatment() != null){
				Treatment t = treatmentFactory.createDrugTreatment(treatment.getDiagnosis(), treatment.getDrugTreatment().getName(), treatment.getDrugTreatment().getDosage());
				t.setProvider(prov);
				return pat.addTreatment(t);
			} else if (treatment.getSurgery() != null){
				Treatment t = treatmentFactory.createSurgery(treatment.getDiagnosis(), treatment.getSurgery().getData());
				t.setProvider(prov);
				return pat.addTreatment(t);
			} else if (treatment.getRadiology() != null) {
				Treatment t = treatmentFactory.createRadiology(treatment.getDiagnosis(), treatment.getRadiology().getDate());
				t.setProvider(prov);
				return pat.addTreatment(t);
			} else {
				throw new TreatmentNotFoundExn("The treatment type is null!");
			}
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e){
			throw new ProviderServiceExn(e.toString());
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
	public TreatmentDto getTreatment(long id, long tid) throws TreatmentNotFoundExn, ProviderServiceExn {
		// Export treatment DTO from patient aggregate
		try {
			Provider prov = providerDAO.getProvider(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return prov.exportTreatment(tid, visitor);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
	
	@Resource(name="SiteInfo")
	private String siteInformation;
	

	@Override
	public String siteInfo() {
		return siteInformation;
	}

}
