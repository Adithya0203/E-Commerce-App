package entity;

import java.util.List;
import java.util.Map;

public class Order {
    private Customer customer;
    private List<Map<Product, Integer>> cartItems; // List of products and their quantities
    private String shippingAddress;

    // Default constructor
    public Order() {}

    // Parameterized constructor
    public Order(Customer customer, List<Map<Product, Integer>> cartItems, String shippingAddress) {
        this.customer = customer;
        this.cartItems = cartItems;
        this.shippingAddress = shippingAddress;
    }

    // Getters
    public Customer getCustomer() {
        return customer;
    }

    public List<Map<Product, Integer>> getCartItems() {
        return cartItems;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }
}
