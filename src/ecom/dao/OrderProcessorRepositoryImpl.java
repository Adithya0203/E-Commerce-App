package ecom.dao;

import ecom.entity.*;
import ecom.exception.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import ecom.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.DoubleStream;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {
   private List<Product> products = new ArrayList<>();
    
   public static String hashPassword(String password) {
        try {
            // instance of the SHA-256 MessageDigest
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Perform hashing
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            // Return the hashed password as a hexadecimal string
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 algorithm not found.");
        }
    }
	
   @Override
   public boolean createProduct(Product product){
      try(Connection conn = DBConnUtil.getConnection()){
    	  String query = "INSERT INTO products (name, price, description, stockQuantity) VALUES (?, ?, ?, ?)";
	      try(PreparedStatement stmt = conn.prepareStatement(query)){;
	         stmt.setString(1, product.getName());
	         stmt.setDouble(2, product.getPrice());
	         stmt.setString(3, product.getDescription());
	         stmt.setInt(4, product.getStockQuantity());
	         return stmt.executeUpdate() > 0;
	      }
      } catch (SQLException e) {
         e.printStackTrace();
         return false;
      }
   }

   public boolean createCustomer(Customer customer) {
      try(Connection conn = DBConnUtil.getConnection()){
    	  String query = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";
          try(PreparedStatement stmt = conn.prepareStatement(query)){
             stmt.setString(1, customer.getName());
             stmt.setString(2, customer.getEmail());
             
             String userPassword = customer.getPassword();
             String hashedPassword = hashPassword(userPassword);
             stmt.setString(3, hashedPassword);
             
             return stmt.executeUpdate() > 0;
          }  
      } catch(SQLException e) {
    	  e.printStackTrace();
      }
      return false;
   }

   public boolean deleteProduct(int productId) throws ProductNotFoundException {
	    try (Connection conn = DBConnUtil.getConnection()) {
	        String checkSql = "SELECT productid FROM products WHERE productId = ?";
	        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
	            checkStmt.setInt(1, productId);
	            try (ResultSet rs = checkStmt.executeQuery()) {
	                if (!rs.next()) {
	                    throw new ProductNotFoundException("Product with ID " + productId + " not found.");
	                }
	            }
	        }

	        String sql = "DELETE FROM products WHERE productId = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, productId);
	            return stmt.executeUpdate() > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

   public boolean deleteCustomer(int customerId) throws CustomerNotFoundException {
	   try(Connection conn = DBConnUtil.getConnection()){
		   String checkQuery = "SELECT customerId FROM customers WHERE customerId = ?";
		   try(PreparedStatement checkStmt = conn.prepareStatement(checkQuery)){
			   checkStmt.setInt(1, customerId);
			   try(ResultSet rs = checkStmt.executeQuery()){
				   if(!rs.next()) {
					   throw new CustomerNotFoundException("Customer with ID " + customerId + " not found \n");
				   }
			   }
		   }
		   
		   String query = "DELETE FROM customers WHERE customerId = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, customerId);
	            return stmt.executeUpdate() > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
   }

   @Override
   public boolean addToCart(Customer customer, Product product, int quantity) throws CustomerNotFoundException, ProductNotFoundException {
       // Check if the customer exists
       if (getCustomerById(customer.getCustomerID()) == null) {
           throw new CustomerNotFoundException("Customer with ID " + customer.getCustomerID() + " does not exist.");
       }

       // Check if the product exists
       if (getProductById(product.getProductID()) == null) {
           throw new ProductNotFoundException("Product with ID " + product.getProductID() + " does not exist.");
       }

       String query = "INSERT INTO cart (customerId, productId, quantity) VALUES (?, ?, ?)";

       try (Connection connection = DBConnUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query)) {

           pstmt.setInt(1, customer.getCustomerID());
           pstmt.setInt(2, product.getProductID());
           pstmt.setInt(3, quantity);

           int rowsInserted = pstmt.executeUpdate();
           return rowsInserted > 0;

       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }

   public boolean removeFromCart(Customer customer, Product product) {
       try (Connection conn = DBConnUtil.getConnection()) {
           String sql = "DELETE FROM cart WHERE customerId = ? AND productId = ?";
           try (PreparedStatement stmt = conn.prepareStatement(sql)) {
               stmt.setInt(1, customer.getCustomerID());
               stmt.setInt(2, product.getProductID());
               return stmt.executeUpdate() > 0;
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
   }

   public List<Product> getAllFromCart(Customer customer) {
	   String query = "SELECT p.productId, p.name, p.price,p.description, c.quantity FROM cart c " +
               "JOIN products p ON c.productId = p.productId WHERE c.customerId = ?";
	   List<Product> cartProducts = new ArrayList<>();
	    try (Connection connection = DBConnUtil.getConnection();
	            PreparedStatement pstmt = connection.prepareStatement(query)) {
	           
	           pstmt.setInt(1, customer.getCustomerID());
	           ResultSet rs = pstmt.executeQuery();
	           
	           while (rs.next()) {
	               int productId = rs.getInt("productid");
	               String name = rs.getString("name");
	               double price = rs.getDouble("price");
	               String description = rs.getString("description");
	               int quantity = rs.getInt("quantity"); // quantity in cart
	               
	               // Create Product object with the retrieved data
	               Product product = new Product(productId, name, price, description, quantity);
	               
	               // Add product to the list
	               cartProducts.add(product);
	           }
	   } catch(SQLException e) {
		   e.printStackTrace();
	   }
	   return cartProducts;
   }

   @Override
   public boolean placeOrder(Customer customer, String shippingAddress) throws SQLException, ProductNotFoundException {
       String insertOrderSQL = "INSERT INTO orders (customerId, orderDate, totalPrice, shippingAddress) VALUES (?, NOW(), ?, ?)";
       String insertOrderItemSQL = "INSERT INTO orderItems (orderId, productId, quantity) VALUES (?, ?, ?)";
       String getCartItemsSQL = "SELECT productId, quantity FROM cart WHERE customerId = ?";

       Connection connection = null;
       PreparedStatement orderStatement = null;
       PreparedStatement orderItemStatement = null;
       PreparedStatement cartStatement = null;
       ResultSet generatedKeys = null;
       ResultSet cartItemsResult = null;

       try {
           // Get DB connection
           connection = DBConnUtil.getConnection();
           
           // Disable auto-commit for transaction management
           connection.setAutoCommit(false);

           // Get cart items for the customer
           cartStatement = connection.prepareStatement(getCartItemsSQL);
           cartStatement.setInt(1, customer.getCustomerID());
           cartItemsResult = cartStatement.executeQuery();

           double totalPrice = 0.0;
           
           if (!cartItemsResult.isBeforeFirst()) {
               throw new SQLException("Cart is empty for customer ID: " + customer.getCustomerID());
           }
           
           List<Map<Product, Integer>> cartItems = new ArrayList<>();

           // Fetch each cart item
           while (cartItemsResult.next()) {
               int productId = cartItemsResult.getInt("productId");
               int quantity = cartItemsResult.getInt("quantity");

               // Fetch product details using productId
               Product product = getProductById(productId); // Assume this method fetches product by ID
               if (product != null) {
                   totalPrice += product.getPrice() * quantity; // Calculate total price
                   Map<Product, Integer> item = new HashMap<>();
                   item.put(product, quantity);
                   cartItems.add(item); // Add to cartItems list
               }
           }

           // Insert order into the orders table
           orderStatement = connection.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
           orderStatement.setInt(1, customer.getCustomerID());
           orderStatement.setDouble(2, totalPrice);
           orderStatement.setString(3, shippingAddress);
           orderStatement.executeUpdate();

           // Get the generated order ID
           generatedKeys = orderStatement.getGeneratedKeys();
           int orderId = 0;
           if (generatedKeys.next()) {
               orderId = generatedKeys.getInt(1); // This is the auto-incremented order ID
           } else {
               throw new SQLException("Creating order failed, no ID obtained.");
           }

           // Insert each item into order_items table
           orderItemStatement = connection.prepareStatement(insertOrderItemSQL);
           for (Map<Product, Integer> entry : cartItems) {
               Product product = entry.keySet().iterator().next();
               int quantity = entry.get(product);
               
               orderItemStatement.setInt(1, orderId);
               orderItemStatement.setInt(2, product.getProductID());
               orderItemStatement.setInt(3, quantity);
               orderItemStatement.addBatch();
           }

           // Execute batch update for order items
           orderItemStatement.executeBatch();
           
           // Commit transaction
           connection.commit();
           
           clearCartForCustomer(customer.getCustomerID());
           
           return true;

       } catch (SQLException e) {
           if (connection != null) {
               try {
                   connection.rollback(); // Roll back transaction on error
               } catch (SQLException rollbackEx) {
                   rollbackEx.printStackTrace();
               }
           }
           e.printStackTrace();
           throw e; // Re-throw exception
       } finally {
           if (cartItemsResult != null) try { cartItemsResult.close(); } catch (SQLException ignore) {}
           if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException ignore) {}
           if (orderStatement != null) try { orderStatement.close(); } catch (SQLException ignore) {}
           if (orderItemStatement != null) try { orderItemStatement.close(); } catch (SQLException ignore) {}
           if (cartStatement != null) try { cartStatement.close(); } catch (SQLException ignore) {}
           if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
       }
   }
   
   public void clearCartForCustomer(int customerId) throws SQLException {
	    String deleteCartSQL = "DELETE FROM cart WHERE customerId = ?";
	    
	    try (Connection connection = DBConnUtil.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(deleteCartSQL)) {
	        preparedStatement.setInt(1, customerId);
	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new SQLException("Error clearing cart for customer ID: " + customerId, e);
	    }
	}

   public boolean deleteOrder(int orderId) throws OrderNotFoundException {
	    String sql = "DELETE FROM orders WHERE orderid = ?";
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    try {
	        // Get DB connection
	        connection = DBConnUtil.getConnection();
	        
	        // Check if order exists before attempting to delete
	        if (!orderExists(orderId, connection)) {
	            throw new OrderNotFoundException("Order ID: " + orderId + " does not exist.");
	        }

	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setInt(1, orderId);
	        int rowsAffected = preparedStatement.executeUpdate();

	        return rowsAffected > 0; // Return true if at least one row was deleted

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error occurred while deleting the order: " + e.getMessage());
	    } finally {
	        if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}

	// Helper method to check if an order exists
	private boolean orderExists(int orderId, Connection connection) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM orders WHERE orderid = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, orderId);
	        ResultSet rs = preparedStatement.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // Return true if order exists
	        }
	    }
	    return false;
	}
   
   @Override
   public List<Order> getOrdersByCustomer(int customerId) throws OrderNotFoundException, CustomerNotFoundException {
	    List<Order> orders = new ArrayList<>();
	    String sql = "SELECT * FROM orders WHERE customerid = ?";

	    // Debug: Print the customer ID
	    System.out.println("Fetching orders for customer ID: " + customerId);

	    try (Connection connection = DBConnUtil.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, customerId);
	        ResultSet rs = preparedStatement.executeQuery();

	        // Debug: Check if the query executed correctly
	        if (!rs.isBeforeFirst()) {
	            System.out.println("No orders found in result set for customer ID: " + customerId);
	        }

	        while (rs.next()) {
	            int orderId = rs.getInt("orderId");
	            Date orderDate = rs.getDate("orderDate");
	            String shippingAddress = rs.getString("shippingAddress");
	            double totalPrice = rs.getDouble("totalPrice");

	            // Fetch order items (if needed)
	            //List<Map<Product, Integer>> orderItems = getOrderItemsByOrderId(orderId);
	            Order order = new Order(orderId, orderDate, totalPrice, shippingAddress);
	            orders.add(order);
	            
	            System.out.printf("Order ID: %d, Date: %s, Total Price: %.2f, Shipping Address: %s%n",
	                    orderId, orderDate, totalPrice, shippingAddress);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Print the stack trace for debugging
	        throw new OrderNotFoundException("Error fetching orders for customer ID: " + customerId);
	    }

	    if (orders.isEmpty()) {
	        throw new OrderNotFoundException("No orders found for customer ID: " + customerId);
	    }

	    return orders;
	}

   
   private List<Map<Product, Integer>> getOrderItemsByOrderId(int orderId) {
	    List<Map<Product, Integer>> orderItemsList = new ArrayList<>();
	    String sql = "SELECT oi.product_id, oi.quantity, p.name, p.price, p.description FROM order_items oi " +
	                 "JOIN products p ON oi.product_id = p.product_id WHERE oi.order_id = ?";

	    try (Connection connection = DBConnUtil.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, orderId);
	        ResultSet rs = preparedStatement.executeQuery();

	        while (rs.next()) {
	            int productId = rs.getInt("product_id");
	            int quantity = rs.getInt("quantity");
	            String productName = rs.getString("name");
	            double price = rs.getDouble("price");
	            String description = rs.getString("description");

	            Product product = new Product(productId, productName, price, description,quantity);
	            Map<Product, Integer> orderItem = new HashMap<>();
	            orderItem.put(product, quantity);
	            orderItemsList.add(orderItem);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle the exception as needed
	    }

	    return orderItemsList;
	}
   
   @Override
   public Customer getCustomerById(int customerId) throws CustomerNotFoundException {
       String query = "SELECT * FROM customers WHERE customerId = ?";
       Customer customer = null;
       try(Connection conn = DBConnUtil.getConnection()) {
           // Execute query to find the customer with the given ID
           PreparedStatement ps = conn.prepareStatement(query);
           ps.setInt(1, customerId);
           ResultSet rs = ps.executeQuery();

           if (rs.next()) {
               // Map result set to Customer object
                   String name = rs.getString("name");
                   String email = rs.getString("email");
                   String password = rs.getString("password");
                   
                   customer = new Customer(customerId, name, email, password);
           } else {
               // If no customer found, throw custom exception
               throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
           }
       } catch (SQLException e) {
           e.printStackTrace(); // Handle SQL exception
       }

       return customer;
   }
   
   @Override
   public Product getProductById(int productId) throws ProductNotFoundException {
       String query = "SELECT * FROM products WHERE productId = ?";
	   Product product = null;

       try (Connection conn = DBConnUtil.getConnection()){
           // Query to find the product by product ID
           PreparedStatement ps = conn.prepareStatement(query);
           ps.setInt(1, productId);
           ResultSet rs = ps.executeQuery();

           if (rs.next()) {
               // Map result set to Product object
               String name = rs.getString("name");
               Double price = rs.getDouble("price");
               String desc = rs.getString("description");
               int quantity = rs.getInt("stockQuantity");
               
               product = new Product(productId, name, price, desc, quantity);
           } else {
               // If no product found, throw custom exception
               throw new ProductNotFoundException("Product with ID " + productId + " not found.");
           }
       } catch (SQLException e) {
           e.printStackTrace(); // Handle SQL exception
       }

       return product;
   }
   
   @Override
   public List<Product> getAllProducts() {
       List<Product> products = new ArrayList<>();
       String query = "SELECT * FROM products";
       try (Connection connection = DBConnUtil.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

           while (resultSet.next()) {
        	   int productId = resultSet.getInt("productId");
               String name = resultSet.getString("name");
               double price = resultSet.getDouble("price");
               String description = resultSet.getString("description");
               int stockQuantity = resultSet.getInt("stockQuantity");

               Product product = new Product(productId,name, price, description, stockQuantity);
               products.add(product);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return products;
   }

   // method to get all customers
   @Override
   public List<Customer> getAllCustomers() {
       String query = "SELECT * FROM customers";
       List<Customer> customers = new ArrayList<>();

       try (Connection connection = DBConnUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query)) {

           ResultSet rs = pstmt.executeQuery();

           while (rs.next()) {
               int customerId = rs.getInt("customerid");
               String name = rs.getString("name");
               String email = rs.getString("email");
               String password = rs.getString("password");

               // Create Customer object with retrieved data
               Customer customer = new Customer(customerId, name, email, password);

               // Add customer to the list
               customers.add(customer);
           }

       } catch (SQLException e) {
           e.printStackTrace();
       }

       return customers;
   }

   // method to get all orders
   @Override
   public List<Order> getAllOrders() {
       List<Order> orders = new ArrayList<>();
       String query = "SELECT orderId,customerId,orderdate,totalPrice,shippingAddress FROM orders";
       try (Connection connection = DBConnUtil.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

           while (resultSet.next()) {
               int orderID = resultSet.getInt("orderId");
               int customerId = resultSet.getInt("customerId");
               Date orderDate = resultSet.getDate("orderDate");
               double totalPrice = resultSet.getDouble("totalPrice");
               String shippingAddress = resultSet.getString("shippingAddress");

               Customer customer = getCustomerById(customerId); // Fetch customer details
               Order order = new Order(orderID,customerId, orderDate, totalPrice, shippingAddress);
               orders.add(order);
           }
       } catch (SQLException | CustomerNotFoundException e) {
           e.printStackTrace();
       }
       return orders;
   }

 }