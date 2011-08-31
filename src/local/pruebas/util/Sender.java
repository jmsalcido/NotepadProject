/*
 * Sender.java
 * Tuesday, August 30th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 * 
 */
package local.pruebas.util;

import local.pruebas.R;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Sender:
 * This class has the responsability to send messages, share messages and other shit.
 */
public class Sender {
	
	private String subject, body;
	private Context mContexto;

	/**
	 * Sender():
	 * Make a Sender object made by context, subject and body.
	 * @param contexto the context of the application
	 * @param subject the subject of the note
	 * @param body the body of the note
	 */
	public Sender(Context contexto, String subject, String body) {
		this.mContexto = contexto;
		this.subject = subject;
		this.body = body;
	}
	
	/**
	 * shareNote():
	 * This method will share a note on different applications that the device has installed.
	 */
	public void shareNote() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT   , body);
		try {
			String text = mContexto.getResources().getString(R.string.shareSend);
			mContexto.startActivity(Intent.createChooser(intent, text));
		} catch (ActivityNotFoundException anfe) {
			Toast.makeText(mContexto, R.string.shareNoClient, Toast.LENGTH_SHORT).show();
		}
	}
}
