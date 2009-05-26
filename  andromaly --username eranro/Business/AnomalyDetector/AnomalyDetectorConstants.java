package andromaly.main.Business.AnomalyDetector;

import java.util.Hashtable;

public class AnomalyDetectorConstants {
	
	//////////////////////////////////////////////////////////////////////////////////////
	//start of static constants
	private static final int MIN_FEATURES_TRANSITION = 2; 
	private static final int MAX_VECTORS_TRANSITION = 10;
	
	private static final double K = 0.3;
	
	private static final double[] minValues = 		{0,					0,							0,					0,						0,						0,						0,						0,					0,						0};
	private static final double[] maxValues = 		{10,				10,							10,					10,						1500,					1500,					1,						1500,				1500,					1500};
	private static final double[] weightValues =	{1,					1,							1,					1,						1,						1,						1,						1,					1,						1};
	private static final String[] namesArray = 		{"Outgoing_Calls", 	"Outgoing_Non_CL_Calls", 	"Outgoing_SMS", 	"Outgoing_Non_CL_SMS", 	"Avg_Key_Dwell_Time", 	"Avg_Key_Flight_Time", 	"Del_Key_Use_Rate", 	"Avg_Trans_To_U", 	"Avg_Trans_L_To_R", 	"Avg_Trans_R_To_L"};

	//end of static constants
	//////////////////////////////////////////////////////////////////////////////////////
	
	private static Hashtable<String, Integer> names;
	
	public static void initFeaturesConstants(){
		names = new Hashtable<String, Integer>();
		for (int i=0; i< namesArray.length; i++){
			names.put(namesArray[i], new Integer(i));		
		}
	}
	
    public static double getMinVal(String featureName){
        return minValues[names.get(featureName)];
    }

    public static double getMaxVal(String featureName){
        return maxValues[names.get(featureName)];
    }

    public static double getWeight(String featureName){
        return weightValues[names.get(featureName)];
    }

	public static double getK() {
		return K;
	}

	public static int getMinFeaturesTransition() {
		return MIN_FEATURES_TRANSITION;
	}

	public static int getMaxVectorTransition() {
		return MAX_VECTORS_TRANSITION;
	}
    
}
