package andromaly.main.Business.AnomalyDetector;

import andromaly.main.Business.DataContainers.DataCollection;
import andromaly.main.Business.DataContainers.MonitoredDataWrapper;
import andromaly.main.Business.DataContainers.Profile;
import andromaly.main.Persistance.Configurations;

public abstract class AnomalyDetector {

	private KeyboardState keyState;
	private Profile profile1, profile2; // 1 for OPEN; 2 for CLOSE
	protected Profile currentProfile;
	
	private int vectorsCount = 0;
	private int[] featuresCount;

	public enum State {
		Learning, Monitoring, Research
	};

	public enum KeyboardState {
		Open, Close
	};

	public AnomalyDetector(KeyboardState keyState, Profile p1, Profile p2) {
		this.keyState = keyState;
		profile1 = p1;
		profile2 = p2;
		setProfile(this.keyState);
	}

	public double reciveMonitoredData(DataCollection data) {
		
		// normalization
		for (MonitoredDataWrapper featureData : data) {
			featureData.normalize();
		}
		
		switch (Configurations.state) {
		case Learning: {
			updateProfile(data);
			checkTransition(data);
			return 0;
		}
		case Monitoring: {
			return detectAnomaly(data);
		}
		case Research: {
			saveVector(data);
			return 0;
		}
		default:
			return -1;
		}
	}

	private void checkTransition(DataCollection data) {
		vectorsCount++;
		System.out.print(",#" + vectorsCount + ",");
		//System.out.println("vectorsCount = " + vectorsCount);
		// if num of collected vectors reached max limit -> switch to monitoring
		if (vectorsCount >= AnomalyDetectorConstants.getMaxVectorTransition()) {
			System.out.println("\nReached MAX_VECTORS_TRANSITION");
			setState(State.Monitoring);
			return;
		}

		// init features count with respect to the 1st dataCollection size
		if (featuresCount == null) {
			featuresCount = new int[data.size()];
		}
		
		//summing and checking features count
		boolean allOverMinimum = true;
		//System.out.println("data size = " + data.size());
		for (int i = 0; i < data.size(); i++) {
			if (!(data.get(i).isEmpty())) {
				featuresCount[i]++;
			}
			if (allOverMinimum && (featuresCount[i] < AnomalyDetectorConstants.getMinFeaturesTransition())){
				allOverMinimum = false;
			}
		}
		if (allOverMinimum){
			System.out.println("\nAll features reached MIN_FEATURES_TRANSITION");
			setState(State.Monitoring);
			return;
		}
	}

	protected abstract double detectAnomaly(DataCollection data);

	protected abstract void updateProfile(DataCollection data);

	protected abstract void saveVector(DataCollection data);

	private void setProfile(KeyboardState profileNumber) {
		switch (profileNumber) {
		case Open: {
			currentProfile = profile1;
			keyState = KeyboardState.Open;
			break;
		}
		case Close: {
			currentProfile = profile2;
			keyState = KeyboardState.Close;
			break;
		}
		default:
			System.out.println("In setProfile : profileNumber is not valid");
		}
	}

	public int numOfClusters() {
		return this.currentProfile.size();
	}

	public void setState(State s) {
		Configurations.state = s;
		
		//init learning-to-monitoring-auto-switch variables
		if (s.equals(State.Learning)){
			vectorsCount = 0;
			featuresCount = null;
		}
		else if (s.equals(State.Monitoring)){
			System.out.println(currentProfile);
		}
		System.out.println("Switching to " + s.toString() + " state");
	}

	public void setKeyState(KeyboardState s) {
		keyState = s;
	}
	
	public Profile getProfile1(){
		return profile1;
	}
	
	public Profile getProfile2(){
		return profile2;
	}
}
