package andromaly.main.Business;

import android.app.Activity;
import android.content.Context;
import andromaly.main.FeatureManagerWrapper;
import andromaly.main.Business.AnomalyDetector.AlertsManager;
import andromaly.main.Business.AnomalyDetector.AnomalyDetector;
import andromaly.main.Business.AnomalyDetector.AnomalyDetectorConstants;
import andromaly.main.Business.AnomalyDetector.AnomalyDetectorFactory;
import andromaly.main.Business.AnomalyDetector.AnomalyDetector.KeyboardState;
import andromaly.main.Business.DataContainers.DataCollection;
import andromaly.main.Business.DataContainers.Profile;
import andromaly.main.Persistance.Configurations;
import andromaly.main.Persistance.FilesHandler;

public class Agent extends Thread{
	
	private FilesHandler _filesHandler;
	private FeatureManagerWrapper _featureManager;
	private AnomalyDetector _anomalyDetector;
	private AlertsManager _alertsManager;
	private Context _context;
	private Activity _activity;
	private boolean _agentActive; //TODO add agentActive switch
	
	public Agent(Context context, Activity activity) {
		
		System.out.println("***************STARTING NEW AGENT***************");
		
		_context = context;
		_activity = activity;
		//init files handler
		_filesHandler = FilesHandler.getInstance();
		_filesHandler.setAgent(this);
		//read configurations from configuration's file
		_filesHandler.readConfigurations();
		
		//read profiles from profile's file 
		Profile profile1 = new Profile();
		Profile profile2 = new Profile();
		_filesHandler.readProfiles(_activity, profile1, profile2);

		//creating feature manager
		FeatureManagerWrapper.setContext(context);
		_featureManager = FeatureManagerWrapper.getInstance();
		_featureManager.initialize();

		//TODO listen to keyboard toggling -> change current profile

		//init features constants 
		AnomalyDetectorConstants.initFeaturesConstants();
		
		//init anomaly detector
		AnomalyDetectorFactory.initialize(getCurrentKeyboardState(_featureManager), profile1, profile2);
		_anomalyDetector = AnomalyDetectorFactory.getInstance();
		_anomalyDetector.setState(andromaly.main.Business.AnomalyDetector.AnomalyDetector.State.Learning);
		
		//init Alerts mager
		_alertsManager = new AlertsManager();
		
		_agentActive = true;
	}
	
	private KeyboardState getCurrentKeyboardState(FeatureManagerWrapper fm){
		//TODO implement getCurrentKeyboardState
		return KeyboardState.Open;
	}

	public void run(){
		while (_agentActive){
			//collecting all features
			DataCollection data = _featureManager.collectFeatures();
			//System.out.println("finished collecting features data");

			if (data == null){
				System.out.print("Data is null)");
			}else{
				System.out.print("\n" + data.toString());
			}
			
			//calculating deviation
			double deviation = _anomalyDetector.reciveMonitoredData(data);
			System.out.print("\t=> current dev:" + deviation);
			
			//on monitoring mode -> send deviation to alerts manager
			if (deviation >0){
				double newAvg = _alertsManager.receiveDeviation(deviation);
				System.out.print("\t=> dev avg:" + newAvg);
			}
			else{
				System.out.print("\t=> recieved deviation: " + deviation);
				System.out.print("\t=> not sending to alertsManager");
			}
			
			System.out.println("");
			
			try {
				//System.out.println("going to sleep");
				Thread.sleep(Configurations.agentLoopTime);
			} catch (InterruptedException e) {
				System.err.println("error while sleeping in agent loop");
				e.printStackTrace();
			}
			
			//TODO backup profiles every several loops
			
		}

		//When exiting the loop - shutting down features extraction
		_featureManager.stopFeatureExctrators();
	}
	
	public Context getContext(){
		return _context;
	}

	/**************************
	 * Settors & Gettors
	 **************************/

	public void stopAgentLoop(){
		_agentActive = false;
	}
	
	public void backupProfiles(){
		Profile p1 = _anomalyDetector.getProfile1();
		Profile p2 = _anomalyDetector.getProfile2();
		_filesHandler.backupProfiles(_activity, p1, p2);
	}
	
//	public void exportProfiles(){
//		Profile p1 = _anomalyDetector.getProfile1();
//		Profile p2 = _anomalyDetector.getProfile2();
//		_filesHandler.exportProfiles(_activity, p1, p2, "profilesExport.xml");
//	}
	
}