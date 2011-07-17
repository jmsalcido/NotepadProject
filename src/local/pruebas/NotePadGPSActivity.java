/*
 * NotePadGPSActivity.java
 * Monday, June 27th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */
package local.pruebas;

import local.pruebas.adaptadores.DatabaseAdapter;
import local.pruebas.adaptadores.NotesGUIAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * NotePadGPSActivity:
 * Main activity of the NotepadGPS (RememberMe)
 */
public class NotePadGPSActivity extends Activity {

	private DatabaseAdapter mDbAdapter;
	private long[] mIdNotes;
	
	// FLAGS
	private boolean DEBUG = true;

	// DEBUG STRINGS
	private final String FIRST_RUN = "[FIRST]";
	private final String NORMAL_RUN = "[NORMAL]";
	
    @Override
    /**
     * onCreate:
     * main method for the activity, you know the rest if not check developer.android.com
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mDbAdapter = new DatabaseAdapter(this);
        mDbAdapter.open(DatabaseAdapter.OPTION_NOTES);
        boolean initialConfiguration = isFirstRun();
        if (initialConfiguration) {
        	doFirstStartup();
        }
        doNormalStartup();
    }
    
	@Override
	/**
	 * onDestroy:
	 * ALERT, CLOSE ALL.
	 */
	protected void onDestroy() {
		mDbAdapter.close();
		super.onDestroy();
	}

    /**
     * isFirstRun:
     * Lets see if this is the first time that our friend uses this app dont you think?
     * @return true if yes herpderp
     */
    private boolean isFirstRun() {
    	boolean firstRun = false;
    	return firstRun;
    }
    
    /**
     * firstStartup:
     * Fill all the configuration files and maybe some kind of tutorial?
     */
    private void doFirstStartup() {
    	// TODO the startup process, like adding a place and a note tutorial or something like that?
		// if it is not needed, this will return nothing so... it is okay.
		// ALSO: Everything would be do on another activity :)
		Log.v(FIRST_RUN, "Doing the first run dance");
    }
    
    /**
     * doNormalRun:
     * Get all the notes and fill the UI with them...
     */
    private void doNormalStartup() {
    	Log.v(NORMAL_RUN, "Doing the normal run dance");
    	if (DEBUG) testAddNotes();
    	fillData();
    	//check if the user is in a place so we can alert him
	}
    
    /**
     * fillData:
     * Fill the application data (UI)
     */
    private void fillData() {
    	// Logical work
    	Cursor notesCursor = mDbAdapter.fetchAllNotes();
    	//startManagingCursor(notesCursor);
    	Note notes[] = new Note[notesCursor.getCount()];
    	String titles[] = new String[notes.length];
    	notesCursor.moveToFirst();
    	
    	// get titles of the notes
    	for(int i = 0; i < notes.length ; i++) {
    		notes[i] = new Note(notesCursor.getString(0));
    		notesCursor.moveToNext(); // magic happens here :|
    		titles[i] = notes[i].getTitle();
    		Log.v("Nota " + i, titles[i]);
    	}
    	
    	// TODO Graphical work, actually working with GridView
    	GridView mainGrid = (GridView) findViewById(R.id.mainGrid);
    	mainGrid.setAdapter(new NotesGUIAdapter(this, titles));
    	
    	// Add the listener to the mainGrid.
    	mainGrid.setOnItemClickListener(new ClickOnMainGrid());
    }
    
    /**
     * testAddNotes:
     * Testing the mDbAdapter class creating test notes in the DB.
     */
    private void testAddNotes() {
		if (mDbAdapter.fetchAllNotes().getCount() == 0) {
			int random = (int)(Math.random() * 3);
			Log.v("pruebin", random + "");
			mIdNotes = new long[random];
			for (int i = 0; i < random; i++) {
				long id = mDbAdapter.createNote("test" + i, "nota" + i);
				mIdNotes[i] = id;
				Log.v("added", "Note ID: " + id);
			}
		} else
			Log.v("pruebin", "not adding because there are moar notes already");
	}
    
    /**
     * ClickOnMainGrid:
     * Manage the event of clicks in mainGrid (notesGrid).
     */
    private class ClickOnMainGrid implements OnItemClickListener {
    	public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO the lack of ViewNote.java is disturbing.
			//Intent viewNote;
		}
    }
}
