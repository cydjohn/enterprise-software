package edu.stevens.cs548.clinic.domain;

import javax.persistence.EntityManager;

public class TreatmentDAO implements ITreatmentDAO {
	
	public TreatmentDAO(EntityManager em) {
		this.em = em;
	}
	
	private EntityManager em;

	public Treatment getTreatmentByDbId(long id) throws TreatmentExn {
		Treatment treatment = em.find(Treatment.class, id);
		if (treatment== null) {
			throw new TreatmentExn("Missing treatment: id =" + id);
		}
		else {
			return treatment;
		}
	}

	public void addTreatment(Treatment t) {
		em.persist(t);
	}
	
	public void deleteTreatment(Treatment t) {
		em.remove(t);
	}

}
