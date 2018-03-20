package Model;

public class EatingEvent implements DailyEvents {

	private String event_name;
	private double carbohydrate;
	private int time;
	private double BSLvl;
	
	
	
	public EatingEvent(String event_name,double carbohydrate,int time, double Bslvl){
		this.event_name = event_name;
		this.carbohydrate = carbohydrate;
		this.time = time;
		this.BSLvl = Bslvl;
				
	}
	
	
	public double getCarbohydrate() {
		return carbohydrate;
	}


	public void setCarbohydrate(double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}


	public String getEventName() {
		
		return this.event_name;
	}

	public double getGlucose() {
		// 
		return this.carbohydrate;
	}

	public int getTime() {

		return this.time;
	}

	public void setTime(int time) {
		this.time = time;

	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public void setGlucose(double glucose) {
		this.carbohydrate = glucose;
	}


	public double getBSLvl() {
		return BSLvl;
	}


	public void setBSLvl(double bSLvl) {
		BSLvl = bSLvl;
	}
	
	

}
