/*
 * No estoy seguro si necesitaremos esta clase.
 */
package local.pruebas;

public class Note {
	private long id;
	private String title;
	private String body;
	
	public Note() {
		this.id = -1;
		this.title = "";
		this.body = "";
	}
	
	public Note(long id, String title, String body) {
		this.id = id;
		this.title = title;
		this.body = body;
	}
	
	public Note(String title) {
		this.title = title;
	}
	
	public Note(String title, String body) {
		this.title = title;
		this.body = body;
	}
	
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
}
