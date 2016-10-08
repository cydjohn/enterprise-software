module namespace solu =   'http://www.example.com/xquery/clinic/Solutions';
import schema namespace pat = "http://www.example.org/schemas/patient" at "Patient.xsd";
import schema namespace clin = "http://www.example.org/schemas/clinic" at "Clinic.xsd";
import schema namespace trmt = "http://www.example.org/schemas/clinic/treatment" at "Treatment.xsd";
import schema namespace prov="http://www.example.org/schemas/provider" at "Provider.xsd";

declare namespace ps = "http://www.example.org/schemas/clinc/patients";


declare function solu:getPatientTreatments ($patientId as xs:string,$klinic as element(clin:Clinic)) as element(pat:treatments) * {
    for $x in $klinic/pat:Patient
    where $x/pat:patient-id = $patientId
    return $x/pat:treatments
}; 

declare function solu:getPatientDrugs($patientId as xs:string,$klinic as element(clin:Clinic)) {
    for $x in $klinic/pat:Patient
    where $x/pat:patient-id = $patientId and $x/pat:treatments/pat:treatment/trmt:drug-treatment/trmt:dosage > 0
    return  <result><diagnosis>{$x/pat:treatments/pat:treatment/trmt:diagnosis/text()}</diagnosis><drugTreatment>{$x/pat:treatments/pat:treatment/trmt:drug-treatment}</drugTreatment></result>
    
    };

declare function solu:getTreatmentInfo($klinic as element(clin:Clinic)) {
    for $x in $klinic/pat:Patient
    return  <result><patient-id>{$x/pat:patient-id/text()}</patient-id><trmt:Provider>{ $x/pat:treatments/pat:treatment/trmt:provider-id/text() }</trmt:Provider><Treatment>{$x/pat:treatments/pat:treatment}</Treatment></result>
};


declare function solu:getProviderInfo($klinic as element(clin:Clinic)) {
    for $x in $klinic/prov:Provider,$p in $klinic/pat:Patient, $pt in $p/pat:treatments
    where $x/prov:provider-id = $pt/pat:treatment/trmt:provider-id
    return <result><provider>{$x}</provider><patient><name>{$p/pat:name}</name><patient-id>{$p/pat:patient-id}</patient-id><patientTreatment>{$pt/pat:treatment}</patientTreatment></patient></result>
};

declare function solu:getDrugInfo($klinic as element(clin:Clinic)) {
    for $p in $klinic/pat:Patient, $pt in $p/pat:treatments/pat:treatment,$dt in $pt/trmt:drug-treatment,$prov in $klinic/prov:Provider
    where $pt/trmt:provider-id = $prov/prov:provider-id 
    return <result><drug>{$pt/trmt:drug-treatment}</drug><patient-id>{$p/pat:patient-id/text()}</patient-id><name>{$p/pat:name/text()}</name><provider>{$prov/prov:name/text()}</provider><diagnosis>{$pt/trmt:diagnosis/text()}</diagnosis></result>
};



