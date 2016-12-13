CREATE KEYSPACE cs548 WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use cs548;

Drop table patient;
Drop table provider;
Drop table treatmentByPatient;
Drop table treatmentByProvider;

CREATE TABLE patient(
	id uuid,
	patientId varchar,
	name varchar,
	birth date,
	PRIMARY KEY(id)
);

CREATE TABLE provider(
	providerId uuid,
	npi varchar,
	name varchar,
	specialization varchar,
	PRIMARY KEY(providerId)
);

CREATE TYPE drug(
	drugName varchar,
	dosage int
);

CREATE TYPE surgery(
	date date
);

CREATE TYPE radiology(
	dates List<date>
);


CREATE TABLE treatmentByPatient(
	treatmentId uuid,
	startDate date,
	startYear int,
	diagnosis varchar,
	patientId varchar,
	patientName varchar,
	npi varchar,
	providerName varchar,
	drug frozen<drug>,
	surgery frozen<surgery>,
	radiology frozen<radiology>,
	PRIMARY KEY(patientId, startDate)
) WITH CLUSTERING ORDER BY (startDate DESC);

CREATE TABLE treatmentByProvider(
	treatmentId uuid,
	startDate date,
	startYear int,
	diagnosis varchar,
	patientId varchar,
	patientName varchar,
	npi varchar,
	providerName varchar,
	drug frozen<drug>,
	surgery frozen<surgery>,
	radiology frozen<radiology>,
	PRIMARY KEY((npi, startYear) , startDate)
) WITH CLUSTERING ORDER BY (startDate DESC);

SELECT * FROM patient;
SELECT * FROM provider;
SELECT * FROM treatmentByPatient;
SELECT * FROM treatmentByProvider;
SELECT * FROM treatmentByPatient WHERE patientId = '301108001' ORDER BY startDate;
SELECT * FROM treatmentByProvider WHERE npi = '100102001' AND startYear=2001 ORDER BY startDate;



/*test data*/
INSERT INTO treatmentByPatient(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, drug)
VALUES(06827aeb-df01-421f-b889-948ac63de1e2, '2007-09-03', 2007, 'ccc', '301108004', 'John', '100102005', 'Bob', {drugName: 'aaa071', dosage: 6});

INSERT INTO treatmentByProvider(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, drug)
VALUES(06827aeb-df01-421f-b889-948ac63de1e2, '2007-09-03', 2007, 'ccc', '301108004', 'John', '100102005', 'Bob', {drugName: 'aaa071', dosage: 6});



INSERT INTO treatmentByPatient(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, surgery)
VALUES(9c049cbb-df01-421f-ba29-590e18274fe2, '2005-05-23', 2005, 'uuu', '301108002', 'Jane', '100102001', 'Lee', {date: '2006-02-22'});

INSERT INTO treatmentByProvider(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, surgery)
VALUES(9c049cbb-df01-421f-ba29-590e18274fe2, '2005-05-23', 2005, 'uuu', '301108002', 'Jane', '100102001', 'Lee', {date: '2006-02-22'});



INSERT INTO treatmentByPatient(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, radiology)
VALUES(06bbae56-df01-421f-b889-5f6837a6c652, '2009-05-20', 2009, 'eee', '301108005', 'Ross', '100102003', 'Julia', {dates: ['2009-07-11', '2012-04-21']});

INSERT INTO treatmentByProvider(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, radiology)
VALUES(06bbae56-df01-421f-b889-5f6837a6c652, '2009-05-20', 2009, 'eee', '301108005', 'Ross', '100102003', 'Julia', {dates: ['2009-07-11', '2012-04-21']});




/*inputted data*/
INSERT INTO patient(id, patientId, name, birth) 
VALUES(06049cbb-dfed-421f-b889-5f649a0de1e2, '310108001', 'Sean', '1990-05-25');

INSERT INTO provider(providerId, npi, name, specialization)
VALUES(06b88cbb-dfed-421f-b889-5f649a0de1e2, '100102001', 'Lee', 'surgery');




INSERT INTO treatmentByPatient(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, drug)
VALUES(06049cbb-df01-421f-b889-5f649b3de1e2, '2001-03-23', 2001, 'xxx', '301108001', 'Sean', '100102001', 'Lee', {drugName: 'aaa001', dosage: 10});

INSERT INTO treatmentByProvider(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, drug)
VALUES(06049cbb-df01-421f-b889-5f649b3de1e2, '2001-03-23', 2001, 'xxx', '301108001', 'Sean', '100102001', 'Lee', {drugName: 'aaa001', dosage: 10});



INSERT INTO treatmentByPatient(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, surgery)
VALUES(aab49cbb-df01-421f-b889-5f649b23a1e2, '2001-07-23', 2001, 'xxx', '301108001', 'Sean', '100102001', 'Lee', {date: '2001-10-11'});

INSERT INTO treatmentByProvider(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, surgery)
VALUES(aab49cbb-df01-421f-b889-5f649b23a1e2, '2001-07-23', 2001, 'xxx', '301108001', 'Sean', '100102001', 'Lee', {date: '2001-10-11'});



INSERT INTO treatmentByPatient(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, radiology)
VALUES(06049cbb-df01-421f-b889-5f635aa5e1e2, '2002-05-23', 2002, 'yyy', '301108002', 'Jane', '100102003', 'Julia', {dates: ['2002-07-11', '2006-04-21']});

INSERT INTO treatmentByProvider(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, radiology)
VALUES(06049cbb-df01-421f-b889-5f6493a5e1e2, '2002-05-23', 2002, 'yyy', '301108002', 'Jane', '100102003', 'Julia', {dates: ['2002-07-11', '2006-04-21']});



INSERT INTO treatmentByPatient(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, radiology)
VALUES(06049cbb-df01-421f-b889-5f6318274fe2, '2007-05-23', 2007, 'zzz', '301108002', 'Jane', '100102001', 'Lee', {dates: ['2008-07-11', '2009-04-21']});

INSERT INTO treatmentByProvider(treatmentId, startDate, startYear, diagnosis, patientId, patientName, npi, providerName, radiology)
VALUES(06049cbb-df01-421f-b889-5f6318274fe2, '2007-05-23', 2007, 'zzz', '301108002', 'Jane', '100102001', 'Lee', {dates: ['2008-07-11', '2009-04-21']});




