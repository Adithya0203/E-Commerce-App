package ecom.entity;

import java.util.List;
import java.util.Map;
import java.util.Date;

public class Order {
   private int OrderId;
   private int CustomerId;
   private Date orderDate;
   private double totalPrice;
   private String shippingAddress;
   private Customer customer;
   private List<Map<Product, Integer>> cartItems;
   
   //constructor
   
   // for retrieval
public Order(int orderId, int customerId, Date orderDate, double totalPrice, String shippingAddress) {
	super();
	OrderId = orderId;
	CustomerId = customerId;
	this.orderDate = orderDate;
	this.totalPrice = totalPrice;
	this.shippingAddress = shippingAddress;
}

//for insertion
public Order(int customerId, Date orderDate, double totalPrice, String shippingAddress) {
	super();
	CustomerId = customerId;
	this.orderDate = orderDate;
	this.totalPrice = totalPrice;
	this.shippingAddress = shippingAddress;
	this.customer = customer;
	this.cartItems = cartItems;
}



public Order(Customer customer, List<Map<Product, Integer>> cartItems,String shippingAddress) {
	super();
	this.shippingAddress = shippingAddress;
	this.customer = customer;
	this.cartItems = cartItems;
}

public int getOrderId() {
	return OrderId;
}

public void setOrderId(int orderId) {
	OrderId = orderId;
}

public int getCustomerId() {
	return CustomerId;
}

public void setCustomerId(int customerId) {
	CustomerId = customerId;
}

public Date getOrderDate() {
	return orderDate;
}

public void setOrderDate(Date orderDate) {
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

public Customer getCustomer() {
	return customer;
}

public void setCustomer(Customer customer) {
	this.customer = customer;
}

public List<Map<Product, Integer>> getCartItems() {
	return cartItems;
}

public void setCartItems(List<Map<Product, Integer>> cartItems) {
	this.cartItems = cartItems;
}

@Override
public String toString() {
	return "OrderId=" + OrderId + ", CustomerId=" + CustomerId + ", orderDate=" + orderDate + ", totalPrice="
			+ totalPrice + ", shippingAddress=" + shippingAddress;
}

}