import module namespace solu = "http://www.example.com/xquery/clinic/Solutions" at "solutions.xquery";

import schema namespace pat = "http://www.example.org/schemas/patient" at "Patient.xsd";
import schema namespace clin = "http://www.example.org/schemas/clinic" at "Clinic.xsd";
import schema namespace trmt = "http://www.example.org/schemas/clinic/treatment" at "Treatment.xsd";
import schema namespace prov="http://www.example.org/schemas/provider" at "Provider.xsd";

let $clinic := doc("ClinicData.xml")/clin:Clinic

return solu:getProviderInfo($clinic)