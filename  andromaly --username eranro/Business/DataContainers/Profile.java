package andromaly.main.Business.DataContainers;

import static org.xmlpull.v1.XmlPullParser.END_TAG;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;


public class Profile extends Vector<DataCollection> {

	public Profile(){
		super();
	}
	
	public String toString(){
		String res = "====== Profile ======\n";
		for (DataCollection dc : this){
			res += dc.getValuesString();
		}
		return res;
	}
	
	public void writeToOutput(java.io.Writer osw) throws IOException{
		osw.write("<Profile>\n");
		for (DataCollection dc : this){
			dc.writeToOutput(osw);
		}
		osw.write("</Profile>\n");
		osw.flush();
	}
	
	public boolean readFromInput(XmlPullParser xpp) {
		try {
			int eventType = xpp.next();
			DataCollection dc;
			
			while (eventType != END_TAG) {
				if (eventType == START_TAG) {
					String name = xpp.getName();
					if (TextUtils.isEmpty(name)) {
						continue;
					} else if (name.equalsIgnoreCase("datacollection")) {
						dc = new DataCollection();
						dc.readFromInput(xpp);
						this.add(dc);
					}
				}
				eventType = xpp.next();
			}

			return true;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	

}
