package Model;

import java.util.ArrayList;
import java.util.List;

import View.GUI;
public class BolusCalculation {
	int i=0;
	public int calculateBolusCarbs(int carbIntake, int bgLevel, Patient patient) {
		/*
		 * calculations TOTAL DAILY INSULIN DOSE = 0.55 x 70 Kg = 38.5 units of
		 * insulin/day Carbohydrate coverage ratio = 500 ÷ TDI (40 units) Assume
		 * you weigh 160 pounds Your total daily insulin dose (TDI) = 160 lbs ÷
		 * 4 = 40 units.
		 */
		int TDI = (int) Math.round(patient.getDaily_bolus_prescribed() + patient.getDaily_basal_prescribed());
		int CHO = 500 / TDI; // (1 unit of insulin for CHO)
		int insForCarb = (int) Math.round(carbIntake / CHO);
		double corrFactr = 1800 / TDI;// 1 unit insulin will drop reduce the
										// blood sugar level by corrFactr mg/dl
		int increasedBSL = bgLevel - patient.getNormal_bg_level();
		int insForCorrFactr = (int) Math.round(increasedBSL / corrFactr);
		int totalInsDosage = insForCarb + insForCorrFactr;

		return totalInsDosage;
	}
	
	public int calculateBolus(int bgLevel,Patient patient) {
		
		int TDI = (int) Math.round(patient.getDaily_bolus_prescribed() + patient.getDaily_basal_prescribed());
		double corrFactr = 1800 / TDI;// 1 unit insulin will drop reduce the
										// blood sugar level by corrFactr mg/dl
		int totalInsDosage = (int)((bgLevel - 120)/corrFactr);

		return totalInsDosage;
	}
	
	public void calcBolus(Patient p, long timeInMin,int bloodSugar,GUI gui, Battery battery, InsulinReservoir insulin)
	{
		i++;
		if(i%5==0){
			battery.getPowerAmout(1);    //level of battery is reduced by 1 each transaction
		}
		
		//below 70mg/dl is hypoglycemia and above 300 is hyperglycemia
		//90-160 normal range
		//160-240 too high
		//240-300 very high diabetes out of control
		//above 300 call doc 

		List<DailyEvents> eventList=new ArrayList<DailyEvents>();
		double carbIntake=0;
		double normalInsLvl=0;
		eventList=p.getDailyEventsList();

		for(DailyEvents e : eventList){
			if(e.getTime()==timeInMin && !SimulatorUtility.injectedIns){
				SimulatorUtility.calcBolus=true;
				carbIntake=e.getCarbohydrate();
				normalInsLvl= e.getBSLvl();
				break;
			}
		}
		//System.out.println("calc");
//comment
		if(SimulatorUtility.calcBolus && bloodSugar>SimulatorUtility.INSULIN_HIGH){
			//calculations
			//TOTAL DAILY INSULIN DOSE = 0.55 x 70 Kg = 38.5 units of insulin/day
			//Carbohydrate coverage ratio = 500 ÷ TDI (40 units) 
			//Assume you weigh 160 pounds
			//Your total daily insulin dose (TDI) = 160 lbs ÷ 4 = 40 units.
			int TDI= (int) Math.round(p.getDaily_bolus_prescribed()+p.getDaily_basal_prescribed());
			int CHO = 500 /TDI; //(1 unit of insulin for CHO)
			int insForCarb = (int) Math.round(carbIntake/CHO);
			double corrFactr = 1800/TDI;//1 unit insulin will drop reduce the blood sugar level by corrFactr mg/dl
			int increasedBSL = bloodSugar - (int)normalInsLvl;
			int insForCorrFactr = (int) Math.round(increasedBSL/corrFactr);
			int totalInsDosage = insForCarb+insForCorrFactr;
			SimulatorUtility.calcBolus=false;
			SimulatorUtility.injectedIns=true;
			SimulatorUtility.injectedTime=System.currentTimeMillis();
			System.out.println("Insulin Injected"+totalInsDosage);
			
			((GUI) gui).progressBar_Insulin.setValue(insulin.getInsulinAmount(totalInsDosage));
			battery.getPowerAmout(1);
		}
		insulin.getAvailable();//to check status
		((GUI) gui).progressBar_Battery.setValue(battery.getAvailable());
		//response.setBSL(bloodSugar);
	}
}

