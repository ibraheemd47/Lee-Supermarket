import ObjectDTO.Supplier.ContactDTO;
import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.SupplierDTO;
import PresentationLayer.Supplier.Controllers.ServiceController;
import PresentationLayer.Supplier.Controllers.SupplierController;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SupplierIntegrationTest {

    private static SupplierController supplierController;
    private static final String SUPPLIER_ID = "sup123";

    @BeforeAll
    public static void setup() {
        //ServiceController.getInstance().initialize();
        supplierController = new SupplierController();
        supplierController.clearSuppliers(); // Clean slate
    }

    @Test
    @Order(1)
    public void testAddSupplier() {
        List<ContactDTO> contacts = new ArrayList<>();
        contacts.add(new ContactDTO("John Doe", "123456789","054", "john@example.com"));
        DaysDTO deliveryDays = DaysDTO.MONDAY;
        supplierController.addSupplier(SUPPLIER_ID, "Test Supplier", "Bank XYZ", contacts, deliveryDays);

        SupplierDTO retrieved = supplierController.findSupplier(SUPPLIER_ID);
        assertNotNull(retrieved);
        assertEquals("Test Supplier", retrieved.getName());
        assertEquals("Bank XYZ", retrieved.getBankAccount());
        assertEquals(1, retrieved.getContact().size());
    }

    @Test
    @Order(2)
    public void testUpdateSupplier() {
        DaysDTO newDays = DaysDTO.SATURDAY;
        supplierController.updateSupplier(SUPPLIER_ID, "Updated Supplier", "New Bank", newDays);

        SupplierDTO updated = supplierController.findSupplier(SUPPLIER_ID);
        assertEquals("Updated Supplier", updated.getName());
        assertEquals("New Bank", updated.getBankAccount());
        assertEquals(newDays, updated.getDaySup());
    }

    @Test
    @Order(3)
    public void testAddAndRemoveContact() {
        ContactDTO newContact = new ContactDTO("Jane Smith", "987654321","053", "jane@example.com");
        supplierController.addContactToSupplier(SUPPLIER_ID, newContact);

        SupplierDTO supplier = supplierController.findSupplier(SUPPLIER_ID);
        assertTrue(supplier.getContact().stream().anyMatch(c -> c.getPhoneNumber().equals("053")));

        supplierController.removeContactFromSupplier(SUPPLIER_ID, newContact);

        SupplierDTO updated = supplierController.findSupplier(SUPPLIER_ID);
        assertFalse(updated.getContact().stream().anyMatch(c -> c.getPhoneNumber().equals("053")));
    }

    @Test
    @Order(4)
    public void testDeleteSupplier() {
        supplierController.delete_supplier(SUPPLIER_ID);
        SupplierDTO deleted = supplierController.findSupplier(SUPPLIER_ID);
        assertNull(deleted);
    }

    @Test
    @Order(5)
    public void testAddDuplicateSupplier_ShouldFailGracefully() {
        List<ContactDTO> contacts = new ArrayList<>();
        contacts.add(new ContactDTO("John", "123456789", "054", "john@example.com"));
        DaysDTO deliveryDays = DaysDTO.SUNDAY;

        supplierController.addSupplier(SUPPLIER_ID, "Supplier One", "Bank A", contacts, deliveryDays);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            supplierController.addSupplier(SUPPLIER_ID, "Supplier Duplicate", "Bank B", contacts, deliveryDays);
        });
        System.out.println( exception.getMessage());
        String msg = exception.getMessage();
        assertTrue(msg != null && (msg.contains("Supplier with this id is found") || msg.contains("duplicate id")),
                "Expected exception message about duplication, got: " + msg);
        supplierController.delete_supplier(SUPPLIER_ID);
    }
}
