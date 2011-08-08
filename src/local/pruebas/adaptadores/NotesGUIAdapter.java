/*
 * NotesGUIAdapter.java
 * Saturday, July 16th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */
package local.pruebas.adaptadores;

import local.pruebas.NotepadUtils;
import local.pruebas.R;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * NotesGUIAdapter:
 * Adapter for the GridView on the GUI
 * Based on the ImageAdapter from Google Android Developers GridView Tutorial.
 */
public class NotesGUIAdapter extends BaseAdapter {
	private Context mContexto;
	private Long[] mIds;
	private String[] mTitles;
	private int mCount;
	
	public NotesGUIAdapter(Context context, Cursor notes) {
		mContexto = context;
		// PRUEBA
		fillData(notes);
	}
	
	/**
	 * fillData:
	 * fill all the data needed by the adapter
	 * WHY: reduce ram usage a-lot (at least on my phone)
	 * @param notes
	 */
	private void fillData(Cursor notes) {
		// Must be on first always
		notes.moveToFirst();
		mCount = notes.getCount();
		mIds = new Long[mCount];
		mTitles = new String[mCount];
		
		for (int i = 0; i < mCount; i++) {
			mIds[i] = notes.getLong(notes.getColumnIndexOrThrow(NotepadUtils.KEY_ROWID_DATABASE));
			mTitles[i] = notes.getString(notes.getColumnIndexOrThrow(NotepadUtils.KEY_TITLE_DATABASE));
			notes.moveToNext();
		}
	}
	
	/**
	 * getCount:
	 * get the count of notes, basically the number of notes in the db
	 */
	public int getCount() {
		return mCount;
	}
	
	/**
	 * getItem:
	 * ??
	 */
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * getItemId:
	 * return the id of a position
	 */
	public long getItemId(int position) {
		return mIds[position];
	}

	/**
	 * getView:
	 * return the ImageView of the note drawable
	 *
	 * Added this way, if someone knows a better way change it.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// Create a View resource to use and return
		View layout;
		
		// Less usage of resources
		if(mCount == position) {
			mTitles = null;
		}
		
		// Check if the old view is null
		if(convertView == null) {
			// Create the LayoutInflater
			LayoutInflater layoutInflater = 
				(LayoutInflater) mContexto.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			layout = layoutInflater.inflate(R.layout.maingridlayout, null);
			
			TextView gridText = (TextView) layout.findViewById(R.id.gridTextView);
			
			// Check the orientation of the device and make proper changes
			int orientation = mContexto.getResources().getConfiguration().orientation;
	    	if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    		LinearLayout mainGridLinearLayout = (LinearLayout) layout.findViewById(R.id.mainGridLinearLayout);
	    		mainGridLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
	    		gridText = setMaxLength(gridText);
	    	} else {
	    		// DO NOTHING.
	    	}
	    	// Set the text as the title
			String title = getTitle(position);
			gridText.setText(title);
		} else {
			layout = convertView;
		}
		return layout;
	}
	
	private TextView setMaxLength(TextView gridText) {
		// WHY THE MAGIC NUMBERS?
		// Welp: InputFilter length 1, this could be added in notepadutils... would be fit better? i dont know.
		//
		// WHY NULL?
		// Welp: Fucking memory usage is, emm, HUGE!.
		InputFilter[] maxLength = new InputFilter[1];
		maxLength[maxLength.length-1] = new InputFilter.LengthFilter(NotepadUtils.MAX_CHARACTERS_HORIZONTAL);
		gridText.setFilters(maxLength);
		maxLength = null;
		return gridText;
	}
	
	/**
	 * getCursorTitle:
	 * get
	 * @param position
	 * @return
	 */
	private String getTitle(int position) {
		return mTitles[position];
	}
}
