//package andromaly.main.Persistance;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//import java.util.Properties;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.xml.sax.SAXException;
//
//import android.app.Activity;
//import android.os.Environment;
//import android.util.Log;
//import andromaly.main.Business.Agent;
//import andromaly.main.Business.AnomalyDetector.AnomalyDetector.State;
//import andromaly.main.Business.DataContainers.DataCollection;
//import andromaly.main.Business.DataContainers.Profile;
//
//public class FilesHandler_old {
//
//	private static final String PROPERTIES_FILE_NAME = "configurations.txt";
//	private static final String PROFILES_FILE_NAME = "profiles.xml";
//	
//	private static Agent agent;
//	
//	private static FilesHandler_old _instance;
//
//	public static FilesHandler_old getInstance(){
//		if (_instance == null){
//			_instance = new FilesHandler_old();
//		}
//		return _instance;
//	}
//	
//	private FilesHandler_old() {
//	}
//	
//	public void setAgent(Agent agent) {
//		this.agent = agent;
//	}
//
//	/**
//	 * read all values from properties file into static variables in matching classes
//	 * @return true if read ok, false otherwise
//	 */
//	public boolean readConfigurations() {
//		// loading file to hashtable
//		Properties pfile = loadPropertiesFile();
//
//		if (pfile == null) {
//			return false;
//		}
//		//read all properties from file and distribute them to the appropriate objects
//		
//		//anomaly detector properties
//		Configurations.state = State.valueOf(pfile.getProperty("state"));
//		
//		//alerts manager properties
//		Configurations.allowedDeviationLimit = Double.valueOf(pfile.getProperty("allowed_deviation_limit"));
//		
//		//agent properties
//		Configurations.agentLoopTime = Long.valueOf(pfile.getProperty("agent_loop_time_in_ms"));
//	
//		return true;
//	}
//	
//	public boolean writeConfigurations(){
//		//creating a new properties instance
//		Properties newProperties = new Properties();
//		
//		//collecting properties from all objects
//
//		//anomaly detector properties
//		newProperties.put("state", Configurations.state);
//		
//		//alerts manager properties
//		newProperties.put("allowed_deviation_limit", Configurations.allowedDeviationLimit);
//		
//		//agent properties
//		newProperties.put("agent_loop_time_in_ms", Configurations.agentLoopTime);
//		
//		return storePropertiesFile(newProperties);
//	}
//
//	/**
//	 * write(override if already exist) a single property into the properties file
//	 * @param property name
//	 * @param property value
//	 * @return true if write ok, false otherwise
//	 */
//	private boolean writeSingleConfiguration(String pName, String pVal) {
//		// loading file to hashtable
//		Properties pfile = loadPropertiesFile();
//		
//		if (pfile == null){
//			return false;
//		}
//
//		// changing single property
//		pfile.put(pName, pVal);
//
//		// writing hashtable to file
//		storePropertiesFile(pfile);
//		
//		return true;
//	}
//
//	/**
//	 * loads the properties file to java.util.Properties instance
//	 * @return java.util.Properties instance which represents the properties file. null if err in properties file
//	 */
//	private Properties loadPropertiesFile() {
//		Properties pfile = new Properties();
//		try {
//			pfile.load(agent.getContext().getResources().openRawResource(andromaly.main.R.raw.configurations));
//		} catch (IOException e) {
//			System.err.println("Error while reading properties file");
//			return null;
//		}
//		return pfile;
//	}
//
//	/**
//	 * store the java.util.Properties insance in the properties file
//	 * @param pfile the java.util.Properties instance to store
//	 * @return true if write ok, false otherwise
//	 */
//	private boolean storePropertiesFile(Properties pfile) {
//		try {
//			pfile.store(new FileOutputStream(_instance.PROPERTIES_FILE_NAME), "Andromaly properties file");
//		} catch (FileNotFoundException e) {
//			System.err.println("File " + _instance.PROPERTIES_FILE_NAME + " not found.");
//			return false;
//		} catch (IOException e) {
//			System.err.println("Error while writing properties file");
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * rerutns a singleton of a dom document
//	 * @return dom document
//	 * @throws ParserConfigurationException
//	 */
//	public DocumentBuilder getNewDocomentBuilder() throws ParserConfigurationException {
//			// get an instance of factory
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			
////          df.setValidating(true);
////          df.setNamespaceAware(true);
////          XMLErrorHandler errorHandler = new XMLErrorHandler();
////          db.setErrorHandler(errorHandler);
//			
//			// get an instance of builder
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			return db;
//			
//			// create an instance of DOM
//			//return db.newDocument();
//	}
//
//
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
//		    	return false;
//		    }
//		} catch (IOException e) {
//		    e.printStackTrace();
//			System.err.println("Error while exporting profiles");
//		    return false;
//		}
//	}
//
//	public boolean backupProfiles(Activity activity, Profile p1, Profile p2) {
//		Log.v("FilesHandler", "backing-up profiles to " + PROFILES_FILE_NAME);
//		try {
//			OutputStreamWriter osw = openFileOnDataDir(activity, PROFILES_FILE_NAME, Activity.MODE_PRIVATE);
//
//			osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
//			osw.write("<Andromaly_profiles>\n");
//
//			p1.writeToOutput(osw);
//
//			p2.writeToOutput(osw);
//
//			osw.write("</Andromaly_profiles>\n");
//			osw.flush();
//
//			closeFileOnDataDir(osw);
//			return true;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//	
//	private OutputStreamWriter openFileOnDataDir(Activity a, String fileName, int privacyMode) {
//		FileOutputStream fOut = null;
//		OutputStreamWriter osw = null;
//
//		try {
//			fOut = a.openFileOutput(fileName, privacyMode);
//			osw = new OutputStreamWriter(fOut);
//			return osw;
//		} catch (Exception e) {
//			e.printStackTrace();
//			try {
//				osw.close();
//				fOut.close();
//				return null;
//			} catch (IOException e2) {
//				e2.printStackTrace();
//				return null;
//			}
//		}
//	}
//	
//	private boolean closeFileOnDataDir(OutputStreamWriter osw) {
//		try {
//			osw.close();
//			// fOut.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//	
////	public boolean readProfiles(Profile p1 , Profile p2){
////		if (p1 == null || p2 == null){
////			System.err.println("Error. readProfiles got null profiles");
////			return false;
////		}
////		p1.clear();
////		p2.clear();
////		try {
////            //creating doc from file
////            DocumentBuilder db = getNewDocomentBuilder();
////            //File f = new File(fileName);
////            
////            Document doc = db.parse(agent.getContext().getResources().openRawResource(andromaly.main.R.raw.profiles));
////          
////            //getting profiles nodes from doc
////            Node root = doc.getFirstChild().getNextSibling();
////            ArrayList<Node> nodes = getRelevantChildNodes(root);
////            
//////            System.out.println(doc.getNodeName());
//////            System.out.println(root.getNodeName());
//////            System.out.println(nodes.size());
////            
////            //adding profile1 nodes to p1
////            ArrayList<Node> p1Nodes = getRelevantChildNodes(nodes.get(0));
////            for (Node n : p1Nodes){
//////            	p1.add(new DataCollection(n));
////            }
////            
////            //adding profile2 nodes to p2
////            ArrayList<Node> p2Nodes = getRelevantChildNodes(nodes.get(1));
////            for (Node n : p2Nodes){
//////            	p2.add(new DataCollection(n));
////            }
////            
////            //System.out.println(p1);
////            //System.out.println(p2);
////		}
////        catch (ParserConfigurationException ex) {
////        	System.err.println("Parser exception while reading profiles");
////        	ex.printStackTrace();
////			return false;
////        } catch (SAXException e) {
////        	System.err.println("SAX exception while reading profiles");
////        	e.printStackTrace();
////			return false;
////		} catch (IOException e) {
////			System.err.println("IO exception while reading profiles");
////			e.printStackTrace();
////			return false;
////		}
////        return true;
////	}
//	
//	public ArrayList<Node> getRelevantChildNodes(Node n){
//		ArrayList<Node> res = new ArrayList<Node>();
//		if (n.getFirstChild() == null || n.getFirstChild().getNextSibling() == null){
//			return res;
//		}
//		n = n.getFirstChild().getNextSibling();
//        while (n != null){
//        	res.add(n);
//        	n = n.getNextSibling().getNextSibling();
//        }
//        return res;
//	}
//
//}