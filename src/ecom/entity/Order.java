package ecom.entity;

public class Order {
	private int orderID;
	private String customerID; //Foreign key
	private String orderDate;
	private double totalPrice;
	private String shippingAddress;
	
//	constructor
	
	public Order(int orderID, String customerID, String orderDate, double totalPrice, String shippingAddress) {
		super();
		this.orderID = orderID;
		this.customerID = customerID;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.shippingAddress = shippingAddress;
	}

//	getter and setter
	
	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	
//	toString

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", customerID=" + customerID + ", orderDate=" + orderDate + ", totalPrice="
				+ totalPrice + ", shippingAddress=" + shippingAddress + "]";
	}

}
