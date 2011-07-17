/*
 * NotepadGPS - NotesDbAdapter.java
 * Sunday, June 26th 2011
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
 * DbAdapter.class.
 * Clase de ayuda para conectar a la base de datos.
 * @author devteam
 */
public class DatabaseAdapter {
	
	public static final String KEY_ROWID = "rowid";
	public static final String KEY_NOTES_TITLE = "title";
	public static final String KEY_NOTES_BODY = "body";
	public static final String KEY_NOTES_PLACE = "place";
	
	public static final String KEY_PLACES_NAME = "name";
	public static final String KEY_PLACES_X = "posx";
	public static final String KEY_PLACES_Y = "posy";
	
	public static final int OPTION_NOTES = 1;
	public static final int OPTION_PLACES = 2;
	
	private static final String TAG = "NotepasGPSDB";

	private DatabaseHelper mHelper;
	private SQLiteDatabase mDb;
	
	private static final String DATABASE_NOTES_CREATE = "" +
			"CREATE TABLE notes(" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"title TEXT NOT NULL," +
			"body TEXT NOT NULL," +
			"place INTEGER NOT NULL);";//," +
			//"FOREIGN KEY(place) REFERENCES places(_id));";
	
	// TODO Averiguar que tipo de dato son las coordenadas en locations.
	private static final String DATABASE_PLACES_CREATE = "" +
			"CREATE TABLE places(" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"posx TEXT NOT NULL," + // TEXT?
			"posy TEXT NOT NULL);"; // TEXT?
	
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_NOTES_TABLE = "notes";
	private static final String DATABASE_PLACES_TABLE = "places";
	private static final int DATABASE_VERSION = 2;
	
	private final Context mContexto;
	
	/**
	 * DatabaseHelper.class.
	 * La clase que crea la base de datos y demas.
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
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(create);
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
	 * Constructor del adaptador.
	 * @param contexto de la aplicacion.
	 */
	public DatabaseAdapter(Context contexto) {
		this.mContexto = contexto; 
	}
	
	/**
	 * Abrir la conexion a la base de datos y seleccionar la tabla elegida.
	 * @param option que se usara, 1 para notas, 2 para lugares
	 * @return la instancia
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
		return this;
	}
	
	/**
	 * Cerrar conexion
	 */
	public void close() {
		mHelper.close();
	}
	
	/**
	 * Crear una nota con titulo y cuerpo
	 * @param titulo de la nota
	 * @param body de la nota
	 * @return ID de la nota
	 */
	// se puede eliminar...
	public long createNote(String title, String body) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOTES_TITLE, title);
		initialValues.put(KEY_NOTES_BODY, body);
		initialValues.put(KEY_NOTES_PLACE, 0);
		return mDb.insert(DATABASE_NOTES_TABLE, null, initialValues);
	}
	
	/**
	 * Crear una nota con titulo, cuerpo y lugar.
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
	 * Crear un lugar con nombre y coordenadas.
	 * @param name nombre del lugar
	 * @param x posicion x
	 * @param y posicion y
	 * @return
	 */
	// TODO: Coordenadas en el api de google maps!, String o como?!
	public long createPlace(String name, String x, String y) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PLACES_NAME, name);
		initialValues.put(KEY_PLACES_X, x);
		initialValues.put(KEY_PLACES_Y, y);
		return mDb.insert(DATABASE_PLACES_TABLE, null, initialValues);
	}
	
	/**
	 * Borrar una nota.
	 * @param rowId
	 * @return
	 */
	public boolean deleteNote(long rowId) {
		return mDb.delete(DATABASE_NOTES_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Borrar un lugar
	 * @param rowId
	 * @return
	 */
	public boolean deletePlace(long rowId) {
		return mDb.delete(DATABASE_PLACES_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Obtener todas las notas en un cursor.
	 * @return el cursor con las notas.
	 */
	public Cursor fetchAllNotes() {
		return mDb.query(DATABASE_NOTES_TABLE, 
				new String[] {KEY_ROWID, KEY_NOTES_TITLE, KEY_NOTES_BODY, KEY_NOTES_PLACE}, 
				null, null, null, null, null);
	}
	
	/**
	 * Obtener todos los lugares en un cursor.
	 * @return
	 */
	public Cursor fetchAllPlaces() {
		return mDb.query(DATABASE_PLACES_TABLE,
				new String[] {KEY_ROWID, KEY_PLACES_NAME, KEY_PLACES_X, KEY_PLACES_Y},
				null, null, null, null, null);
	}
	
	/**
	 * Obtener nota por el id dado.
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
	 * Actualizar la nota con los parametros dados.
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
	 * Actualizar un lugar con los parametros dados.
	 * @param rowId id del lugar
	 * @param name nombre del lugar
	 * @param x not sure if String
	 * @param y not sure if String
	 * @return true si se actualizo, falsi si no
	 */
	public boolean updatePlace(long rowId, String name, String x, String y) {
		ContentValues args = new ContentValues();
		args.put(KEY_PLACES_NAME, name);
		args.put(KEY_PLACES_X, x);
		args.put(KEY_PLACES_Y, y);
		return mDb.update(DATABASE_PLACES_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
