package dao;

import entity.Cart;
import entity.Customer;
import entity.Order;
import entity.Product;
import exception.CustomerNotFoundException;
import exception.OrderNotFoundException;
import exception.ProductNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {

    // Simulating a database with lists
    private List<Product> products = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Order> orders = new ArrayList<>(); // List to hold orders
    private Map<Customer, Cart> customerCarts = new HashMap<>(); // Map to hold customer carts

    @Override
    public boolean createProduct(Product product) {
        Connection connection = DBConnUtil.getConnection("db.properties");
        String query = "INSERT INTO products (name, price, description, stockQuantity) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getDescription());
            statement.setInt(4, product.getStockQuantity());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createCustomer(Customer customer) {
        Connection connection = DBConnUtil.getConnection("db.properties");
        String query = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPassword());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteProduct(int productId) throws ProductNotFoundException {
        boolean isDeleted = products.removeIf(product -> product.getProductId() == productId);
        if (!isDeleted) {
            throw new ProductNotFoundException("Product with ID " + productId + " not found.");
        }
        return true;
    }

    @Override
    public boolean deleteCustomer(int customerId) throws CustomerNotFoundException {
        boolean isDeleted = customers.removeIf(customer -> customer.getCustomerId() == customerId);
        if (!isDeleted) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }
        return true;
    }

    @Override
    public boolean addToCart(Customer customer, Product product, int quantity) {
        Cart cart = customerCarts.computeIfAbsent(customer, k -> new Cart());
        cart.addItem(product, quantity);
        return true;
    }

    @Override
    public boolean removeFromCart(Customer customer, Product product) {
        Cart cart = customerCarts.get(customer);
        if (cart != null) {
            cart.removeItem(product);
            return true;
        }
        return false;
    }

    @Override
    public Map<Product, Integer> getAllFromCart(Customer customer) {
        Cart cart = customerCarts.get(customer);
        return cart != null ? cart.getItems() : new HashMap<>();
    }

    @Override
    public boolean placeOrder(Customer customer, List<Map<Product, Integer>> cartItems, String shippingAddress) {
        // Create an order object and add it to the orders list
        Order order = new Order(customer, cartItems, shippingAddress);
        orders.add(order);
        // Clear the customer's cart after placing the order
        customerCarts.remove(customer);
        return true;
    }

    @Override
    public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) throws OrderNotFoundException {
        List<Map<Product, Integer>> customerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().getCustomerId() == customerId) {
                customerOrders.add(order.getCartItems());
            }
        }
        if (customerOrders.isEmpty()) {
            throw new OrderNotFoundException("No orders found for Customer ID " + customerId);
        }
        return customerOrders;
    }
}
