/*
 * NotepadUtils.java
 * Tuesday, July 26th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */
package local.pruebas;

import local.pruebas.adaptadores.DatabaseAdapter;

/**
 * NotepadUtils:
 * All the constants used widely the app will be here and other routines.
 */
public class NotepadUtils {
	
	// GENERAL
	public static int ERROR 							= -1;
	public static final int MAX_CHARACTERS_VERTICAL 	= 15;
	public static final int MAX_CHARACTERS_HORIZONTAL 	= 25;
	public static final String FIRST_TAG 				= "[FIRST]";
	public static final String NORMAL_TAG 				= "[NORMAL]";
	public static final String DEBUG_TAG 				= "[DEBUG]";
	
	// DATABASE
	public final static String KEY_ROWID_DATABASE 		= DatabaseAdapter.KEY_ROWID;
	public final static String KEY_TITLE_DATABASE 		= DatabaseAdapter.KEY_NOTES_TITLE;
	public final static String KEY_BODY_DATABASE 		= DatabaseAdapter.KEY_NOTES_BODY;
	public final static int OPTION_NOTES_DATABASE 		= DatabaseAdapter.OPTION_NOTES;
	public final static int OPTION_PLACES_DATABASE 		= DatabaseAdapter.OPTION_PLACES;
	
	// EDIT / VIEW NOTES
	public final static String KEY_BEHAVIOUR_NOTE 		= "behaviour";
	
	public final static int ERROR_NOTES 				= 0;
	public final static int EDIT_NOTES 					= 1;
	public final static int VIEW_NOTES 					= 2;
	public final static int DELETE_NOTES 				= 3;
	public final static int ADD_NOTE 					= 4;
}
