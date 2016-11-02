package edu.stevens.cs548.clinic.service.representations;

import java.util.Date;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.ObjectFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.TreatmentType;
import edu.stevens.cs548.clinic.service.web.rest.data.dap.LinkType;

@XmlRootElement
public class TreatmentRepresentation extends TreatmentType {
	
	private ObjectFactory repFactory = new ObjectFactory();
	
	public LinkType getLinkPatient() {
		return this.getPatient();
	}
	
	public LinkType getLinkProvider() {
		return this.getProvider();
	}
	
	public static LinkType getTreatmentLink(long tid, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("treatment");
		UriBuilder ubTreatment = ub.clone().path("{tid}");
		String treatmentURI = ubTreatment.build(Long.toString(tid)).toString();
	
		LinkType link = new LinkType();
		link.setUrl(treatmentURI);
		link.setRelation(Representation.RELATION_TREATMENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	private TreatmentDtoFactory treatmentDtoFactory;
	
	public TreatmentRepresentation() {
		super();
		treatmentDtoFactory = new TreatmentDtoFactory();
	}
	
	public TreatmentRepresentation (TreatmentDto dto, UriInfo uriInfo) {
		this();
		this.id = getTreatmentLink(dto.getId(), uriInfo);
		this.patient =  PatientRepresentation.getPatientLink(dto.getPatient(), uriInfo);
		this.provider = ProviderRepresentation.getProviderLink(dto.getProvider(), uriInfo);
		
		this.diagnosis = dto.getDiagnosis();
		
		if (dto.getDrugTreatment() != null) {
			/*
			 * TODO finish this
			 */
			this.drugTreatment = repFactory.createDrugTreatmentType();
			this.drugTreatment.setName(dto.getDrugTreatment().getName());
			this.drugTreatment.setDosage(dto.getDrugTreatment().getDosage());
		} else if (dto.getSurgery() != null) {
			/*
			 * TODO finish this
			 */
			this.surgery = repFactory.createSurgeryType();
			this.surgery.setData(dto.getSurgery().getData());
		} else if (dto.getRadiology() != null) {
			/*
			 * TODO finish this
			 */
			this.radiology = repFactory.createRadiologyType();
//			this.radiology.setDate(dto.getSurgery().getData());
//			this.getRadiology().setDate(dto.getRadiology().getDate());
			this.radiology.setDate(dto.getRadiology().getDate());
			for(int i = 0; i<dto.getRadiology().getDate().size();i++){
				Date date = dto.getRadiology().getDate().get(i);
				this.radiology.getDate().add(date);
			}
			
		}
	}

	public TreatmentDto getTreatment() {
		TreatmentDto m = null;
		if (this.getDrugTreatment() != null) {
			m = treatmentDtoFactory.createDrugTreatmentDto();
			m.setId(Representation.getId(id));
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
			/*
			 * TODO finish this
			 */
			m.getDrugTreatment().setName(this.getDrugTreatment().getName());
			m.getDrugTreatment().setDosage(this.getDrugTreatment().getDosage());
		} else if (this.getSurgery() != null) {
			/*
			 * TODO finish this
			 */
			m = treatmentDtoFactory.createSurgeryDto();
			m.setId(Representation.getId(id));
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
			m.getSurgery().setData(this.getSurgery().getData());
		} else if (this.getRadiology() != null) {
			/*
			 * TODO finish this
			 */
			m = treatmentDtoFactory.createRadiologyDto();
			m.setId(Representation.getId(id));
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
			m.getRadiology().setDate(this.getRadiology().getDate());
		}
		return m;
	}
	
}
