/*
 * NotepadLocationHandler.java
 * Sunday, August 7th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */
package local.pruebas;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * NotepadLocationHandler:
 * Location handler for the map activity
 */
public class NotepadLocationHandler {
	
	private Context mContexto;
	
	private MapController mMapController;
	private LocationManager mLocationManager;
	private Location mLocation;
	public boolean makeUseOf;
	
	public NotepadLocationHandler(Context contexto, MapController mapController, boolean makeUseOf) {
		this.makeUseOf = makeUseOf;
		this.mContexto = contexto;
		this.mMapController = mapController;
		// Acquire a reference to the system Location Manager
		this.mLocationManager = (LocationManager) mContexto.getSystemService(Context.LOCATION_SERVICE);
		
		// Check if this will happen
		if(makeUseOf) {
			mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(mLocation == null)
				Toast.makeText(mContexto, "shit nigga", 100).show();
				mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			moveView();
		} else {
			// Nothing to do here
			return;
		}
	}
	
	public Location getLocation() {
		// POOOOOWW I got hit by an idea...
		if (makeUseOf) {
			updateLocation();
			return mLocation;
		} else {
			// Nothing to do here
			return null;
		}
	}
	
	private static final long MIN_GPS_TIME = 10000 * 60 * 2; // almost two minutes
	//private static final long MIN_NETWORK_TIME = 10000 * 30; // almost two minutes
	
	public void updateLocation() {
		// Check if GPS is enabled if not, use Network provider
		if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_GPS_TIME, 0, listener);
		} else {
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, listener);
		}
	}
	
	private void moveView() {
		moveView(mMapController);
	}
	
	private void moveView(MapController mapController) {
		int latitude = (int) (mLocation.getLatitude() * 1e6);
		int longitude = (int) (mLocation.getLongitude() * 1e6);
		GeoPoint point = new GeoPoint(latitude, longitude);
		mapController.setCenter(point);
		mapController.setZoom(15);
	}
	
	private LocationListener listener = new LocationListener() {
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProviderDisabled(String provider) {
			// Show a Toast with a message saying: "provider not enabled"
			String text = provider + " " + mContexto.getText(R.string.locationProviderNotFound);
			Toast.makeText(mContexto, text, Toast.LENGTH_SHORT).show();
		}
		
		public void onLocationChanged(Location location) {
			// Make use of new location
			mLocation = location;
			Toast.makeText(mContexto, "location detected mah nigga!", Toast.LENGTH_LONG).show();
			moveView();
			// Remove the listener and update the last know location.
			mLocationManager.removeUpdates(listener);
		}
	};
}
