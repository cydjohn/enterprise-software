ALTER TABLE TREATMENT DROP CONSTRAINT FK_TREATMENT_provider_fk
ALTER TABLE TREATMENT DROP CONSTRAINT FK_TREATMENT_patient_fk
ALTER TABLE DrugTreatment DROP CONSTRAINT FK_DrugTreatment_ID
ALTER TABLE Radiology DROP CONSTRAINT FK_Radiology_ID
ALTER TABLE Surgery DROP CONSTRAINT FK_Surgery_ID
ALTER TABLE DRUG_DrugTreatment DROP CONSTRAINT FK_DRUG_DrugTreatment_treatments_ID
ALTER TABLE DRUG_DrugTreatment DROP CONSTRAINT FK_DRUG_DrugTreatment_Drug_ID
DROP TABLE PATIENT CASCADE
DROP TABLE TREATMENT CASCADE
DROP TABLE DrugTreatment CASCADE
DROP TABLE PROVIDER CASCADE
DROP TABLE Radiology CASCADE
DROP TABLE Surgery CASCADE
DROP TABLE DRUG CASCADE
DROP TABLE BILLINGRECORD CASCADE
DROP TABLE DRUG_DrugTreatment CASCADE
DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN'
