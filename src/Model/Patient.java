package Model;

import java.util.List;

public class Patient {

	private String name;
	private int normal_bg_level;
	private double current_bg_level;

	//prescribed basal rate per hour
	private double basal_prescribed_rate;
	private double daily_basal_prescribed;
	private double daily_bolus_prescribed;
	private double prescribed_glucagon;

	private List<DailyEvents> dailyEventsList;
	private List<PatientDailyEvents> patientDailyEventList;

	//PatientSensitivityFactor sensitivityFactor;

	public double getBasal_prescribed_rate() {
		return basal_prescribed_rate;
	}

	public void setBasal_prescribed_rate(double basal_prescribed_rate) {
		this.basal_prescribed_rate = basal_prescribed_rate;
	}

	public double getDaily_basal_prescribed() {
		return daily_basal_prescribed;
	}

	public void setDaily_basal_prescribed(double daily_basal_prescribed) {
		this.daily_basal_prescribed = daily_basal_prescribed;
	}

	public double getDaily_bolus_prescribed() {
		return daily_bolus_prescribed;
	}

	public void setDaily_bolus_prescribed(double daily_bolus_prescribed) {
		this.daily_bolus_prescribed = daily_bolus_prescribed;
	}

	public double getCurrent_bg_level() {
		return current_bg_level;
	}
	
	public void setCurrent_bg_level(double current_bg_level) {
		this.current_bg_level = current_bg_level;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNormal_bg_level() {
		return normal_bg_level;
	}
	
	public void setNormal_bg_level(int normal_bg_level) {
		this.normal_bg_level = normal_bg_level;
	}
	
	/*public PatientSensitivityFactor getSensitivityFactor() {
		return sensitivityFactor;
	}

	public void setSensitivityFactor(PatientSensitivityFactor sensitivityFactor) {
		this.sensitivityFactor = sensitivityFactor;
	}*/


	public List<PatientDailyEvents> getPatientDailyEventList() {
		return patientDailyEventList;
	}


	public void setPatientDailyEventList(
			List<PatientDailyEvents> patientDailyEventList) {
		this.patientDailyEventList = patientDailyEventList;
	}


	public List<DailyEvents> getDailyEventsList() {
		return dailyEventsList;
	}

	public void setDailyEventsList(List<DailyEvents> dailyEventsList) {
		this.dailyEventsList = dailyEventsList;
	}

	public double getPrescribed_glucagon() {
		return prescribed_glucagon;
	}

	public void setPrescribed_glucagon(double prescribed_glucagon) {
		this.prescribed_glucagon = prescribed_glucagon;
	}

}
