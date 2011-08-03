/*
 * NotepadMapActivity.java
 * Saturday, August 2nd 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */
package local.pruebas;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

/**
 * NotepadMapActivity:
 * Activity for the map actions.
 */
public class NotepadMapActivity extends MapActivity {
	
	private MapView mMapView;
	
	@Override
	/**
     * onCreate:
     * Main method for the activity, you know the rest if not check developer.android.com
     */
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.map);
		initUIElements();
	}
	
	/**
     * initUIElements():
     * Fill the UI Elements
     */
	private void initUIElements() {
		mMapView = (MapView) findViewById(R.id.mapView);
		mMapView.setBuiltInZoomControls(true); // Im not in the mood for handle this, so... cruise control for this.
	}



	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
