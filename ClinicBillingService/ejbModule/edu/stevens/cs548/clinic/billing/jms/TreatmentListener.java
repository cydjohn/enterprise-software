package edu.stevens.cs548.clinic.billing.jms;

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

import edu.stevens.cs548.clinic.billing.domain.BillingRecordDAO;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.domain.billing.BillingRecord;
import edu.stevens.cs548.clinic.service.dto.*;

/**
 * Message-Driven Bean implementation class for: TreatmentListener
 */
@MessageDriven(
		activationConfig = {
				@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Topic")
		}, 
		mappedName = "jms/Treatment")
public class TreatmentListener implements MessageListener {

    /**
     * Default constructor. 
     */
    public TreatmentListener() {
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
		BillingRecordDAO dao = new BillingRecordDAO(em);
		ITreatmentDAO tdao = new TreatmentDAO(em);
		Random r = new Random();
		BillingRecord br = new BillingRecord();
		br.setAmount(r.nextFloat()*500);
		br.setDescription("111");
		Treatment t = tdao.getTreatment(treatment.getId());
		br.setTreatment(t);
		br.setDate(new Date());
	
		dao.addBillingRecord(br);
		
	} catch (JMSException | TreatmentExn e) {
		// TODO Auto-generated catch block
		// log the exception
		e.printStackTrace();
	}
    }

}
