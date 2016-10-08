import module namespace solu = "http://www.example.com/xquery/clinic/Solutions" at "solutions.xquery";

import schema namespace pat = "http://www.example.org/schemas/patient" at "Patient.xsd";
import schema namespace clin = "http://www.example.org/schemas/clinic" at "Clinic.xsd";


let $clinic := doc("ClinicData.xml")/clin:Clinic

return solu:getPatientTreatments('12345678',$clinic)