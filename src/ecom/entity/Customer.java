package ecom.entity;

public class Customer {
	private int customerID;
	private String name;
	private String email;
	private String password;
	
//	constructor
	
	public Customer(int customerID, String name, String email, String password) {
		super();
		this.customerID = customerID;
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
		return "Customer [customerID=" + customerID + ", name=" + name + ", email=" + email + ", password=" + password
				+ "]";
	}
}