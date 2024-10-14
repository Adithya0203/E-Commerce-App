package ecom.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ecom.dao.*;
import ecom.entity.*;
import ecom.exception.*;

public class EcomApp {

    public static void main(String[] args) throws SQLException, CustomerNotFoundException, ProductNotFoundException {
        Scanner scanner = new Scanner(System.in);
        OrderProcessorRepository orderService = new OrderProcessorRepositoryImpl();
        
        while (true) {
            System.out.println("\n===== E-Commerce Application Menu =====");
            System.out.println("1. Register Customer");
            System.out.println("2. Create Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Delete Customer");
            System.out.println("5. Add to Cart");
            System.out.println("6. Remove from Cart");
            System.out.println("7. View Cart");
            System.out.println("8. Place Order");
            System.out.println("9. Delete Order");
            System.out.println("10. View Customer Orders");
            System.out.println("11. View All Products");
            System.out.println("12. View All Customers");
            System.out.println("13. View All Orders");
            System.out.println("14. Exit");
            System.out.print("Choose an operation: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: // Register Customer
                    System.out.println("Enter customer name:");
                    String customerName = scanner.next();
                    
                    String capitalName = customerName.substring(0,1).toUpperCase() 
                    		+ customerName.substring(1,customerName.length());
                    
                    System.out.println("Enter customer email:");
                    String customerEmail = scanner.next();
                    System.out.println("Enter customer password:");
                    String customerPassword = scanner.next();
                    Customer newCustomer = new Customer(capitalName, customerEmail, customerPassword);
                    boolean customerCreated = orderService.createCustomer(newCustomer);
                    if (customerCreated) {
                        System.out.println("Customer registered successfully.");
                    } else {
                        System.out.println("Failed to register customer.");
                    }
                    break;

                case 2: // Create Product
                    try {
                    	scanner.nextLine();
                        System.out.println("Enter product name:");
                        String productName = scanner.nextLine();

                        System.out.println("Enter product price (with decimals):");
                        double productPrice = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline

                        System.out.println("Enter product description:");
                        String productDescription = scanner.nextLine();

                        System.out.println("Enter product stock quantity:");
                        int productStockQuantity = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Create Product object (assuming you have a Product class with a constructor)
                        Product product = new Product(productName, productPrice, productDescription, productStockQuantity);

                        // Call createProduct method in service
                        boolean isSuccess = orderService.createProduct(product);
                        if (isSuccess) {
                            System.out.println("Product created successfully!");
                            System.out.println("Product details: " + product.getName() + ", " + product.getPrice() + ", " + product.getDescription() + ", " + product.getStockQuantity());
                        } else {
                            System.out.println("Failed to create the product.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter the correct data type.");
                        scanner.nextLine(); // Consume the invalid input
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;

                case 3: // Delete Product
                    System.out.println("Enter product ID to delete:");
                    int deleteProductId = scanner.nextInt();
                    try {
                        boolean productDeleted = orderService.deleteProduct(deleteProductId);
                        if (productDeleted) {
                            System.out.println("Product deleted successfully.");
                        } else {
                            System.out.println("Failed to delete product.");
                        }
                    } catch (ProductNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                
                case 4: // Delete Customer
                    try {
                        System.out.println("Enter customer ID to delete:");
                        int customerId = scanner.nextInt();

                        boolean isSuccess = orderService.deleteCustomer(customerId);
                        if (isSuccess) {
                            System.out.println("Customer deleted successfully!");
                        } else {
                            System.out.println("Failed to delete the customer.");
                        }
                    } catch (CustomerNotFoundException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;

                case 5: // Add to Cart
                    System.out.println("Enter customer ID:");
                    int customerId = scanner.nextInt();
                    System.out.println("Enter product ID:");
                    int productId = scanner.nextInt();
                    System.out.println("Enter quantity:");
                    int quantity = scanner.nextInt();
                    try {
                        Customer customer = orderService.getCustomerById(customerId);
                        Product product = orderService.getProductById(productId);
                        boolean addedToCart = orderService.addToCart(customer, product, quantity);
                        if (addedToCart) {
                            System.out.println("Product added to cart.");
                        } else {
                            System.out.println("Failed to add product to cart.");
                        }
                    } catch (CustomerNotFoundException | ProductNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                
                case 6: // Remove from Cart
                    try {
                        System.out.println("Enter customer ID:");
                        int customerIdToRemove = scanner.nextInt();

                        System.out.println("Enter product ID:");
                        int productIdToRemove = scanner.nextInt();

                        Customer customer = orderService.getCustomerById(customerIdToRemove);
                        Product product = orderService.getProductById(productIdToRemove);

                        boolean isSuccess = orderService.removeFromCart(customer, product);
                        if (isSuccess) {
                            System.out.println("Product removed from cart.");
                        } else {
                            System.out.println("Failed to remove product from cart.");
                        }
                    } catch (CustomerNotFoundException | ProductNotFoundException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;                    

                case 7: // View Cart
                    System.out.println("Enter customer ID to view cart:");
                    int viewCartCustomerId = scanner.nextInt();
                    try {
                        Customer viewCartCustomer = orderService.getCustomerById(viewCartCustomerId);
                        List<Product> cartItems = orderService.getAllFromCart(viewCartCustomer);
                        System.out.println("Cart items:");
                        for (Product product : cartItems) {
                            System.out.println(product.getName() + " - " + product.getPrice() + " (Qty: " + product.getStockQuantity() + ")");
                        }
                    } catch (CustomerNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 8: // Place Order
                    System.out.println("Enter customer ID:");
                    int placeOrderCustomerId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter shipping address:");
                    String shippingAddress = scanner.nextLine();
                    try {
                        Customer placeOrderCustomer = orderService.getCustomerById(placeOrderCustomerId);
                        List<Map<Product, Integer>> cartItems = new ArrayList<>();  // Assume we get cart items here.
                        boolean orderPlaced = orderService.placeOrder(placeOrderCustomer,shippingAddress);
                        if (orderPlaced) {
                            System.out.println("Order placed successfully.");
                        } else {
                            System.out.println("Failed to place order.");
                        }
                    } catch (CustomerNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                
                case 9: // Delete Order
                    System.out.print("Enter Order ID to delete: ");
                    int orderIdToDelete = scanner.nextInt();
                    try {
                        boolean isDeleted = orderService.deleteOrder(orderIdToDelete);
                        if (isDeleted) {
                            System.out.println("Order deleted successfully.");
                        } else {
                            System.out.println("Failed to delete the order.");
                        }
                    } catch (OrderNotFoundException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error occurred: " + e.getMessage());
                    }
                    break;

                case 10: // View Customer Orders
                    System.out.println("Enter customer ID to view orders:");
                    int viewOrdersCustomerId = scanner.nextInt();
                    try {
                        List<Order> orders = orderService.getOrdersByCustomer(viewOrdersCustomerId);
//                        System.out.println("Orders:");
//                        for (Order order : orders) {
//                            System.out.println("Order ID: " + order.getOrderId() + ", Shipping Address: " + order.getShippingAddress());
//                        }
                    } catch (OrderNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 11: // View All Products
                    try {
                        List<Product> products = orderService.getAllProducts();
                        System.out.println("All Products:");
                        for (Product product : products) {
                            System.out.println(product);
                        }
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;

                case 12: // View All Customers
                    try {
                        List<Customer> customers = orderService.getAllCustomers();
                        System.out.println("All Customers:");
                        for (Customer customer : customers) {
                            System.out.println(customer);
                        }
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;

                case 13: // View All Orders
                    List<Order> orders = orderService.getAllOrders();
                    if (orders.isEmpty()) {
                        System.out.println("No orders found.");
                    } else {
                        System.out.println("All Orders:");
                        for (Order order : orders) {
                            System.out.println(order);  // This will now call the toString() method
                        }
                    }

                    break;

                case 14: // Exit
                    System.out.println("Thank you visit again");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}