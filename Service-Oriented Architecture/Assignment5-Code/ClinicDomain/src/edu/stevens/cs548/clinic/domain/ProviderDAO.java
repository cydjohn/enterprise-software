package edu.stevens.cs548.clinic.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;



public class ProviderDAO implements IProviderDAO {

	private EntityManager em;
	private TreatmentDAO treatmentDAO;
	
	public ProviderDAO (EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}
	
	//5a
	public Provider getProvider(long id) throws ProviderExn {
		Provider prov = em.find(Provider.class, id);
		if (prov == null){
			throw new ProviderExn("Provider not found: primary key = " + id);
		} else {
			return prov;
		}
	}
	
	//5b
	public Provider getPrividerByNPI(long npi) throws ProviderExn{
		TypedQuery<Provider> query = em.createNamedQuery("SearchProviderByNPI", Provider.class)
				.setParameter("npi", npi);
		List<Provider> providers = query.getResultList();
		if (providers.size() > 1){
			throw new ProviderExn("Duplicate patient records: National Provider Identifier = " + npi);
		} else if (providers.size() < 1) {
			throw new ProviderExn("Provider not found: National Provider Identifier = " + npi);
		} else {
			Provider prov = providers.get(0);
			return prov;
		}
	}

	//4
	public void addProvider(Provider prov) throws ProviderExn {
		long npi = prov.getNpi();
		TypedQuery<Provider> query = em.createNamedQuery("SearchProviderByNPI", Provider.class)
				.setParameter("npi", npi);
		List<Provider> providers = query.getResultList();
		if (providers.size() < 1) {
			em.persist(prov);
			prov.setTreatmentDAO(new TreatmentDAO(em));
		} else {
			Provider prov2 = providers.get(0);
			throw new ProviderExn("Insertion: Provider with National Provider Identifier(" + npi
					+") already exists.\n** Note:"+prov2.getName());
		}
	}

	public void deleteProvider(Provider prov) throws ProviderExn {
		em.remove(prov);
	}

	
}
