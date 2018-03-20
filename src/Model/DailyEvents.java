package Model;

public interface DailyEvents {

	
	public String getEventName();
	public double getGlucose();
	public int getTime();
	public void setTime(int time);
	public double getBSLvl();
	public double getCarbohydrate();
	public void setBSLvl(double bsLvl);
	
}
