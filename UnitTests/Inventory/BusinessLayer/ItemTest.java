// File: Inventory/BusinessLayer/ItemTest.java

package Inventory.BusinessLayer;

import static org.junit.jupiter.api.Assertions.*;

import Inventory.BusinessLayer.Inventory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ItemTest {

    private Item item;
    private InventoryManager Manger = new InventoryManager();

    @BeforeEach
    void setUp() {
        List<String> List = new ArrayList<>();
        List.add("Monster Energy Drinks");
        Category category = new Category("Electronics", 0, 0);
        Location storeLocation = new Location("1", "1");
        Location warehouseLocation = new Location("2", "2");
        item = new Item(
                "Laptop",
                category,
                new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10)),
                20,
                10,
                "Ibra","Saris",
                500.0,
                800.0,
                5,
                storeLocation,
                warehouseLocation);
    }

    @Test
    void addUnit() {
        int initialQuantity = item.getQuantity();
        item.addUnit("Store", 5);
        assertEquals(initialQuantity + 5, item.getQuantity());
    }

    @Test
    void removeUnit() {
        int initialQuantity = item.getQuantity();
        item.removeUnit("Warehouse", 5);
        assertEquals(initialQuantity - 5, item.getQuantity());
    }

    @Test
    void updateItem() {
        List<String> List = new ArrayList<>();
        List.add("MILK");
        Category newCategory = new Category("Computers", 0, 0);
        Location newStoreLocation = new Location("3", "3");
        Location newWarehouseLocation = new Location("4", "4");
        Date newExpirationDate = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(20));

        item.updateItem(newStoreLocation, newWarehouseLocation, "Desktop", newCategory,  newExpirationDate,15,25, "Saris", 600.0, 900.0, 8);
        assertEquals("Desktop", item.getName());
        assertEquals(newCategory, item.getCategory());
        assertEquals(900.0, item.getSellingPrice());
    }

    @Test
    void getId() {
        assertEquals(item.getId(), item.getId());
    }

    @Test
    void getExpirationDate() {
        assertNotNull(item.getExpirationDate());
    }

    @Test
    void isBelowThreshold() {
        item.removeUnit("Store", 50);
        assertTrue(item.isBelowThreshold());
    }

    @Test
    void applyDiscount() {
        Discount discount = new Discount("New Year Sale", "Item", 10.0, new Date(), new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5)),Manger);
        double oldPrice = item.getSellingPrice();
        item.applyDiscount(discount);
        assertTrue(item.getSellingPrice() < oldPrice);
    }

    @Test
    void removeDiscounts() {
        Discount discount = new Discount("Seasonal Sale", "Item", 15.0, new Date(), new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)),Manger);
        List<String> List = new ArrayList<>();
        List.add("Monster Energy Drinks");
        Category category = new Category("Electronics", 0, 0);
        Location storeLocation = new Location("1", "1");
        Location warehouseLocation = new Location("2", "2");
        Item newItem = new Item(
                "iphone 16",
                category,
                new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)),
                20,
                10,
                "Ibra","Saris",
                2500,
                1000,
                10,
                storeLocation,
                warehouseLocation);
        newItem.applyDiscount(discount);
        newItem.removeDiscounts();
        assertTrue(newItem.getActiveDiscounts().isEmpty());
    }

    @Test
    void removeDiscount() {
        Discount discount = new Discount("Flash Sale", "Item", 5.0, new Date(), new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)),Manger);
        item.applyDiscount(discount);
        item.removeDiscount(discount.getId());
        assertFalse(item.getActiveDiscounts().containsKey(discount.getId()));
    }

    @Test
    void sortDiscountsByEndDate() {
        Discount discount1 = new Discount("Sale1", "Item", 5.0, new Date(), new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3)),Manger);
        Discount discount2 = new Discount("Sale2", "Item", 10.0, new Date(), new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)),Manger);
        item.applyDiscount(discount1);
        item.applyDiscount(discount2);
        assertEquals(discount2.getId(), item.sortDiscountsByEndDate().get(0).getId());
    }

    @Test
    void isExpired() {
        InventoryManager Manger = new InventoryManager();
        Date ExpDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(50));
        List<String> List = new ArrayList<>();
        List.add("MILK");
        Item expiredItem = new Item(
                "Old Laptop",
                new Category("OldElectronics", 0, 0),
                ExpDate,
                10,
                5,
       "Ibra" ,"Saris",
                200.0,
                400.0,
                3,
                new Location("1", "1"),
                new Location("2", "2"));
        assertTrue(expiredItem.isExpired());
    }

    @Test
    void getActiveDiscounts() {
        Discount discount = new Discount("Holiday Offer", "Item", 20.0, new Date(), new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5)),Manger);
        item.applyDiscount(discount);
        assertFalse(item.getActiveDiscounts().isEmpty());
    }
}
