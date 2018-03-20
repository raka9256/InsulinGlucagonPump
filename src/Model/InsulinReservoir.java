package Model;

import java.util.Observable;

public class InsulinReservoir extends Observable {
	private static final Object InsulinReservoir = null;
	final String STATUS_LOW = "LOW";
	final String STATUS_FULL = "FULL";
	final String STATUS_EMPTY = "EMPTY";
	final String STATUS_OK = "OK";
	
	private int tank_capacity = 100; //units
	private int available_amount = tank_capacity  ;
	AudioPlayer02 audioplayer02 = new AudioPlayer02();
	
	public InsulinReservoir(){
		
	}
	public void refill() {
		available_amount = tank_capacity; 
		
	}

	public int getInsulinAmount(int amount) {
		available_amount = available_amount-amount;
		setChanged();
		notifyObservers(InsulinReservoir);
		return amount;
	}

	public int getAvailable(){
		if(available_amount<20 && available_amount >5){
			String song="G:\\HIS\\SEM1\\SCS\\scs\\beep-09.wav";
		    audioplayer02.playAudio(song);
		}else if(available_amount < 5 && !SimulatorUtility.mailSentIns){
			//send emails
			String[] recipients = new String[]{"jagruti.gunjal92@gmail.com"};
			String[] bccRecipients = new String[]{"raka9256@gmail.com"};
			String subject = "Patient1 pump shutdown(Insulin Reservoir is Empty)!!";
			String messageBody = "Patient1 is not changing his insulin reservoir and the system may shut down. "
					+ "Can lead to dangerous effects.";
			new MailUtil().sendMail(recipients, bccRecipients, subject, messageBody);
			SimulatorUtility.mailSentIns=true;

		}
		return this.available_amount;
	}

	public void setInsulin(int amount) {
		this.available_amount = amount;
		setChanged();
		notifyObservers(InsulinReservoir);	
	}
	



}
