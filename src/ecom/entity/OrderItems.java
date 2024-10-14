package ecom.entity;

public class OrderItems {
	private int orderItemID;
	private int orderID; //foreign key
	private int productID; //foreign key
	private int quantity;
	
//	constructor
	
	public OrderItems(int orderItemID, int orderID, int productID, int quantity) {
		super();
		this.orderItemID = orderItemID;
		this.orderID = orderID;
		this.productID = productID;
		this.quantity = quantity;
	}
	
//	getter and setter

	public int getOrderItemID() {
		return orderItemID;
	}

	public void setOrderItemID(int orderItemID) {
		this.orderItemID = orderItemID;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
//	toString

	@Override
	public String toString() {
		return "OrderItems [orderItemID=" + orderItemID + ", orderID=" + orderID + ", productID=" + productID
				+ ", quantity=" + quantity + "]";
	}	

}
