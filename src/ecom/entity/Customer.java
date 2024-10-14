package ecom.entity;

public class Customer {
	private int customerID;
	private String name;
	private String email;
	private String password;
	
//	constructor
	
	// for insertion
	public Customer(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	//for retrieval
	public Customer(int customerId,String name, String email, String password) {
		this.customerID = customerId;
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
//	getter and setter

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
//	toString
	@Override
	public String toString() {
		return "customerID=" + customerID + ", name=" + name + ", email=" + email + ", password=" + password;
	}
}