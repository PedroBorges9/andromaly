package andromaly.main.Persistance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import andromaly.main.Business.Agent;
import andromaly.main.Business.AnomalyDetector.AnomalyDetector.State;
import andromaly.main.Business.DataContainers.DataCollection;
import andromaly.main.Business.DataContainers.Profile;

public class FilesHandler {

	private static final String PROPERTIES_FILE_NAME = "configurations.txt";
	private static final String PROFILES_FILE_NAME = "profiles.xml";
	
	private static Agent agent;
	
	private static FilesHandler _instance;

	public static FilesHandler getInstance(){
		if (_instance == null){
			_instance = new FilesHandler();
		}
		return _instance;
	}
	
	private FilesHandler() {
	}
	
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	/**
	 * read all values from properties file into static variables in matching classes
	 * @return true if read ok, false otherwise
	 */
	public boolean readConfigurations() {
		// loading file to hashtable
		Properties pfile = loadPropertiesFile();

		if (pfile == null) {
			return false;
		}
		//read all properties from file and distribute them to the appropriate objects
		
		//anomaly detector properties
		Configurations.state = State.valueOf(pfile.getProperty("state"));
		
		//alerts manager properties
		Configurations.allowedDeviationLimit = Double.valueOf(pfile.getProperty("allowed_deviation_limit"));
		
		//agent properties
		Configurations.agentLoopTime = Long.valueOf(pfile.getProperty("agent_loop_time_in_ms"));
	
		return true;
	}
	

	private Properties loadPropertiesFile() {
		Properties pfile = new Properties();
		try {
			pfile.load(agent.getContext().getResources().openRawResource(andromaly.main.R.raw.configurations));
		} catch (IOException e) {
			System.err.println("Error while reading properties file");
			return null;
		}
		return pfile;
	}
	
	/************************************************************/
	/***** PROFILES METHODS - READ & BACKUP / EXPORT & IMPORT ***/
	/************************************************************/
	
	public void readProfiles(Profile p1, Profile p2){
		
	}

//	public boolean exportProfiles(Activity activity, Profile p1, Profile p2, String fileName){
//		Log.v("FilesHandler", "exporting profiles to " + fileName);
//		try {
//		    File root = Environment.getExternalStorageDirectory();
//		    if (root.canWrite()){
//		        File gpxfile = new File(root, fileName);
//		        FileWriter gpxwriter = new FileWriter(gpxfile);
//		        BufferedWriter out = new BufferedWriter(gpxwriter);
//		        
//		        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
//		        out.write("<Andromaly_profiles>\n");
//		        
//		        p1.writeToOutput(out);
//		        p2.writeToOutput(out);
//		        
//		        out.write("</Andromaly_profiles>\n");
//		        
//		        out.close();
//		        return false;
//		    }
//		    else{
//		    	System.err.println("Error while exporting profiles. Root is not writeable.");
//		    	return false;
//		    }
//		} catch (IOException e) {
//		    e.printStackTrace();
//			System.err.println("Error while exporting profiles");
//		    return false;
//		}
//	}

	public boolean backupProfiles(Activity activity, Profile p1, Profile p2) {
		Log.v("FilesHandler", "backing-up profiles to " + PROFILES_FILE_NAME);
		try {
			OutputStreamWriter osw = openFileOnDataDir(activity, PROFILES_FILE_NAME, Activity.MODE_PRIVATE);

			osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			osw.write("<Andromaly_profiles>\n");

			p1.writeToOutput(osw);

			p2.writeToOutput(osw);

			osw.write("</Andromaly_profiles>\n");
			osw.flush();

			closeFileOnDataDir(osw);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	public boolean readProfiles(Activity activity, Profile p1, Profile p2) {
//		Log.v("FilesHandler", "reading profiles from " + PROFILES_FILE_NAME);
//		
//	}
	
	/************************************************************/
	/************** HELPING FILES HANDLING METHIDS **************/
	/************************************************************/
	
	private OutputStreamWriter openFileOnDataDir(Activity a, String fileName, int privacyMode) {
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;

		try {
			fOut = a.openFileOutput(fileName, privacyMode);
			osw = new OutputStreamWriter(fOut);
			return osw;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				osw.close();
				fOut.close();
				return null;
			} catch (IOException e2) {
				e2.printStackTrace();
				return null;
			}
		}
	}
	
	private boolean closeFileOnDataDir(OutputStreamWriter osw) {
		try {
			osw.close();
			// fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

}