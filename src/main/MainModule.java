package main;

import dao.OrderProcessorRepositoryImpl;
import entity.Customer;
import entity.Product;
import exception.CustomerNotFoundException;
import exception.OrderNotFoundException;
import exception.ProductNotFoundException;
import util.DBConnUtil;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class MainModule {
    public static void main(String[] args) {
        // Establish database connection
        Connection connection = DBConnUtil.getConnection("db.properties");
        
        if (connection != null) {
            System.out.println("Database connection established successfully!");

            // Create an instance of OrderProcessorRepositoryImpl
            OrderProcessorRepositoryImpl orderProcessorRepository = new OrderProcessorRepositoryImpl();

            // Example usage
            try {
                // Create customer and product for demonstration
                Customer customer = new Customer(1, "John Doe", "john@example.com", "password");
                Product product = new Product(1, "Product A", 100.0, "Description", 10);
                
                // Add customer and product
                orderProcessorRepository.createCustomer(customer);
                orderProcessorRepository.createProduct(product);

                // Add to cart
                orderProcessorRepository.addToCart(customer, product, 2);
                
                // Delete customer
                orderProcessorRepository.deleteCustomer(customer.getCustomerId());

                // Delete product
                orderProcessorRepository.deleteProduct(product.getProductId());

                // Example of retrieving orders by customer
                orderProcessorRepository.getOrdersByCustomer(customer.getCustomerId());

            } catch (CustomerNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (ProductNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (OrderNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Failed to establish a database connection.");
        }
    }
}
