package model;

public class View2 {
	String id;
	String jid;
	String jname;
	int qty;
	
	public View2(String id, String jid, String jname, int qty) {
		this.id = id;
		this.jid = jid;
		this.jname = jname;
		this.qty = qty;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getJname() {
		return jname;
	}

	public void setJname(String jname) {
		this.jname = jname;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}


	


}
