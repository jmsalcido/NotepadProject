/*
 * NotepadViewNote.java
 * Tuesday, July 26th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 * 
 */
// TODO Places and all these things
package local.pruebas;

import local.pruebas.adaptadores.DatabaseAdapter;
import local.pruebas.util.Sender;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * NotepadViewNote:
 * Activity destined to view and delete notes
 */
public class NotepadViewNote extends Activity {
	
	private Context mContexto;
	private DatabaseAdapter mDbAdapter;
	private Long mRowId;
	
	// UI COMPONENTS
	private TextView noteTitleText;
	private TextView noteBodyText;
	private Button noteShowMapButton;
	
	// MENU OPTIONS VIEW/DELETE
	private final static int MENU_EDIT = 1;
	private final static int MENU_SHARE = 2;
	private final static int MENU_DELETE = 3;
	
	
	// TESTING SHIT
	private StringBuilder texto;
	
	@Override
	/**
     * onCreate:
     * Main method for the activity, you know the rest if not check developer.android.com
     */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewnote);
        mContexto = getApplicationContext();
		mDbAdapter = new DatabaseAdapter(mContexto);
		mDbAdapter.open(NotepadUtils.OPTION_NOTES_DATABASE);
		mRowId = getRowId(savedInstanceState);
		initUIElements();
		fillApp();
    }
	
	@Override
	/**
	 * onActivityResult:
	 * just fill all the data again
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillApp();
	}
	
	@Override
	/**
	 * onDestroy:
	 */
	protected void onDestroy() {
		mDbAdapter.close();
		super.onDestroy();
	}
	
	@Override
	/**
	 * onCreateOptionsMenu:
	 * this method will create the menu for us
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		int magic_number = 0; // ??, I have used 0 all the time here but I dont know why, so it is a magic number.
		menu.add(magic_number, MENU_EDIT, MENU_EDIT, R.string.noteMenuEdit);
		menu.add(magic_number, MENU_SHARE, MENU_SHARE, R.string.noteMenuShare);
		menu.add(magic_number, MENU_DELETE, MENU_DELETE, R.string.noteMenuDelete);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	/**
	 * onOptionsItemSelected:
	 * handles the menu actions
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		int MENU_OPTION = item.getItemId();
		Intent intent;
		switch(MENU_OPTION) {
		case MENU_SHARE:
			String subject = noteTitleText.getText().toString();
			String body = noteBodyText.getText().toString();
			Sender sender = new Sender(this, subject, body);
			sender.shareNote();
			return true;
		case MENU_DELETE:
			delete();
			finish();
			return true;
		case MENU_EDIT:
			intent = new Intent(mContexto, NotepadEditNote.class);
			intent.putExtra(NotepadUtils.KEY_BEHAVIOUR_NOTE, NotepadUtils.EDIT_NOTES);
			intent.putExtra(NotepadUtils.KEY_ROWID_DATABASE, mRowId);
			startActivityForResult(intent, NotepadUtils.EDIT_NOTES);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * initUIElements:
	 * Initiate all the UI Elements for the activity.
	 * @param behaviour
	 */
	private void initUIElements() {
		noteTitleText = (TextView) findViewById(R.id.noteViewTitleText);
		noteBodyText = (TextView) findViewById(R.id.noteViewBodyText);
		noteShowMapButton = (Button) findViewById(R.id.noteShowMapButton);
		noteShowMapButton.setOnClickListener(buttonClick);
		// TODO Places and all these things
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong(NotepadUtils.KEY_ROWID_DATABASE, mRowId);
		super.onSaveInstanceState(outState);
	}

	/**
	 * fillApp:
	 * Fills all the activity data: title, body and place of the note with id: mRowId.
	 */
	private void fillApp() {
		Log.v("ROWID", mRowId + "");
		if (mRowId != null) {
			Cursor note = mDbAdapter.fetchNote(mRowId);
			startManagingCursor(note);
			// TODO Places
			String body  = note.getString(note.getColumnIndexOrThrow(NotepadUtils.KEY_BODY_DATABASE));
			String title = note.getString(note.getColumnIndexOrThrow(NotepadUtils.KEY_TITLE_DATABASE));
			
			// Title hack
			String defaultNoteName = "Note";//getString(R.string.defaultNoteName);
			title = (title.equals("")) ? defaultNoteName : title;
			
			noteTitleText.setText(title);
			noteBodyText.setText(body);
			
			// Erase the cursor
			note = null;
		}
	}
	
	/**
	 * getRowId(Bundle):
	 * Return the rowId of the note (to edit or view)
	 * @param savedInstanceState
	 * @return
	 */
	private long getRowId(Bundle savedInstanceState) {
		Long rowId;
		Log.v("BUNDLESTATE", (savedInstanceState == null) + "");
		//(savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(Tools.KEY_ROWID_DATABASE);
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			rowId = extras.getLong(NotepadUtils.KEY_ROWID_DATABASE);
		} else
			rowId = (Long) savedInstanceState.getSerializable(NotepadUtils.KEY_ROWID_DATABASE);
		return (rowId != null) ? rowId : NotepadUtils.ERROR;
	}
	
	/**
	 * delete:
	 * This method will delete a note.
	 */
	private void delete() {
		mDbAdapter.deleteNote(mRowId);
	}
	
	// noteShowMapButton listener
	OnClickListener buttonClick = new OnClickListener() {
		public void onClick(View v) {
			fillText();
		}
	};
	
	// Durka Durka
	private void fillText() {
		if (texto == null) {
			texto = new StringBuilder();
			texto.append("-- Prueba --\n" +
						  "Esto no se guardara en la nota\n\n");
		}
		
		int random, palabras = (int)(Math.random() * 100);
		String[] dic = {"hur", "dur", "durka", "durka", "herp", "derp", "nigger", "fagget", "durka", "jose miguel salcido aguilar"};
		
		for (int i = 0; i < palabras; i++) {
			random = (int)(Math.random() * (dic.length-1));
			texto.append(dic[random] + " ");
		}
		
		noteBodyText.setText(texto);
	}
}
