package edu.stevens.cs548.clinic.domain.research;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

import edu.stevens.cs548.clinic.domain.DrugTreatment;

/**
 * Entity implementation class for Entity: Drug
 *
 */
@Entity

public class Drug implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Drug() {
		super();
	}
	
	@Id @GeneratedValue
	private long id;
	
	private String name;
	
	@OneToMany
	@OrderBy
	private List<DrugTreatment> treatments;
   
}
