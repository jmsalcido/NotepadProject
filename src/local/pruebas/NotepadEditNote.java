/*
 * NotepadEditNote.java
 * Saturday, July 16th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */

package local.pruebas;

import android.app.Activity;
import android.os.Bundle;

/**
 * NotepadEditNote:
 * Activity destined to edit notes
 */
public class NotepadEditNote extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notestub);
	}
	
}
