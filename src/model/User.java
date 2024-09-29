package model;

public class User{
	String usn;
	int pass;
	String role;
	
	public User(String usn, int pass, String role) {
		this.usn = usn;
		this.pass = pass;
		this.role = role;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUsn() {
		return usn;
	}
	public void setUsn(String usn) {
		this.usn = usn;
	}
	public int getPass() {
		return pass;
	}
	public void setPass(int pass) {
		this.pass = pass;
	}
	
}
