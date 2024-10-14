package ecom.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.SQLException;
import java.util.*;

public class OrderServiceTest {

    private OrderService orderService; // Assuming the class containing placeOrder method is called OrderService
    private Customer mockCustomer;
    private Product mockProduct;
    private CartService mockCartService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService();
        mockCustomer = new Customer(1, "John Doe");
        mockProduct = new Product(101, "Laptop", 1500.00, 10);
        mockCartService = Mockito.mock(CartService.class); // Mocking cart service
    }

    @Test
    public void testPlaceOrderSuccessfully() throws SQLException, ProductNotFoundException {
        // Prepare cart items
        Map<Product, Integer> cartItem = new HashMap<>();
        cartItem.put(mockProduct, 2); // 2 laptops
        List<Map<Product, Integer>> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        // Mock cart items fetching
        when(mockCartService.getCartItems(mockCustomer.getCustomerID())).thenReturn(cartItems);

        // Invoke placeOrder method
        boolean result = orderService.placeOrder(mockCustomer, "123 Test St.");

        // Verify result
        assertTrue(result, "Order should be placed successfully.");
    }
}
