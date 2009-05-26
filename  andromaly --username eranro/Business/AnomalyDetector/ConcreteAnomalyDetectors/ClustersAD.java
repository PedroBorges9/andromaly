
package andromaly.main.Business.AnomalyDetector.ConcreteAnomalyDetectors;

import andromaly.main.Business.AnomalyDetector.AnomalyDetector;
import andromaly.main.Business.DataContainers.DataCollection;
import andromaly.main.Business.DataContainers.Profile;


public class ClustersAD extends AnomalyDetector{

    private final double clustersMinDiff = 3; // the minimal difference between clusters

    public ClustersAD(State state, KeyboardState keyState, Profile p1, Profile p2){
    	super(keyState, p1, p2);
    }
    
    @Override
    protected double detectAnomaly(DataCollection data) {
        if (data.AllEmpty()){
            return 0; // no activity was made
        }

        // returns the minimal difference between the data to each cluster
        double minDiff = Double.MAX_VALUE;
        double diff;
        for(DataCollection cluster: currentProfile){
            diff = cluster.compare(data);
            if (diff < minDiff){
                minDiff = diff;
            }
        }
        return minDiff;
    }

    @Override
    protected void updateProfile(DataCollection data) {
        if (data.AllEmpty()){
            return; // no activity was made
        }

        /* finds the cluster with the minimal difference to the data & diff < clustersMinDiff
           if cluster was found -> aggregate data to the cluster
           else -> open new cluster*/
        double minDiff = Double.MAX_VALUE;
        double diff;
        DataCollection clusterFound = null;
        for(DataCollection cluster: currentProfile){
            diff = cluster.compare(data);
            if (diff < minDiff){
                minDiff = diff;
                clusterFound = cluster;
            }
        }

        if (minDiff > clustersMinDiff){ // open new cluster
            currentProfile.add(data);
        }
        else{ // agregation
        	clusterFound.aggregate(data);
        }
        
    }

    @Override
    protected void saveVector(DataCollection data) {
        if (data.AllEmpty()){
            return; // no activity was made
        }
        
        // only adds the data (vector) to the profile
        currentProfile.add(data);
    }
    
}
