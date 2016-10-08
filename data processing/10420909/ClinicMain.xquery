import schema namespace pat = "http://www.example.org/schemas/patient" at "Patient.xsd";
import schema namespace clin = "http://www.example.org/schemas/clinic" at "Clinic.xsd";
import schema namespace trmt = "http://www.example.org/schemas/clinic/treatment" at "Treatment.xsd";

declare namespace ps = "http://www.example.org/schemas/clinc/patients";

declare function local:getPatientDrugs($patientId as xs:string,$klinic as element(clin:Clinic)) {
    for $x in $klinic/pat:Patient
    where $x/pat:patient-id = $patientId and $x/pat:treatments/pat:treatment/trmt:drug-treatment/trmt:dosage > 0
    return  <result><diagnosis>{$x/pat:treatments/pat:treatment/trmt:diagnosis/text()}</diagnosis><drugTreatment>{$x/pat:treatments/pat:treatment/trmt:drug-treatment}</drugTreatment></result>
    
    };

let $clinic := doc("ClinicData.xml")/clin:Clinic

return $clinic


