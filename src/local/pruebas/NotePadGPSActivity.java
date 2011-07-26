/*
 * NotePadGPSActivity.java
 * Monday, June 27th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */
// TODO Places and all these things

package local.pruebas;

import local.pruebas.adaptadores.DatabaseAdapter;
import local.pruebas.adaptadores.NotesGUIAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

/**
 * NotePadGPSActivity:
 * Main activity of the NotepadGPS (RememberMe)
 */
public class NotePadGPSActivity extends Activity {

	private Context mContexto;
	private DatabaseAdapter mDbAdapter;
	private GridView mainGrid;
	private Button mainButtonAddNote;
	private Button mainButtonShowMap;
	private long mNotesId[];
	
	// CONSTANTS
	private final int MAIN_BUTTON_ADD = 10;
	private final int MAIN_BUTTON_VIEW = 11;
	
    @Override
    /**
     * onCreate:
     * main method for the activity, you know the rest if not check developer.android.com
     * 
     * Basically: init DbAdapter, check if this is the first run and init application.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContexto = getApplicationContext();
        mDbAdapter = new DatabaseAdapter(mContexto);
        mDbAdapter.open(NotepadUtils.OPTION_NOTES_DATABASE);
        boolean initialConfiguration = isFirstRun();
        if(initialConfiguration) doFirstStartup();
        else doNormalStartup();
    }
    
	@Override
	/**
	 * onActivityResult:
	 * just fill all the data again
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}

	@Override
	/**
	 * onDestroy:
	 * CAPTAIN, ALERT THE CREW, CLOSE ALL THE CONNECTIONS AND TRY TO GET OUT ALIVEEEE!
	 */
	protected void onDestroy() {
		mDbAdapter.close();
		Log.v("[NotePadGPS DevTeam]","CAPTAIN, ALERT THE CREW, CLOSE ALL THE CONNECTIONS AND TRY TO GET OUT ALIVEEEE!");
		Log.v("[NotePadGPS DevTeam]", "FOR THOSE WHO READ THE LOG WE SALUTE YOU.");
		super.onDestroy();
	}

    /**
     * isFirstRun:
     * Lets see if this is the first time that our friend uses this app dont you think?
     * @return true if yes herpderp
     */
    private boolean isFirstRun() {
    	boolean firstRun;
    	firstRun = false; // for the moment
    	/*
    	 * TODO:
    	 * A way to know if this is the first run of the app?
    	 * Ideas?
    	 * jose152: Check if there is a database file, if yes, true if no, false?
    	 */
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
		Log.v(NotepadUtils.FIRST_TAG, "Doing the first run dance");
    }
    
    /**
     * doNormalRun:
     * Get all the notes and fill the UI with them and all the other elements...
     */
    private void doNormalStartup() {
    	Log.v(NotepadUtils.NORMAL_TAG, "Doing the normal run dance");
    	// UI work
		initUIElements();
		fillData();
    	// TODO check if the user is in a place so we can alert him
		// allmost all
	}
    
    /**
     * fillData:
     * Fill the application data (UI)
     */
    private void fillData() {
    	// Logical work
    	Cursor notes = mDbAdapter.fetchAllNotes();
    	//startManagingCursor(notes);
    	String titles[] = new String[notes.getCount()];
    	mNotesId = new long[notes.getCount()];
    	notes.moveToFirst();
    	
    	// Save the titles in an array to pass it to the adapter.
    	for(int i = 0; i < titles.length ; i++) {
    		Long id = notes.getLong(notes.getColumnIndexOrThrow(NotepadUtils.KEY_ROWID_DATABASE));
    		String title = notes.getString(notes.getColumnIndexOrThrow(NotepadUtils.KEY_TITLE_DATABASE));
    		titles[i] = fixTitle(title);
    		mNotesId[i] = id;
    		Log.v("Nota " + i, title);
    		notes.moveToNext();
    	}
    	
    	// TODO This is some graphical work, actually working with GridView, should move it?
    	mainGrid.setAdapter(new NotesGUIAdapter(this, titles));
    }
    
    /**
     * fixTitle:
     * Fixes the title in the mainGridTextView to get a cool and soft display
     * @param title
     * @return
     */
    private String fixTitle(String title) {
    	// No of characters (add 3 '.' for the total No) to display at the mainGridTextView.
    	int length = 12;
    	return (title.length() <= length) ? title : title.substring(0, length) + "...";
    }
    
    /**
     * initUIElements():
     * Fill the UI Elements
     */
    private void initUIElements() {
    	// Graphical elements
    	mainGrid = (GridView) findViewById(R.id.mainGrid);
    	mainButtonAddNote = (Button) findViewById(R.id.mainButtonAddNote);
    	mainButtonShowMap = (Button) findViewById(R.id.mainButtonShowMap);
    	
    	// Add the listener to the UI elements
    	mainGrid.setOnItemClickListener(new GridClick());
    	mainButtonAddNote.setOnClickListener(new ClickEvents(MAIN_BUTTON_ADD));
    	mainButtonShowMap.setOnClickListener(new ClickEvents(MAIN_BUTTON_VIEW));
    }
    
    /**
     * testAddNotes:
     * Testing the mDbAdapter class creating test notes in the DB.
     */
    protected void testAddNotes(boolean addTestNotes) {
		int random = (int)(Math.random() * 5);
		Log.v("NUMERO DE NOTAS A AGREGAR", random + "");
		for (int i = 0; i < random; i++) {
			long id = mDbAdapter.createNote("test" + i, "nota" + i);
			Log.v("ADDED", "Note ID: " + id);
		}
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
    		try {
    			Intent intent = new Intent(mContexto, NotepadViewNote.class);
    			intent.putExtra(NotepadUtils.KEY_BEHAVIOUR_NOTE, NotepadUtils.VIEW_NOTES);
    			intent.putExtra(NotepadUtils.KEY_ROWID_DATABASE, mNotesId[position]);
				startActivityForResult(intent, NotepadUtils.VIEW_NOTES);
    		} catch (Exception ex) {
    			return;
    		}
    	}
    }
    
    /**
     * ClickEvents:
     * Manage the event of clicks in the UI elements
     */
    private class ClickEvents implements OnClickListener {
    	private int behaviour;
    	private Intent intent;
    	
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
			switch(behaviour) {
			case MAIN_BUTTON_ADD:
				intent = new Intent(mContexto, NotepadEditNote.class);
    			intent.putExtra(NotepadUtils.KEY_BEHAVIOUR_NOTE, NotepadUtils.ADD_NOTE);
				startActivityForResult(intent, NotepadUtils.ADD_NOTE);
				break;
			case MAIN_BUTTON_VIEW:
				testAddNotes(true);
				fillData();
				break;
			default:
				Toast.makeText(mContexto, "OOPS", Toast.LENGTH_SHORT).show();
				break;
			}
		}
    }
}
