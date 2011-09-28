/*
 * NotepadGPS - NotesDbAdapter.java
 * Sunday, June 26th 2011
 * 
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */

package local.pruebas.adaptadores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DbAdapter:
 * Helper class for database connections.
 * @author devteam
 */
public class DatabaseAdapter {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NOTES_TITLE = "title";
	public static final String KEY_NOTES_BODY = "body";
	public static final String KEY_NOTES_PLACE = "place";
	
	public static final String KEY_PLACES_NAME = "name";
	public static final String KEY_PLACES_LATITUDE = "latitude";
	public static final String KEY_PLACES_LONGITUDE = "longitude";
	
	public static final int OPTION_NOTES = 1;
	public static final int OPTION_PLACES = 2;
	
	private static final String TAG = "NotepasGPSDB";

	private DatabaseHelper mHelper;
	private SQLiteDatabase mDb;
	
	// Query to create the notes database
	private static final String DATABASE_NOTES_CREATE = "" +
			"CREATE TABLE notes(" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"title TEXT NOT NULL," +
			"body TEXT NOT NULL," +
			"place INTEGER NOT NULL);";//," +
			//"FOREIGN KEY(place) REFERENCES places(_id));";
	
	// Query to create the places database
	private static final String DATABASE_PLACES_CREATE = "" +
			"CREATE TABLE places(" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"latitude TEXT NOT NULL," + // TEXT?
			"longitude TEXT NOT NULL);"; // TEXT?
	
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_NOTES_TABLE = "notes";
	private static final String DATABASE_PLACES_TABLE = "places";
	private static final int DATABASE_VERSION = 2;
	
	private final Context mContexto;
	
	/**
	 * DatabaseHelper():
	 * Helper that creates database and updates.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper{
		private String create;
		private String table;
		
		public DatabaseHelper(Context contexto, String create, String table) {
			super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
			this.create = create;
			this.table = table;
		}
		
		@Override
		/**
		 * onCreate:
		 * this method is called when there is no database created.
		 */
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(create);
			
			// Insert the default place: none
			// TODO something
			if (table.equals(DATABASE_PLACES_TABLE)) {
				ContentValues values = createFirstValue();
				if (values == null)
					return;
				else
					db.insert(table, null, values);
			}
		}
		
		/**
		 * createFirstValue:
		 * @param table
		 * @return
		 */
		private ContentValues createFirstValue() {
			ContentValues values = new ContentValues();
			String[] defaultValue = {"None", "", ""};
			
			values.put(KEY_PLACES_NAME, defaultValue[0]);
			values.put(KEY_PLACES_LATITUDE, defaultValue[1]);
			values.put(KEY_PLACES_LONGITUDE, defaultValue[2]);
			
			return values;
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion +
					", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + table);
			onCreate(db);
		}
	}
	
	/**
	 * DatabaseAdapter():
	 * Default builder
	 * @param contexto de la aplicacion.
	 */
	public DatabaseAdapter(Context contexto) {
		this.mContexto = contexto; 
	}
	
	/**
	 * open(int option):
	 * Open the database with the option
	 * @param option used, OPTION_NOTES for notes, OPTION_PLACES for places
	 * @return this
	 * @throws SQLException
	 */
	public DatabaseAdapter open(int option) throws SQLException {
		switch(option) {
		case OPTION_NOTES:
			mHelper = new DatabaseHelper(mContexto, DATABASE_NOTES_CREATE, DATABASE_NOTES_TABLE);
			break;
		case OPTION_PLACES:
			mHelper = new DatabaseHelper(mContexto, DATABASE_PLACES_CREATE, DATABASE_PLACES_TABLE);
			break;
		default:
			break;
		}
		mDb = mHelper.getWritableDatabase();
		return this; // This is magic for me.
	}
	
	/**
	 * close():
	 * Close connections
	 */
	public void close() {
		mHelper.close();
	}
	
	/**
	 * createNote()
	 * @param titulo de la nota
	 * @param body de la nota
	 * @return ID de la nota
	 */
	// se puede eliminar...
	public long createNote(String title, String body) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOTES_TITLE, title);
		initialValues.put(KEY_NOTES_BODY, body);
		initialValues.put(KEY_NOTES_PLACE, 0); // TODO Using magic numbers, must remove?...
		return mDb.insert(DATABASE_NOTES_TABLE, null, initialValues);
	}
	
	/**
	 * createNote()
	 * @param title
	 * @param body
	 * @param place
	 * @return
	 */
	public long createNote(String title, String body, int place) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOTES_TITLE, title);
		initialValues.put(KEY_NOTES_BODY, body);
		initialValues.put(KEY_NOTES_PLACE, place);
		return mDb.insert(DATABASE_NOTES_TABLE, null, initialValues);
	}
	
	/**
	 * createPlace()
	 * @param name nombre del lugar
	 * @param x posicion x
	 * @param y posicion y
	 * @return
	 */
	// TODO: Coordenadas en el api de google maps!, String o como?!
	public long createPlace(String name, String latitude, String longitude) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PLACES_NAME, name);
		initialValues.put(KEY_PLACES_LATITUDE, latitude);
		initialValues.put(KEY_PLACES_LONGITUDE, longitude);
		return mDb.insert(DATABASE_PLACES_TABLE, null, initialValues);
	}
	
	/**
	 * createPlace
	 * @param rowId
	 * @return
	 */
	public boolean deleteNote(long rowId) {
		return mDb.delete(DATABASE_NOTES_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
	 * deletePlace
	 * @param rowId
	 * @return
	 */
	public boolean deletePlace(long rowId) {
		return mDb.delete(DATABASE_PLACES_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
	 * fetchAllNotes
	 * @return el cursor con las notas.
	 */
	public Cursor fetchAllNotes() {
		return mDb.query(DATABASE_NOTES_TABLE, 
				new String[] {KEY_ROWID, KEY_NOTES_TITLE, KEY_NOTES_BODY, KEY_NOTES_PLACE}, 
				null, null, null, null, null);
	}
	
	/**
	 * fetchAllNotesByTitle:
	 * @return el cursor con las notas
	 */
	public Cursor fetchAllNotesByTitle() {
		return mDb.query(DATABASE_NOTES_TABLE,new String[] {KEY_ROWID, KEY_NOTES_TITLE}, null, null, null, null, null);
	}
	
	/**
	 * fetchAllNotesById:
	 * @return el cursor con las notas
	 */
	public Cursor fetchAllNotesById() {
		return mDb.query(DATABASE_NOTES_TABLE, new String[] {KEY_ROWID}, null, null, null, null, null);
	}
	
	/**
	 * fetchAllPlaces
	 * @return
	 */
	public Cursor fetchAllPlaces() {
		return mDb.query(DATABASE_PLACES_TABLE,
				new String[] {KEY_ROWID, KEY_PLACES_NAME, KEY_PLACES_LATITUDE, KEY_PLACES_LONGITUDE},
				null, null, null, null, null);
	}
	
	/**
	 * fetchNote(long rowId)
	 * @param rowId de la nota a sacar.
	 * @return el cursor con la nota.
	 */
	public Cursor fetchNote(long rowId) {
		Cursor mCursor = mDb.query(true, DATABASE_NOTES_TABLE, 
				new String[] {KEY_ROWID, KEY_NOTES_TITLE, KEY_NOTES_BODY, KEY_NOTES_PLACE},
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if(mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}

	/**
	 * 
	 * @param rowId
	 * @param title
	 * @param body
	 * @return
	 */
	public boolean updateNote(long rowId, String title, String body) {
		ContentValues args = new ContentValues();
		args.put(KEY_NOTES_TITLE, title);
		args.put(KEY_NOTES_BODY, body);
		return mDb.update(DATABASE_NOTES_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
	 * updateNote()
	 * @param rowId id de la nota
	 * @param title titulo nuevo
	 * @param body cuerpo nuevo
	 * @return true si se actualizo, falso si no
	 */
	public boolean updateNote(long rowId, String title, String body, int place) {
		ContentValues args = new ContentValues();
		args.put(KEY_NOTES_TITLE, title);
		args.put(KEY_NOTES_BODY, body);
		args.put(KEY_NOTES_PLACE, place);
		return mDb.update(DATABASE_NOTES_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
	 * updatePlace()
	 * @param rowId id del lugar
	 * @param name nombre del lugar
	 * @param x not sure if String
	 * @param y not sure if String
	 * @return true si se actualizo, falsi si no
	 */
	public boolean updatePlace(long rowId, String name, String x, String y) {
		ContentValues args = new ContentValues();
		args.put(KEY_PLACES_NAME, name);
		args.put(KEY_PLACES_LATITUDE, x);
		args.put(KEY_PLACES_LONGITUDE, y);
		return mDb.update(DATABASE_PLACES_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
