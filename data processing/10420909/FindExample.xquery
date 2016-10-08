module namespace find =   'http://www.example.com/xquery/clinic/FindExample';
import schema namespace pat = "http://www.example.org/schemas/patient" at "Patient.xsd";
import schema namespace clin = "http://www.example.org/schemas/clinic" at "Clinic.xsd";

declare function find:findPatients ($klinic as element(clin:Clinic)) as element(pat:Patient) * {
    $klinic/pat:Patient
};
