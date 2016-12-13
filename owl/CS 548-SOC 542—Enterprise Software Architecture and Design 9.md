# CS 548/SOC 542—Enterprise Software Architecture and Design
## Assignment Ten—OWL

<center>***Yudong Cao***</center>
<center>***10420909***</center>
> You can find n3 and rdf files SPARQL queries and vedio in the rooter folder.## Report:
Surgeons, Radiologists and Internists are the type of Provider. So I define them as:

```provider:surgeon rdf:type clinic:provider. provider:radiologist rdf:type    
clinic:provider. provider:internist rdf:type clinic:provider.
```
Surgery, Radiology and DrugTreatment are the form of Treatment.I define them as the subclass of treatment:

```clinic:drugTreatment rdf:type rdfs:Class; rdfs:subClassOf clinic:treatment.surgery:date rdf:type rdf:Property; rdfs:domain clinic:surgery.radiology:dates rdf:type rdf:Property; rdfs:domain clinic:radiology.
```
ProvidedBy is a property that relates Treatments to Providers.I think it is the provider.

```treatment:providedBy rdf:type owl:FunctionalProperty;rdfs:domain clinic:provider.
```
ReceivedBy is a property that relates Treatments to Patients.I think it is the patient.

```treatment:receivedBy rdf:type owl:FunctionalProperty;
rdfs:domain clinic:patient.
```
RadiologistProvided is any form of Treatment that is provided by a Radiologist. It is not just the provider, but it has to be the radiologist.

```treatment:radiologistProvided rdf:type owl:FunctionalProperty; rdfs:domain provider:radiologist.
```
A RadiologyPatient is any Patient who receives treatment from a Radiologist. I follow what hint mentions:
1. Define a property that is the inverse of ReceivedBy:   

```treatment:receive rdf:type rdf:property; rdfs:domain clinic:treatment;owl:inverseOf treatment:receivedBy.
```
2. Define the class RadiologistProvided to be a subclass of a restriction class that implies membership of RadiologyPatient.

```provider:radiologist owl:equivalentClass [ a owl:Restriction;owl:onProperty treatment:receive;owl:someValuesFrom clinic:treamtent ].
```A patient has a unique patient id.I use the way slides show us to restrict uniqueness properties:

```patient:hasIdentityNo rdf:type owl:FunctionalProperty;rdf:type owl:InverseFunctionalProperty;rdfs:domain clinic:patient; rdfs:range xsd:Integer.
```

## Sample data

```
patient:123456789 patient:id "123456789" .
patient:123456789 patient:patientId "11111111" .
patient:123456789 patient:name "John".
patient:123456789 patient:birth "1999-01-01" .
patient:123456789 patient:treatment "drugTreatment" . 

patient:1000000 patient:id "1000000" .
patient:1000000 patient:patientId "122222" .
patient:1000000 patient:name "sally" .
patient:1000000 patient:birth "1993-03-01" .
patient:1000000 patient:treatment "radiology" .

patient:1000000 patient:id "130043200" .
patient:1000000 patient:patientId "1223212" .
patient:1000000 patient:name "lucy" .
patient:1000000 patient:birth "1992-03-01" .
patient:1000000 patient:treatment "radiology" .


provider:radiologist provider:id "53212345436".
provider:radiologist provider:npi "22222222".
provider:radiologist provider:name "smith".
provider:radiologist provider:specilization "radiology".


treatment:234523535235 treatment:id "234523535235" .
treatment:234523535235 treatment:npi "22222222" .
treatment:234523535235 treatment:diagnosis "asfddasfa" .
treatment:234523535235 treatment:providedBy "smith" .
treatment:234523535235 treatment:receive "radiology" .

treatment:124124532 treatment:id "124124532" .
treatment:124124532 treatment:npi "3333333333" .
treatment:124124532 treatment:diagnosis "fewgvsd" .
treatment:124124532 treatment:providedBy "joan" .
treatment:124124532 treatment:receive "radiology" .


```

## SPARQL queries

show the results of queries that return a list of treatments provided by radiologists:

```
PREFIX patient:  <http://www.example.org/clinic/patient/>
PREFIX pat: <http://purl.org/hpi/patchr#>
SELECT ?patientId ?name ?birth
WHERE
  { 
  	?x patient:patientId ?patientId .
  	?x patient:name ?name .
    ?x patient:birth ?birth.
  	?x patient:treatment "radiology"
}
```

------show the results of queries that return a list of patients who receive treatment from radiologists.

```

PREFIX patient:  <http://www.example.org/clinic/patient/>
PREFIX provider:  <http://www.example.org/clinic/provider/>
PREFIX treatment:  <http://www.example.org/clinic/treatment/>
PREFIX pat: <http://purl.org/hpi/patchr#>
SELECT ?npi ?diagnosis ?providedBy
WHERE
  { 
  	?x treatment:npi ?npi .
  	?x treatment:diagnosis ?diagnosis .
    ?x treatment:providedBy ?providedBy.
  	?x treatment:receive "radiology"
}
```