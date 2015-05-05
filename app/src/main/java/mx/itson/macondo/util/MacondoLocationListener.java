package mx.itson.macondo.util;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * @author ramos.isw@gmail.com
 * Created by Julio C. Ramos on 04/05/2015.
 */
public class MacondoLocationListener implements LocationListener {

    private final MacondoLcationListenerCallbacks mCallcacks;

    public MacondoLocationListener(MacondoLcationListenerCallbacks callbacks) {
        mCallcacks = callbacks;
    }


    public void onLocationChanged(Location argLocation) {
        mCallcacks.onLocationChanged(argLocation);
    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    public void onStatusChanged(String provider,
                                int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface MacondoLcationListenerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onLocationChanged(Location argLocation);
    }
}
