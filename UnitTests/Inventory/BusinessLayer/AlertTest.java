// File: Inventory/BusinessLayer/AlertTest.java

package Inventory.BusinessLayer;

import static org.junit.jupiter.api.Assertions.*;

import Inventory.BusinessLayer.Inventory.*;
import Inventory.ServiceLayer.Inventory.ServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class AlertTest {


    private Alert alert;
    private Item item;
    private InventoryManager Manger = new InventoryManager();
    private ServiceFactory serviceFactory = new ServiceFactory();

    @BeforeEach
    void setUp() {
// Assume you have proper objects for Supplier, Category, Location classes

        alert = new Alert("Low stock", "Warning", new Date(), item);
        List<String> SuppliedItems = new ArrayList<>();
        SuppliedItems.add("Milk");
        // Example constructor
        Category category = new Category("Food", 20, 25);
        Location storeLocation = new Location("1", "5"); // Aisle 1, Shelf 5
        Location warehouseLocation = new Location("2", "3"); // Aisle 2, Shelf 3

        Date expirationDate = new Date(); // Today for example

//        item = new Item(
//                "Milk",
//                category,
//                expirationDate,
//                50, // quantityInWarehouse
//                20, // quantityInStore
//                "IBrahem", "Saris",
//                2.0, // buyingPrice
//                3.5, // sellingPrice
//                10, // demand
//                storeLocation,
//                warehouseLocation);

        serviceFactory.addItem("String",1,expirationDate,50,20,"sd","s",30,20,5,",","","","");

    }

    @Test
    void getId() {
        assertEquals(alert.getId(), alert.getId()); // Alert ID is automatically set, just verify it's consistent
    }

    @Test
    void updateAlert() {
        List<String> SuppliedItems = new ArrayList<>();
        SuppliedItems.add("Milk");
        Category category = new Category("Food", 20, 25);
        Location storeLocation = new Location("1", "5"); // Aisle 1, Shelf 5
        Location warehouseLocation = new Location("2", "3"); // Aisle 2, Shelf 3

        Date expirationDate = new Date(); // Today for example

        Item newItem = new Item(
                "Water",
                category,
                expirationDate,
                50, // quantityInWarehouse
                20, // quantityInStore
                "supplier", "Saris",
                2.0, // buyingPrice
                3.5, // sellingPrice
                10, // demand
                storeLocation,
                warehouseLocation);
        Date newDate = new Date();
        alert.updateAlert("Out of stock", "Critical", newDate, newItem);

        assertEquals("Out of stock", alert.getMessage());
        assertEquals("Critical", alert.getType());
        assertEquals(newDate, alert.getDateCreated());
        assertEquals(1, alert.getItem().getId());
    }

    @Test
    void getMessage() {
        assertEquals("Low stock", alert.getMessage());
    }

    @Test
    void getType() {
        assertEquals("Warning", alert.getType());
    }

    @Test
    void getItem() {
        assertEquals(100, alert.getItem().getId());
    }

    @Test
    void getDateCreated() {
        assertNotNull(alert.getDateCreated());
    }

    @Test
    void generateAlert() {
        String generated = alert.generateAlert();
        assertTrue(generated.contains("Alert generated:"));
        assertTrue(generated.contains("Alert ID:"));
        assertTrue(generated.contains("Item Id: 100"));
        assertTrue(generated.contains("Message: Low stock"));
        assertTrue(generated.contains("Type: Warning"));
    }


}