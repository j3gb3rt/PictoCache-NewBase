package edu.gatech.cs4261.wheresdabeef.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Date;

/**
 * Created by Kyle M.
 */
public class LocationApi {
    private static Location currLocation;
    private static LocationManager locationManager;
    private static LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (currLocation == null) {
                currLocation = location;
                return;
            }

            long timeDelta = currLocation.getTime() - location.getTime();
            boolean isNewer = timeDelta > 0;

            int accuracyDelta = (int) (currLocation.getAccuracy() - location.getAccuracy());
            boolean isLessAccurate = accuracyDelta > 0;
            boolean isMoreAccurate = accuracyDelta < 0;

            if (isMoreAccurate || (isNewer && !isLessAccurate)) {
                currLocation = location;
                return;
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }
        @Override
        public void onProviderEnabled(String s) { }
        @Override
        public void onProviderDisabled(String s) { }
    };

    public static void startPollingLocation(final Context context) {
        currLocation = null; // Erase old location

        // Set up the criteria. Criteria are loosened sequentially if no provider is found.
        Criteria criteria = new Criteria();
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setSpeedRequired(false);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String locationService = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(locationService, 0, 0, locationListener);

        currLocation = locationManager.getLastKnownLocation(locationService);

    }

    public static Location stopPollingLocation() {
        locationManager.removeUpdates(locationListener);
        if (currLocation != null) {
            return currLocation;
        }
        else {
            currLocation.setLatitude(0);
            currLocation.setAltitude(0);
            currLocation.setTime(new Date().getTime());
            return currLocation;
        }
    }
}
