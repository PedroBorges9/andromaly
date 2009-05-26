package andromaly.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import andromaly.main.Business.DataContainers.DataCollection;
import dt.fe.FeatureExtractor;
import dt.fe.FeatureManager;
import dt.fe.MonitoredData;
import dt.fe.ParcelableDate;
import dt.fe.extractors.localserver.LocalServerConn;

public class FeatureManagerWrapper {

	private static FeatureManagerWrapper _instance;
	private static Context _context;
	private Collection<FeatureExtractor> _extractors;
	
	public static void setContext(Context context){
		_context = context;
	}
	
	public static FeatureManagerWrapper getInstance(){
		//if no context -> error
		if (_context == null){
			System.err.println("context is null while trying to create FeaturesManagerWrapper. Run setContext(...) first.");
			return null;
		}
		
		//return a singleton instance
		if (_instance == null){
			_instance = new FeatureManagerWrapper(_context);
		}
		return _instance;
	}
	
	private FeatureManagerWrapper(Context context) {
		// init local server file
		try {
			LocalServerConn.initialize(context, R.raw.local_server);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error while loading local_server");
			_extractors = null;
		}


	}
	
	public void initialize(){
		// init feature extractors
		_extractors = initFeatureExctrators(_context);
	}

	private Collection<FeatureExtractor> initFeatureExctrators(Context context) {
		// int configFeaturesResourceId - A reference to the features xml config
		// file
		// boolean isBinary - false if config_features.xml is located under
		// res\raw\
		// true if config_features.xml is located under res\xml\
		FeatureManager.initialize(context, R.raw.config_features, false);

		// init all feature extractors
		Collection<String> errors = new ArrayList<String>();
		Collection<FeatureExtractor> extractors = FeatureManager
				.getFeatureManager().initFeatureExtractors(errors);

		// handling errors if any
		if (errors.size() != 0) {
			for (String error : errors) {
				System.err.println(error);
			}
			return null;
		}

		// in case no extractors were created
		if (extractors.isEmpty()) {
			System.err.println("No extractors were created");
			return null;
		}

		return extractors;
	}

	/**
	 * Performs the first part of the service thread iteration, the feature
	 * collection. Upon successful collection it will return the collected
	 * features, otherwise null
	 * 
	 * @return The collected features upon successful collection, otherwise null
	 */
	public DataCollection collectFeatures() {
		List<MonitoredData> data;
		ParcelableDate endTime;

		//for safety if extractors init didn't work well
		if (_extractors == null) {
			System.err.println("No executors are available.");
			return null;
		}

		data = new ArrayList<MonitoredData>();
		endTime = new ParcelableDate(System.currentTimeMillis());

		//retrieving monitored data from all executors and adding them to result list
		for (FeatureExtractor extractor : _extractors) {
			extractor.collectData(data, endTime);
		}

		return new DataCollection(data);
	}
	
	public void stopFeatureExctrators(){
		FeatureManager.tearDown();
	}

}
