package andromaly.main.Business.DataContainers;

import java.io.IOException;
import java.util.Vector;



public class Profile extends Vector<DataCollection> {

	public Profile(){
		super();
	}
	
	public String toString(){
		String res = "====== Profile ======\n";
		for (DataCollection dc : this){
			res += dc.toString();
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
	

}
