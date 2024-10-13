package ecom.entity;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	private int cartID;
	private int customerID; //foreign key
	private int productID ;//foreign key
	private int quantity;
	private Map<Product,Integer> items;
	
//	constructor
	
	public Cart(int cartID, int customerID, int productID, int quantity) {
		super();
		this.cartID = cartID;
		this.customerID = customerID;
		this.productID = productID;
		this.quantity = quantity;
		items = new HashMap<>();
	}
	
	public Cart(int customerID, int productID, int quantity) {
		super();
		this.customerID = customerID;
		this.productID = productID;
		this.quantity = quantity;
		items = new HashMap<>();
	}
	
//	methods
	
//	to add item to cart
	public void addItem(Product product, int quantity2) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Product cannot be null and quantity must be greater than zero");
        }
        // If the product already exists in the cart, update the quantity
        if (items.containsKey(product)) {
            int existingQuantity = items.get(product);
            items.put(product, existingQuantity + quantity);
        } else {
            // Otherwise, add the product to the cart with the given quantity
            items.put(product, quantity);
        }

        System.out.println("Product added to the cart: " + product.getName() + ", Quantity: " + quantity);
	}

//	to remove item from cart
	public void removeItem(Product product) {
	
	}

//	to retrieve list of items in the cart
	public void getItems() {
	
	}
	
//	getters and setters

	public int getCartID() {
		return cartID;
	}

	public void setCartID(int cartID) {
		this.cartID = cartID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
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
		return "Cart [cartID=" + cartID + ", customerID=" + customerID + ", productID=" + productID + ", quantity="
				+ quantity + "]";
	}

}
