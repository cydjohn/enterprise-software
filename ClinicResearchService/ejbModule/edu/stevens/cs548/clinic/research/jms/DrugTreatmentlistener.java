package edu.stevens.cs548.clinic.research.jms;

import java.util.Date;
import java.util.Random;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.billing.BillingRecord;
import edu.stevens.cs548.clinic.domain.billing.DrugTreatmentRecord;
import edu.stevens.cs548.clinic.research.domain.ResearchDAO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

/**
 * Message-Driven Bean implementation class for: DrugTreatmentlistener
 */
@MessageDriven(
		activationConfig = {
				@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
				@ActivationConfigProperty(
						propertyName = "messageSelector", propertyValue = "treatmentType='Drug'")
		}, 
		mappedName = "jms/Treatment")
public class DrugTreatmentlistener implements MessageListener {

    /**
     * Default constructor. 
     */
    public DrugTreatmentlistener() {
        // TODO Auto-generated constructor stub
    }
    
    @PersistenceContext(unitName = "ClinicDomain")
    private EntityManager em;
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
    	 ObjectMessage objMessage = (ObjectMessage) message;
         try {
  		TreatmentDto treatment = (TreatmentDto) objMessage.getObject();
  		
  		ResearchDAO dao = new ResearchDAO(em);
  		ITreatmentDAO tdao = new TreatmentDAO(em);
  		Random r = new Random();
  		DrugTreatmentRecord dtr = new DrugTreatmentRecord();
  		
  		Treatment t = tdao.getTreatment(treatment.getId());
  		
  		dtr.setDate(new Date());
  		dtr.setDosage(treatment.getDrugTreatment().getDosage());
  		dtr.setDrugName(treatment.getDrugTreatment().getName());
  		
  		dao.addDrugTreatmentRecord(dtr);
  		
  	} catch (JMSException | TreatmentExn e) {
  		// TODO Auto-generated catch block
  		// log the exception
  		e.printStackTrace();
  	}
      
    }

}
