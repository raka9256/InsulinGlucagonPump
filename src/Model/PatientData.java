package Model;
import java.util.ArrayList;
import java.util.List;

public class PatientData {

		List<Patient> patientList = new ArrayList<Patient>();
		
		public void savePatient(Patient patient){
			
			Patient pt = new Patient();
			pt.setName(patient.getName());
			pt.setNormal_bg_level(patient.getNormal_bg_level());
			pt.setBasal_prescribed_rate(patient.getBasal_prescribed_rate());
			pt.setDaily_basal_prescribed(patient.getDaily_basal_prescribed());
			pt.setDaily_bolus_prescribed(patient.getDaily_bolus_prescribed());
			pt.setPrescribed_glucagon(patient.getPrescribed_glucagon());
			patientList.add(pt);
			
		}
		
		
		public Patient getPatient(String name){
			for (Patient patient: patientList) {
				if(patient.getName().equalsIgnoreCase(name)){
					return patient; 
				}
			}
			return null;
		}
		
		
		/*public void addDailyPatientActivity(Patient patient,List<DailyEvents> dailyEvents){
			patient.setDailyEventsList(dailyEvents);
		}
		
		*/
		public void addPatientEvent(DailyEvents event,String name){
			Patient patient = getPatient(name);
			List<DailyEvents> eventList = new ArrayList<DailyEvents>();
			if(patient.getDailyEventsList() != null){
				eventList = patient.getDailyEventsList();
			}
			
			eventList.add(event);
			
			patient.setDailyEventsList(eventList);
			
		}
		
		public List<Patient> getAll(){
			return patientList;
		}
}
