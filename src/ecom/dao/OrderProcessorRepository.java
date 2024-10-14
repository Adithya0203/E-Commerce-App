package ecom.dao;

import ecom.entity.*;
import ecom.exception.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface OrderProcessorRepository {
   
	boolean createProduct(Product product);

   boolean createCustomer(Customer customer);

   boolean deleteProduct(int productId) throws ProductNotFoundException;

   boolean deleteCustomer(int customerId) throws CustomerNotFoundException;

   boolean addToCart(Customer customer, Product product, int quantity) throws CustomerNotFoundException, ProductNotFoundException;

   boolean removeFromCart(Customer customer, Product product);

   List<Product> getAllFromCart(Customer customer);

   boolean placeOrder(Customer customer,String shippingAddress) throws SQLException, ProductNotFoundException;

//   List<Map<Product, Integer>> getOrdersByCustomer(int var1) throws OrderNotFoundException;
   List<Order> getOrdersByCustomer(int customerId) throws OrderNotFoundException, CustomerNotFoundException;
   
   Customer getCustomerById(int customerId) throws CustomerNotFoundException;
   
   Product getProductById(int productId) throws ProductNotFoundException;
   
   List<Product> getAllProducts();
   
   List<Customer> getAllCustomers();
   
   List<Order> getAllOrders();

boolean deleteOrder(int orderIdToDelete) throws OrderNotFoundException;
}