// File: Inventory/BusinessLayer/DiscountTest.java

package Inventory.BusinessLayer;

import static org.junit.jupiter.api.Assertions.*;

import Inventory.BusinessLayer.Inventory.Discount;
import Inventory.BusinessLayer.Inventory.InventoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class DiscountTest {

    private Discount discount;
    private Date startDate;
    private Date endDate;
    private InventoryManager Manger = new InventoryManager();

    @BeforeEach
    void setUp() {
        startDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)); // yesterday
        endDate = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)); // tomorrow
        discount = new Discount("Holiday Sale", "Item", 20.0, startDate, endDate,Manger);
    }

    @Test
    void setOldPrice() {
        discount.setOldPrice(100.0);
        assertEquals(100.0, discount.getOldPrice());
    }

    @Test
    void getOldPrice() {
        discount.setOldPrice(150.0);
        assertEquals(150.0, discount.getOldPrice());
    }

    @Test
    void getDiscountPercentage() {
        assertEquals(20.0, discount.getDiscountPercentage());
    }

    @Test
    void setDiscountPercentage() {
        discount.setDiscountPercentage(30.0);
        assertEquals(30.0, discount.getDiscountPercentage());
    }

    @Test
    void getStartDate() {
        assertEquals(startDate, discount.getStartDate());
    }

    @Test
    void setStartDate() {
        Date newStartDate = new Date();
        discount.setStartDate(newStartDate);
        assertEquals(newStartDate, discount.getStartDate());
    }

    @Test
    void setName() {
        discount.setName("Black Friday Sale");
        assertEquals("Black Friday Sale", discount.getName());
    }

    @Test
    void setType() {
        discount.setType("Category");
        assertEquals("Category", discount.getType());
    }

    @Test
    void getEndDate() {
        assertEquals(endDate, discount.getEndDate());
    }

    @Test
    void setEndtDate() {
        Date newEndDate = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5));
        discount.setEndtDate(newEndDate);
        assertEquals(newEndDate, discount.getEndDate());
    }

    @Test
    void isExpired() {
        Discount expiredDiscount = new Discount("Old Sale", "Item", 10.0,
                new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)),
                new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)),Manger);
        assertTrue(expiredDiscount.isExpired());
    }

    @Test
    void isActive() {
        assertTrue(discount.isActive());
    }
}
