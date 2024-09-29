package model;

public class View1 {

	String id;
	String type;
	String usn;
	public View1(String id, String type, String usn) {
		this.id = id;
		this.type = type;
		this.usn = usn;
		
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUsn() {
		return usn;
	}
	public void setUsn(String usn) {
		this.usn = usn;
	}
	
	
	
}
