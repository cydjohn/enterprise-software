# CS 548/SOC 542—Enterprise Software Architecture and Design
## Assignment Eleven—BPMN

<center>***Yudong Cao***</center>
<center>***10420909***</center>


> All the picture for process diagrams and project files can be found in the root folder.


## Report

![](my.png)

There are three participants, patient, clinic and hospital in the whole process. Clinic includes four departments, like front office, , pharmacy and research. Out-patient care has junior and senior clinician.The main process is `Enter` --> `Registration` --> `Initial consultation` --> `Follow-up` -->`Decision`.

### Registration sub-process

![](Registration.png)

Check whether it’s the first visit for the patient firstly. If it is, we try to get the medical history and the consent. All situations will gather in a complex gateway, then get or confirm the insurance information.

### Initial consultation sub-process

![](Consulation.png)

Making decision by physical examination, laboratory analysis or senior guidance. If there is no senior clinician in the clinic, send message to hospital and wait for the decision. If the response is timeout, refer the patient to hospital.

### Data upload sub-process

![](uploadData.png)

Check the consent in Consent agreement DB. If patient provided it, review the data in the clinical data DB. Verify whether the data is complete. If it is, upload data.