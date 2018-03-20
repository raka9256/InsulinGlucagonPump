package Model;

public interface Reservoir {

	final String STATUS_LOW = "LOW";
	final String STATUS_FULL = "FULL";
	final String STATUS_EMPTY = "EMPTY";
	final String STATUS_OK = "OK";
	
	
	public void refill();
	public double getInsulinAmount(double amount);
	public String checkStatus();
	public void setInsulin(double amount);
	public double getAvailable();
}
