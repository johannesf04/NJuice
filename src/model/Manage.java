package model;

public class Manage {

	String id;
	String name;
	Integer price;
	String desc;
	public Manage(String id, String name, Integer price, String desc) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.desc = desc;
		
}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
