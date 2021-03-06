CREATE TABLE PATIENT (ID BIGINT NOT NULL, BIRTHDATE DATE, NAME VARCHAR(255), PATIENTID BIGINT, PRIMARY KEY (ID))
CREATE TABLE TREATMENT (ID BIGINT NOT NULL, TTYPE VARCHAR(31), DIAGNOSIS VARCHAR(255), patient_fk BIGINT, provider_fk BIGINT, PRIMARY KEY (ID))
CREATE TABLE DrugTreatment (ID BIGINT NOT NULL, DOSAGE FLOAT, DRUG VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE PROVIDER (ID BIGINT NOT NULL, NAME VARCHAR(255), NPI BIGINT, SPECIALIZATION VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE Radiology (ID BIGINT NOT NULL, DATES BYTEA, PRIMARY KEY (ID))
CREATE TABLE Surgery (ID BIGINT NOT NULL, DATE DATE, PRIMARY KEY (ID))
CREATE TABLE DRUG (ID BIGINT NOT NULL, NAME VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE BILLINGRECORD (ID  SERIAL NOT NULL, AMOUNT FLOAT, DATE DATE, DESCRIPTION VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE DRUG_DrugTreatment (Drug_ID BIGINT NOT NULL, treatments_ID BIGINT NOT NULL, PRIMARY KEY (Drug_ID, treatments_ID))
ALTER TABLE TREATMENT ADD CONSTRAINT FK_TREATMENT_provider_fk FOREIGN KEY (provider_fk) REFERENCES PROVIDER (ID)
ALTER TABLE TREATMENT ADD CONSTRAINT FK_TREATMENT_patient_fk FOREIGN KEY (patient_fk) REFERENCES PATIENT (ID)
ALTER TABLE DrugTreatment ADD CONSTRAINT FK_DrugTreatment_ID FOREIGN KEY (ID) REFERENCES TREATMENT (ID)
ALTER TABLE Radiology ADD CONSTRAINT FK_Radiology_ID FOREIGN KEY (ID) REFERENCES TREATMENT (ID)
ALTER TABLE Surgery ADD CONSTRAINT FK_Surgery_ID FOREIGN KEY (ID) REFERENCES TREATMENT (ID)
ALTER TABLE DRUG_DrugTreatment ADD CONSTRAINT FK_DRUG_DrugTreatment_treatments_ID FOREIGN KEY (treatments_ID) REFERENCES TREATMENT (ID)
ALTER TABLE DRUG_DrugTreatment ADD CONSTRAINT FK_DRUG_DrugTreatment_Drug_ID FOREIGN KEY (Drug_ID) REFERENCES DRUG (ID)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT DECIMAL(38), PRIMARY KEY (SEQ_NAME))
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)
