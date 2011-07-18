/*
 * NotesGUIAdapter.java
 * Saturday, July 16th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */
package local.pruebas.adaptadores;

import local.pruebas.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * NotesGUIAdapter:
 * Adapter for the GridView on the GUI
 * Based on the ImageAdapter from Google Android Developers GridView Tutorial.
 */
public class NotesGUIAdapter extends BaseAdapter {
	private Context mContexto;
	private String[] mNoteTitles;
	
	public NotesGUIAdapter(Context c, String[] titles) {
		mContexto = c;
		mNoteTitles = titles;
	}
	
	/**
	 * getCount:
	 * get the count of notes, basically the number of notes in the db
	 */
	public int getCount() {
		return mNoteTitles.length;
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
	 * ??
	 */
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * getView:
	 * return the ImageView of the note drawable
	 *
	 * Added this way, if someone knows a better way change it.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO test this
		View layout;
		if(convertView == null) {
			// JustMe saved the day at this point :)
			LayoutInflater layoutInflater = 
				(LayoutInflater) mContexto.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			layout = layoutInflater.inflate(R.layout.maingridlayout, null);
			TextView gridText = (TextView) layout.findViewById(R.id.gridTextView);
			gridText.setText(mNoteTitles[position]);
			// Dont change the image dinamically, it will be static content.
		} else {
			layout = convertView;
		}
		return layout;
	}
}
