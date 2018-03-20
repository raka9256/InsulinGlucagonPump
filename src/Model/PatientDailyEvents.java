package Model;

public class PatientDailyEvents extends Patient{

	private int minutes ;
	private String event_name;
	private double carboHydrate_unit;
	

	
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
	
	public void setTime(String time){
		this.minutes = SimulatorUtility.formatTimeInMinutes(time);
	}
	
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public double getCarboHydrate_unit() {
		return carboHydrate_unit;
	}
	
	
	
	public void setBurningCarbohydrate_unit(double carbohydrate_burned){
		this.carboHydrate_unit = -carbohydrate_burned;
	}
	
	public void setIncreaseCarbohydrate_unit(double carbohydrate_consumed){
		this.carboHydrate_unit = carbohydrate_consumed;
	}
	
	
	
}
