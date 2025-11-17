import domainLayer.Supplier.Managers.OrdersManager;
import domainLayer.Supplier.Objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private OrdersManager manager;
    private Order order1;
    private Order order2;
    private Map<SupItem, Integer> itemMap;
    private LocalDate date;
    private Contact contact;

    @BeforeEach
    void setUp() {
        manager = new OrdersManager();
        itemMap = new HashMap<>();
        date = LocalDate.now();
        contact = new Contact("1", "John Doe", "1234567890", "john.doe@example.com");

        // Create mock dependencies
        Product product = new Product("P1", "Widget");
        Manufacturer manufacturer = new Manufacturer("M1", "ACME Corp");

        List<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount(1, 0.1)); // 10% for 1+
        discounts.add(new Discount(3, 0.2)); // 20% for 3+

        SupItem item = new SupItem(product,"2","1", 100.0, discounts, manufacturer, "SCN1");

        itemMap.put(item, 3); // Quantity of 3 → triggers 20% discount

        order1 = new Order("O1", "S1","s1", itemMap, date, contact,"ad1");
        order2 = new Order("O2", "S1","s1", itemMap, date, contact,"ad2");
    }


    @Test
    void testOrderToString() {
        String result = order1.toString();
        assertTrue(result.contains("O1"));
        assertTrue(result.contains("S1"));
        assertTrue(result.contains("240.0"));
    }

    @Test
    void testAddOrder() {
        manager.addOrder(order1);
        assertTrue(manager.containsOrder("O1"));
    }

    //False test [exceptions]
    @Test
    void testAddOrder_NullOrder() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> manager.addOrder((Order) null));
        assertEquals("Order or Order ID cannot be null", exception.getMessage());
    }

    @Test
    void testAddOrder_NullId() {
        Order invalidOrder = new Order(null, "S2","s1", itemMap, date, contact,"ad3");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> manager.addOrder(invalidOrder));
        assertEquals("Order or Order ID cannot be null", exception.getMessage());
    }

    @Test
    void testRemoveOrder_Success() {
        manager.addOrder(order1);
        assertTrue(manager.removeOrder("O1").contains("successfully"));
        assertFalse(manager.containsOrder("O1"));
    }

    @Test
    void testRemoveOrder_Fail() {
        assertFalse(manager.removeOrder("FakeID").contains("successfully"));
    }

    @Test
    void testGetOrderById() {
        manager.addOrder(order1);
        Order result = manager.getOrderById("O1");
        assertNotNull(result);
        assertEquals("S1", result.getSupplierId());
    }

    @Test
    void testContainsOrder() {
        manager.addOrder(order1);
        assertTrue(manager.containsOrder("O1"));
        assertFalse(manager.containsOrder("O99"));
    }

    @Test
    void testClearAllOrders() {
        manager.addOrder(order1);
        manager.addOrder(order2);
        manager.clearAllOrders();
        assertEquals(0, manager.getAllOrders().size());
    }

    @Test
    void testGetAllOrders() {
        manager.addOrder(order1);
        manager.addOrder(order2);
        Map<String, Order> allOrders = manager.getAllOrders();
        assertEquals(2, allOrders.size());
        assertTrue(allOrders.containsKey("O1"));
        assertTrue(allOrders.containsKey("O2"));
    }

    @Test
    void testGetOrderBySupId_Found() {
        manager.addOrder(order1);
        manager.addOrder(order2);
        List<Order> found = manager.getOrderBySupId("S1");
        assertNotNull(found);
        assertEquals(2, found.size());
    }

    @Test
    void testGetOrderBySupId_NotFound() {
        List<Order> result = manager.getOrderBySupId("UnknownID");
        assertNull(result);
    }
    @Test
    void testOrderStatusCancel() {
        order1.cancel();
        assertEquals(STATUS.CANCELLED, order1.getStatus());
    }

    @Test
    void testOrderStatusDelivered() {
        order1.delivered();
        assertEquals(STATUS.DELIVERED, order1.getStatus());
    }

}
