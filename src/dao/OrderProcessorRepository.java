package dao;

import java.util.List;
import java.util.Map;

import entity.Customer;
import entity.Product;
import exception.CustomerNotFoundException;
import exception.OrderNotFoundException;
import exception.ProductNotFoundException;

public interface OrderProcessorRepository {

    // Method to create a new product
    boolean createProduct(Product product);

    // Method to create a new customer
    boolean createCustomer(Customer customer);

    // Method to delete a product
    boolean deleteProduct(int productId) throws ProductNotFoundException;

    // Method to delete a customer
    boolean deleteCustomer(int customerId) throws CustomerNotFoundException;

    // Method to add a product to the cart
    boolean addToCart(Customer customer, Product product, int quantity);

    // Method to remove a product from the cart
    boolean removeFromCart(Customer customer, Product product);

    // Method to list all products in the cart for a customer
    List<Product> getAllFromCart(Customer customer);

    // Method to place an order
    boolean placeOrder(Customer customer, List<Map<Product, Integer>> cartItems, String shippingAddress);

    // Method to get all orders for a customer
    List<Map<Product, Integer>> getOrdersByCustomer(int customerId) throws OrderNotFoundException;
}
