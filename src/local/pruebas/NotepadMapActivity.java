/*
 * NotepadMapActivity.java
 * Saturday, August 2nd 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */
package local.pruebas;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

/**
 * NotepadMapActivity:
 * Activity for the map actions.
 */
public class NotepadMapActivity extends MapActivity {
	
	private MapView mMapView;
	private NotepadLocationHandler mLocationHandler;
	
	@Override
	/**
     * onCreate:
     * Main method for the activity, you know the rest if not check developer.android.com
     */
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.map);
		initUIElements();
		mLocationHandler = new NotepadLocationHandler(this, mMapView.getController(), true); // TODO Settings for the second option
		//moveView();
	}
	
	/**
     * initUIElements():
     * Fill the UI Elements
     */
	private void initUIElements() {
		mMapView = (MapView) findViewById(R.id.mapView);
		mMapView.setBuiltInZoomControls(true); // Im not in the mood for handle this, so... cruise control for this.
	}
	
	/*private void moveView() {
		Location location = mLocationHandler.getLocation();
		int latitude = (int) (location.getLatitude() * 1e6);
		int longitude = (int) (location.getLongitude() * 1e6);
		GeoPoint point = new GeoPoint(latitude, longitude);
		
		MapController mapController = mMapView.getController();
		mapController.setCenter(point);
		mapController.setZoom(15);
	}*/
	
	private static final int MENU_UPDATE = 0;
	private static final int MENU_RETURN = 1;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int magic_number = 0; // ??, I have used 0 all the time here but I dont know why, so it is a magic number.
		menu.add(magic_number, MENU_UPDATE, MENU_UPDATE, "UPDATE"); // TODO remove hard-code
		menu.add(magic_number, MENU_RETURN, MENU_RETURN, "CLOSE");  // TODO remove hard-code
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case MENU_UPDATE:
			mLocationHandler.updateLocation();
			return true;
		case MENU_RETURN:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
