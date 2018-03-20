package Model;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import View.GUI;

/**
 * MathematicalModel class implements a model which can predict the blood
 * glucose levels of diabetes people.
 * 
 * @author Fitore Muharemi
 * @version 1.0
 * @since 2016-06-15
 */
public class MathematicalModel {

	public int glucose_level;
	public long actualTime;
	public long lastTime;
	private boolean hasEaten = true;
	private boolean tiredtime = true;
	private Calendar lastDayWeMeasured;
	Patient p = new Patient();
	int reactionTime = 0;
	
	private static final LocalTime TIRED_START_TIME = LocalTime.of(19, 0);
	private static final LocalTime TIRED_END_TIME = LocalTime.of(17, 0);
	
	private static final LocalTime BREAKFAST_START_TIME = LocalTime.of(9, 00);
	private static final LocalTime LUNCH_START_TIME = LocalTime.of(14, 43);
	private static final LocalTime DINNER_START_TIME = LocalTime.of(20,00);
	
	private LocalTime time;
	private boolean isFalling;
	private boolean isRaising;
	private boolean injectedInsulin;
	private boolean injectedGlucagon;
	int carbs=0;
	int incBSL=0;
	boolean mealTime=false;
	
	BolusCalculation bCalc = new BolusCalculation();

	public static void main(String[] args) throws InterruptedException {}

	/**
	 * 
	 */
	public MathematicalModel() {
		lastDayWeMeasured = Calendar.getInstance();
		isDiffDay();
	}


	/**
	 * This method first checks, if the system is started for the first time
	 * then it generate a random BG level. Then it checks if its meal time,
	 * since people have high level of blood glucose, and at this time the
	 * glucose_level start to increase. Then it checks if its tiredness time,
	 * when people are tired the blood glucose is low.
	 * 
	 * @param low
	 *            The minimum value of BG generated first time.
	 * @param high
	 *            The maximum value of BG generated first time.
	 * @param timeInjected
	 *            Time of insulin or glucagon injected.
	 * @param injectedGlucagon
	 *            Is glucagon injected?
	 * @param injectedInsulin
	 *            Is insulin injected?
	 * @param guiInst 
	 * @param insulin 
	 * @param battery 
	 * @param pdata 
	 * @param res 
	 * @return int The glucose level.
	 */
	public int glucoseLevelChange(int low, int high, long timeInjected, boolean injectedGlucagon,
			boolean injectedInsulin, GUI guiInst, Battery battery, InsulinReservoir insulin, PatientData pdata) {
		this.injectedGlucagon = injectedGlucagon;
		this.injectedInsulin = injectedInsulin;
		time = zoneAndTime();
		int timeInMin = time.getHour()*60+time.getMinute();
		actualTime = System.currentTimeMillis();
		p = pdata.getPatient("Patient1");

		if (glucose_level == 0) {
			lastTime = System.currentTimeMillis();
			glucose_level = random(low, high);
		} else {
			injectedInsulin=SimulatorUtility.injectedIns;
			if (isRaising || isItMealTime()) {
				//System.out.println("isRaising || isItMealTime()");
				glucose_level = raiseGradually(glucose_level, timeInjected, injectedInsulin);
				hasEaten = false;
			} else if (isFalling || isItTirednessPeriod()) {
				//System.out.println("isFalling || isItTirednessPeriod()");
				glucose_level = fallGradually(glucose_level, timeInjected, injectedGlucagon);
				tiredtime = false;
			} else if (glucose_level < 100) {
				//System.out.println("glucose_level < 100");
				glucose_level = random(glucose_level, glucose_level + 5);
			} else if (glucose_level > 200) {
				//System.out.println("glucose_level > 200");
				glucose_level = random(glucose_level - 4, glucose_level);
			} else {
				//System.out.println("in +-3");
				glucose_level = random(glucose_level - 3, glucose_level + 3);
			}
			lastTime = actualTime;
		}
		bCalc.calcBolus(p, timeInMin, glucose_level,guiInst,battery,insulin);
		System.out.println(glucose_level);
		return glucose_level;
	}

	/**
	 * If is tiredness time, then the blood glucose should decrease gradually
	 * until 10 minutes after glucagon is injected.
	 * 
	 * @param glucoseLevel
	 *            Current glucose level
	 * @param injectedTime
	 *            Time of glucagon injected, if?
	 * @param injectedGlucagon
	 *            Is glucagon injected yet or not?
	 * @return int Blood glucose level
	 */
	private int fallGradually(int glucoseLevel, long injectedTime, boolean injectedGlucagon) {
		isFalling = true;
		if (glucoseLevel > 40 && !passedTenMinutes(injectedTime, injectedGlucagon)) {
			return random(glucoseLevel - 1, glucoseLevel - 5);
		} else if (passedTenMinutes(injectedTime, injectedGlucagon)) {
			if (glucoseLevel < 90) {
				return random(glucoseLevel + 1, glucoseLevel + 5);
			} else {
				isFalling = false;
				this.injectedGlucagon = false;
			}
		} else {
			isFalling = false;
		}
		return glucoseLevel;
	}

	/**
	 * If its meal time, then the blood glocuse should increase gradually until
	 * 10 minutes after insulin is injected
	 * 
	 * @param glucoseLevel
	 *            Current blood glucose level
	 * @param injectedTime
	 *            Time of insulin injected, if?
	 * @param injectedInsulin
	 *            Is insulin injected yet or not?
	 * @return int Blood Glucose level
	 */
	private int raiseGradually(int glucoseLevel, long injectedTime, boolean injectedInsulin) {
		isRaising = true;
		//System.out.println("incBSL+carbs"+(incBSL+carbs));
		//System.out.println("glucoseLevel"+glucoseLevel);
		if (glucoseLevel < 300 && !passedTenMinutes(injectedTime, injectedInsulin)) {
			if(glucoseLevel < (incBSL+carbs)){
				return random(glucoseLevel + 1, glucoseLevel + 4);	
			}else{
				if(time.getSecond()<reactionTime){
					return glucoseLevel;
				}else{
					isRaising = false;
				}
			}
			//System.out.println("in bsl < 300");
		} else if (passedTenMinutes(injectedTime, injectedInsulin)) {
			if (glucoseLevel > 110) {
				return random(glucoseLevel - 3, glucoseLevel - 5);
			} else {
				isRaising = false;
				this.injectedInsulin = false;
				SimulatorUtility.injectedIns=false;
				mealTime=false;
			}
		} else {
			isRaising = false;
		}
		return glucoseLevel;
	}

	/**
	 * Checks if passed 10 minutes since glucagon or insulin is injected
	 * 
	 * @param injectedTime
	 *            time when insulin or glucagon are injected
	 * @param injected
	 *            Is injected or not?
	 * @return boolean true if 10 minutes passed since injected, else false
	 */
	public boolean passedTenMinutes(long injectedTime, boolean injected) {
		//assert injectedTime < System.currentTimeMillis();
		long injectedTimeInSeconds=0;
		if(injected){
			injectedTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(SimulatorUtility.injectedTime);
		}else{
			injectedTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(injectedTime);
		}
		long actualTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		if (injected && ((actualTimeInSeconds - injectedTimeInSeconds) >=20)) {
			//System.out.println("in actualTimeInSeconds - injectedTimeInSeconds");
			return true;
		}
		return false;
	}

	/**
	 * Checks the time, if it is meal time?
	 * 
	 * @return boolean Is meal time or not?
	 */
	private boolean isItMealTime() {
		int hour=time.getHour();
		int min=time.getMinute();
		int sec = time.getSecond();
		
		int calcMin = (hour*60)+min;
		
		List<DailyEvents> eventList=new ArrayList<DailyEvents>();
		eventList=p.getDailyEventsList();
		
		if (hasEaten && ((hour==BREAKFAST_START_TIME.getHour() && min==BREAKFAST_START_TIME.getMinute())) && !mealTime) {
			incBSL=glucose_level;
			mealTime=true;
			System.out.println("BREAKFAST TIME");
		}else if(hasEaten && (hour==LUNCH_START_TIME.getHour() && min==LUNCH_START_TIME.getMinute()) && !mealTime){
			incBSL=glucose_level;
			mealTime=true;
			System.out.println("LUNCH TIME");
		}else if(hasEaten && ((hour==DINNER_START_TIME.getHour() && min==DINNER_START_TIME.getMinute())) && !mealTime){
			incBSL=glucose_level;
			mealTime=true;
			System.out.println("DINNER TIME");
		}
			
		for(DailyEvents e : eventList){
			if(e.getTime()==calcMin){
				carbs=(int) e.getCarbohydrate();
				break;
			}
		}
		
		if (hasEaten && ((hour==BREAKFAST_START_TIME.getHour() && min==BREAKFAST_START_TIME.getMinute() && sec>15) 
				|| (hour==LUNCH_START_TIME.getHour() && min==LUNCH_START_TIME.getMinute() && sec>15) 
				|| (hour==DINNER_START_TIME.getHour() && min==DINNER_START_TIME.getMinute() && sec>15))) {
			reactionTime = sec+40;
			return true;
		}
		return false;
	}

	/**
	 * Checks the time, if it is tiredness time?
	 * 
	 * @return boolean Is tiredness time?
	 */
	private boolean isItTirednessPeriod() {
		if (tiredtime && time.compareTo(TIRED_START_TIME) > 0 && time.compareTo(TIRED_END_TIME) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the day changed
	 */
	private void isDiffDay() {
		Calendar currentDate = Calendar.getInstance();
		if (!(currentDate.get(Calendar.DAY_OF_YEAR) == lastDayWeMeasured.get(Calendar.DAY_OF_YEAR))) {
			hasEaten = true;
			//tiredtime = true;
		}
	}

	/**
	 * Get the time
	 * 
	 * @return LocalTime
	 */
	private static LocalTime zoneAndTime() {
		ZoneId zone = ZoneId.of("Europe/Berlin");
		LocalTime time = ZonedDateTime.now(zone).toLocalTime();
		return time;
	}

	/**
	 * Generate a random number
	 * 
	 * @param low
	 *            min level
	 * @param high
	 *            max level
	 * @return int the random number between low and high
	 */
	public static int random(int low, int high) {
		Random xrandom = new Random();
		int range = high - low;
		double xrange = xrandom.nextDouble() * range;
		int level = (int) (xrange + low);
		return level;
	}
}