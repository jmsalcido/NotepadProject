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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

/**
 * NotePadGPSActivity:
 * Main activity of the NotepadGPS (RememberMe)
 */
public class NotePadGPSActivity extends Activity {

	private DatabaseAdapter mDbAdapter;
	private long[] mIdNotes;
	private Note mNotes[];
	private GridView mainGrid;
	private Button mainButtonAddNote;
	private Button mainButtonShowMap;

	// DEBUG STRINGS
	private final String FIRST_TAG = "[FIRST]";
	private final String NORMAL_TAG = "[NORMAL]";
	private final String DEBUG_TAG = "[DEBUG]";
	
	// CONSTANTS
	private final int COLUMN_ID = 0;
	private final int COLUMN_TITLE = 1;
	
	private final int MAIN_BUTTON_ADD = 10;
	private final int MAIN_BUTTON_VIEW = 11;
	
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
	 * CAPTAIN, ALERT THE CREW, CLOSE ALL THE CONNECTIONS AND TRY TO GET OUT ALIVEEEE!
	 */
	protected void onDestroy() {
		Log.v("[NotePadGPS DevTeam]", "CAPTAIN, ALERT THE CREW, CLOSE ALL THE CONNECTIONS AND TRY TO GET OUT ALIVEEEE!");
		mDbAdapter.close();
		super.onDestroy();
		Log.v("[NotePadGPS DevTeam]", "FOR THOSE WHO READ THE LOG WE SALUTE YOU.");
	}

    /**
     * isFirstRun:
     * Lets see if this is the first time that our friend uses this app dont you think?
     * @return true if yes herpderp
     */
    private boolean isFirstRun() {
    	/*
    	 * TODO:
    	 * A way to know if this is the first run of the app?
    	 * Ideas?
    	 * jose152: Check if there is a database, if yes, true if no, false?
    	 */
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
		Log.v(FIRST_TAG, "Doing the first run dance");
    }
    
    /**
     * doNormalRun:
     * Get all the notes and fill the UI with them and all the other elements...
     */
    private void doNormalStartup() {
    	Log.v(NORMAL_TAG, "Doing the normal run dance");
    	try {
    		initUIElements();									// all the ui changes must be done before the data could be added.
    		fillData();											// all the ui changes must be done before the data could be added.
    	} catch (Exception e) {
    		// TODO Here we should try to mail the error if we got problems with DB and these shits...
    		Log.v(DEBUG_TAG, "Error inesperado, cerrando.");
    		e.printStackTrace();
    		finish();
    	}
    	// TODO check if the user is in a place so we can alert him
	}
    
    /**
     * 
     */
    private void initUIElements() {
    	// Graphical work here
    	mainGrid = (GridView) findViewById(R.id.mainGrid);
    	mainButtonAddNote = (Button) findViewById(R.id.mainButtonAddNote);
    	mainButtonShowMap = (Button) findViewById(R.id.mainButtonShowMap);
    	
    	// Add the listener to the UI elements
    	mainGrid.setOnItemClickListener(new GridClick());
    	mainButtonAddNote.setOnClickListener(new ClickEvents(MAIN_BUTTON_ADD));
    	mainButtonShowMap.setOnClickListener(new ClickEvents(MAIN_BUTTON_VIEW));
    }
    
    /**
     * fillData:
     * Fill the application data (UI)
     */
    private void fillData() {
    	// Logical work
    	Cursor notesCursor = mDbAdapter.fetchAllNotes();
    	//startManagingCursor(notesCursor);
    	mNotes = new Note[notesCursor.getCount()];
    	String titles[] = new String[mNotes.length];
    	notesCursor.moveToFirst();
    	
    	// get titles of the notes
    	for(int i = 0; i < mNotes.length ; i++) {
    		mNotes[i] = new Note(notesCursor.getLong(COLUMN_ID), notesCursor.getString(COLUMN_TITLE));
    		notesCursor.moveToNext();
    		titles[i] = mNotes[i].getTitle();
    		Log.v("Nota " + i, titles[i]);
    	}
    	
    	// TODO This is some graphical work, actually working with GridView, should move it?
    	mainGrid.setAdapter(new NotesGUIAdapter(this, titles));
    }
    
    /**
     * TODO REMOVE THIS
     * testAddNotes:
     * Testing the mDbAdapter class creating test notes in the DB.
     * MUST, DELETE THIS ON RELEASE
     */
    private void testAddNotes(boolean addTestNotes) {
		//if (mDbAdapter.fetchAllNotes().getCount() == 0) {
			int random = (int)(Math.random() * 5);
			Log.v("NUMERO DE NOTAS A AGREGAR", random + "");
			mIdNotes = new long[random];
			for (int i = 0; i < random; i++) {
				long id = mDbAdapter.createNote("test" + i, "nota" + i);
				mIdNotes[i] = id;
				Log.v("ADDED", "Note ID: " + id);
			}
		/*} else
			Log.v("pruebin", "not adding because there are moar notes already");*/
	}
    
    /**
     * GridClick:
     * Manage the event of item click in the grid elements.
     */
    private class GridClick implements OnItemClickListener {
    	/**
    	 * onItemClick():
    	 * It must show the note selected in the grid in a new activity.
    	 */
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO everything!
    		if (mDbAdapter == null) {
    			return;
    		} else {
    			mDbAdapter.deleteNote(mNotes[position].getId());
    			Log.v(DEBUG_TAG, "Eliminada la nota: " + mNotes[position].getId());
    			fillData();
    		}
		}
    }
    
    /**
     * ClickEvents:
     * Manage the event of clicks in the UI elements
     */
    private class ClickEvents implements OnClickListener {
    	private int behaviour;
    	
    	/**
    	 * ClickEvents(int behaviour):
    	 * Select the behaviour of the listener.
    	 * @param behaviour
    	 */
    	public ClickEvents(int behaviour) {
    		this.behaviour = behaviour;
    	}

    	/**
    	 * onClick(View parent):
    	 * Manage the event by behaviour.
    	 */
		public void onClick(View parent) {
			// TODO Auto-generated method stub
			switch(behaviour) {
			case MAIN_BUTTON_ADD:
				testAddNotes(true);
				fillData();
				break;
			case MAIN_BUTTON_VIEW:
				break;
			default:
				break;
			}
		}
    }
}
