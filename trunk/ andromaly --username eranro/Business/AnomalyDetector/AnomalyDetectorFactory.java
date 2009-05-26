package andromaly.main.Business.AnomalyDetector;

import andromaly.main.Business.AnomalyDetector.AnomalyDetector.KeyboardState;
import andromaly.main.Business.AnomalyDetector.AnomalyDetector.State;
import andromaly.main.Business.AnomalyDetector.ConcreteAnomalyDetectors.ClustersAD;
import andromaly.main.Business.DataContainers.Profile;

public class AnomalyDetectorFactory {
	private static AnomalyDetector _instance = null;
	private static State _state;
	private static KeyboardState _keyState;
	private static Profile _p1, _p2;
	
	private AnomalyDetectorFactory(){}
	
	public static void initialize(KeyboardState keyState, Profile p1, Profile p2){
		//state is read from the properties file
		_keyState = keyState;
		_p1 = p1;
		_p2 = p2;
	}
	
	public static AnomalyDetector getInstance(){
		if (_instance == null){
			_instance = new ClustersAD(_state, _keyState, _p1, _p2);
		}
		return _instance;
	}
}
