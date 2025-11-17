import ObjectDTO.Supplier.ContactDTO;
import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.SupplierDTO;
import domainLayer.Supplier.Objects.Contact;
import domainLayer.Supplier.Objects.Days;
import domainLayer.Supplier.Objects.Supplier;
import domainLayer.Supplier.Managers.SupplierManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class SupplierTest {
    private SupplierManager manager;
    private SupplierDTO supplier;

    @BeforeEach
    void setUp() {
        manager = new SupplierManager();
        supplier = new SupplierDTO("1", "Test Supplier", "123456",  new ArrayList<>() , DaysDTO.MONDAY);
    }

    @Test
    void testAddSupplier() {
        manager.addSupplier(supplier);
        assertTrue(manager.containsSupplier("1"));
    }

    //False test [exceptions]
    @Test
    void testAddSupplier_NullSupplier() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addSupplier(null);
        });
        assertEquals("Supplier or Supplier ID cannot be null", exception.getMessage());
    }

    @Test
    void testAddSupplier_NullId() {
        SupplierDTO noIdSupplier = new SupplierDTO(null, "No ID", "000",  new ArrayList<>() , DaysDTO.MONDAY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addSupplier(noIdSupplier);
        });
        assertEquals("Supplier or Supplier ID cannot be null", exception.getMessage());
    }

    @Test
    void testGetSupplierById() {
        manager.addSupplier(supplier);
        Supplier fetched = manager.getSupplierById("1");
        assertNotNull(fetched);
        assertEquals("Test Supplier", fetched.getName());
    }

    @Test
    void testRemoveSupplier() {
        manager.addSupplier(supplier);
        boolean removed = manager.removeSupplier("1");
        assertTrue(removed);
        assertFalse(manager.containsSupplier("1"));
    }

    @Test
    void testRemoveNonExistingSupplier() {
        boolean removed = manager.removeSupplier("999");
        assertFalse(removed);
    }

    @Test
    void testContainsSupplier() {
        manager.addSupplier(supplier);
        assertTrue(manager.containsSupplier("1"));
        assertFalse(manager.containsSupplier("999"));
    }

    @Test
    void testGetAllSuppliers() {
        manager.addSupplier(supplier);
        Map<String, Supplier> all = manager.getAllSuppliers();
        assertEquals(1, all.size());
        assertTrue(all.containsKey("1"));
    }

    @Test
    void testClearAllSuppliers() {
        manager.addSupplier(supplier);
        manager.clearAllSuppliers();
        assertEquals(0, manager.getAllSuppliers().size());
    }
    @Test
    void testSupplierFromDTO_NullDTO_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Supplier(null));
    }

    @Test
    void testSupplierFromDTO_ValidDTO() {
        SupplierDTO dto = new SupplierDTO("1", "Name", "123", new ArrayList<>(), DaysDTO.TUESDAY);
        Supplier supplier = new Supplier(dto);

        assertEquals("1", supplier.getId());
        assertEquals("Name", supplier.getName());
        assertEquals("123", supplier.getBankAccount());
        assertEquals(Days.TUESDAY, supplier.getDaySup());
        assertTrue(supplier.getContact().isEmpty());
    }

    @Test
    void testAddSingleContact() {
        SupplierDTO dto = new SupplierDTO("2", "Supp", "456", new ArrayList<>(), DaysDTO.WEDNESDAY);
        Supplier supplier = new Supplier(dto);

        Contact c = new Contact("1","Alice", "1234", "alice@ex.com");
        supplier.addContact(c);
        assertEquals(1, supplier.getContact().size());
    }

    @Test
    void testAddContactList_WithDuplicate() {
        ContactDTO c1 = new ContactDTO("1","Bob", "5678", "bob@ex.com");
        Contact contact = new Contact(c1);

        SupplierDTO dto = new SupplierDTO("3", "Supp3", "999", new ArrayList<>(Collections.singletonList(c1)), DaysDTO.FRIDAY);
        Supplier supplier = new Supplier(dto);

        supplier.addContact(Collections.singletonList(contact)); // Adding same contact
        assertEquals(1, supplier.getContact().size()); // Should not add duplicate
    }

}
