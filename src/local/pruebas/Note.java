/*
 * Note.java
 * Saturday, July 16th 2011
 *
 * Created by Jose Miguel Salcido Aguilar (jose152)
 * The brief description of the class is on Javadoc format.
 */
package local.pruebas;

/**
 * Note:
 * A normal note abstracted from the world would have title and body
 * but this one has title, body and places!!!! WOOOOOOOOW!
 */
public class Note {
	private long id;
	private String title;
	private String body;
	private int place;
	
	/**
	 * Note:
	 * Public constructor
	 */
	public Note() {
		this.id = -1;
		this.title = null;
		this.body = null;
		this.place = 0;
	}
	
	/**
	 * Note:
	 * Public constructor with params
	 * @param id
	 * @param title
	 * @param body
	 * @param place
	 */
	public Note(long id, String title, String body, int place) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.place = place;
	}
	
	/**
	 * Note:
	 * Public constructor with params
	 * @param id
	 * @param title
	 */
	public Note(long id, String title) {
		this.id = id;
		this.title = title;
		this.body = null;
	}
	
	/**
	 * Note:
	 * Public constructor with params
	 * @param title
	 */
	public Note(String title) {
		this.id = -1;
		this.title = title;
		this.body = null;
	}
	
	/**
	 * Note:
	 * Public constructor with params
	 * @param title
	 * @param body
	 */
	public Note(String title, String body) {
		this.id = -1;
		this.title = title;
		this.body = body;
		this.place = -1;
	}
	
	/*
	 * shitty default methods
	 */
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public int getPlace() {
		return place;
	}
	
	public void setPlace(int place) {
		this.place = place;
	}
}
