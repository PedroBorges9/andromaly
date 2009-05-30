package andromaly.main.Business.DataContainers;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import andromaly.main.Business.AnomalyDetector.AnomalyDetectorConstants;
import dt.fe.MonitoredData;
import dt.fe.ParcelableDate;


public class MonitoredDataWrapper extends MonitoredData {
	
	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:SSS");

	public MonitoredDataWrapper(String name, Object value, Date endTime){
		super(name, value, new ParcelableDate(endTime), new ParcelableDate(endTime));
		fixNullValue();
	}

	public MonitoredDataWrapper(String name, Object value, ParcelableDate endTime){
		super(name, value, endTime, endTime);
		fixNullValue();
	}

	public MonitoredDataWrapper(String name, Object value, Date startTime, Date endTime){
		super(name, value, new ParcelableDate(startTime), new ParcelableDate(endTime));
		fixNullValue();
	}

	public MonitoredDataWrapper(String name, Object value, Date startTime, ParcelableDate endTime){
		super(name, value, startTime, endTime);
		fixNullValue();
	}

	public MonitoredDataWrapper(String name, Object value, ParcelableDate startTime, ParcelableDate endTime){
		super(name, value, startTime, endTime);
		fixNullValue();
	}
	
	public MonitoredDataWrapper(MonitoredData data){
		super(data.getName(), data.getValue(), data.getStartTime(), data.getEndTime());
		fixNullValue();
		//TODO do we need to copy the extra bundle also?
	}
	
	public MonitoredDataWrapper(){
		super("", 0,null,null);
	}
	
	private void fixNullValue(){
		if (_value == null){
			_value = new Double(0);
		}
	}
	
    public double compare(MonitoredDataWrapper other){
        Double val1 = this.mdToDouble();
        Double val2 = other.mdToDouble();
        if (val1 == null || val2 == null){
            return 0;
        }
        return Math.abs(val1 - val2);
        //@TODO: compare by FE values - normalize
    }

    public void aggregate(MonitoredDataWrapper other){
        Double val1 = this.mdToDouble();
        Double val2 = other.mdToDouble();
        if (val1 == null || val2 == null){
            _value = 0;
        } 
        else{
            _value = (val1 + val2)/2;
        }
    }

    public double getWeight() {
        return AnomalyDetectorConstants.getWeight(_name);
    }

    public boolean isEmpty(){
        Double val = mdToDouble();
        return (val == null || val == 0);
    }

    public void normalize(){
        Double valObj = mdToDouble();
        double val, max, min;
        
        if (valObj!=null){
            max = AnomalyDetectorConstants.getMaxVal(_name);
            min = AnomalyDetectorConstants.getMinVal(_name);
            
            val = valObj.doubleValue();
            if (val > max){ val = max; }
            if (val < min){ val = min; }
            
            val = (val - min) * 100 / max;
            _value = new Double(val);
        }
    }
	
	public void writeToOutput(java.io.Writer osw) throws IOException{
		osw.write("<MonitoredData ");
		osw.write("name=\"" + getName() + "\"");
		osw.write(" value=\"" + getValue() + "\"");
//		osw.write("<name>" + getName() + "</name>");
//		osw.write("<value>" + getValue() + "</value>");
//		osw.write("\t<startTime>" + getStartTime() + "</startTime>\n");
//		osw.write("\t<endTime>" + getEndTime() + "</endTime>\n");
		osw.write(" />\n");
		osw.flush();
	}
	
    public boolean readFromInput(XmlPullParser xpp){
			this._name = xpp.getAttributeValue(0);
			this._value = xpp.getAttributeValue(1);
			//System.out.println("created monitored data: " + this._name + ", " + this._value);
			return true;
    }
    
	
}
