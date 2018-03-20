package Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimulatorUtility {

	public final static int BASAL_CAPACITY = 650;
	public final static int BOLUS_CAPACITY = 100;
	public final static int GLUCAGON_CAPACITY = 50;
	public final static int BATTERY_CAPACITY = 100;
	public final static int INSULIN_LOW = 80;
	public final static int INSULIN_HIGH = 135;
	
	public final static int INSULIN_CURRENT = 100;
	public final static int BATTERY_CURRENT = 100;

	public final static String STATUS_LOW = "LOW";
	public final static String STATUS_FULL = "FULL";
	public final static String STATUS_EMPTY = "EMPTY";
	public final static String STATUS_OK = "OK";
	
	public static boolean calcBolus = false;
	public static boolean injectedIns = false;
	public static boolean mailSentBat = false;
	public static boolean mailSentIns = false;
	public static long injectedTime=0;

	public static String getCurrentTime(int minutes) {

		// 24 hours format
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");

		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
		cal.set(Calendar.MINUTE, 0); // set minute in hour
		cal.set(Calendar.SECOND, 0); // set second in minute
		cal.set(Calendar.MILLISECOND, 0);

		cal.add(Calendar.MINUTE, minutes);

		String currentDate = df.format(cal.getTime());

		return currentDate;

	}

	public static int formatTimeInMinutes(String time) {

		String[] parts = time.split(":");

		String hh = parts[0];
		String mm = parts[1];

		int hr = Integer.parseInt(hh) * 60;
		int min = Integer.parseInt(mm);

		int currentTimeInMin = hr + min;
		return currentTimeInMin;
	}

	/*
	 * returns time string in format hh:mm
	 */
	public static String getTimeWithMinutes(int minutes) {

		if (minutes > 1440) {
			minutes = minutes - 1440;
		}

		String str_time = "00:00";
		int hr = 0;
		int min = 0;
		if (minutes >= 60) {
			hr = minutes / 60;
			min = minutes % 60;
		} else {
			min = minutes;
		}

		String append_z = "";
		String append_m = "";
		if (!(hr > 9)) {
			append_z = "0";
		}

		if (!(min > 9)) {
			append_m = "0";
		}
		str_time = append_z + hr + ":" + append_m + min;

		return str_time;

	}
}
