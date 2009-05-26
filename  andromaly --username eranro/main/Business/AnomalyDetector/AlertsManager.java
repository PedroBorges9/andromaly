
package andromaly.main.Business.AnomalyDetector;

import andromaly.main.Business.Lock.LockFactory;
import andromaly.main.Business.Lock.LockInterface;
import andromaly.main.Persistance.Configurations;

public class AlertsManager {

    private double movingAvg;
    private LockInterface lockApp;

    public AlertsManager(){
        //allowedDeviationLimit is read from properties file
        movingAvg = 0;
        setLockApp(LockFactory.getInstance());
     }
    
    public double receiveDeviation(double deviation){
    	double k = AnomalyDetectorConstants.getK();
        double newAvg = deviation*k + movingAvg*(1-k);
        System.out.println("recieved deviation: " + deviation);
        System.out.println("new deviation avg: " + newAvg);
        if (newAvg > Configurations.allowedDeviationLimit){
           resetAvg();
           lockApp.lock();
           //return false;
        }
        else{
            movingAvg = newAvg;
            //return true;
        }
        return newAvg;
    }

    private void resetAvg(){
        movingAvg = 0;
    }

    public double getAllowedDeviationLimit() {
        return Configurations.allowedDeviationLimit;
    }

    public void setAllowedDeviationLimit(double allowedDeviationLimit) {
    	Configurations.allowedDeviationLimit = allowedDeviationLimit;
    }

    public void setLockApp(LockInterface lockApp) {
        this.lockApp = lockApp;
    }

}
