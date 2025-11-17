import ObjectDTO.Supplier.ContactDTO;
import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.SupplierDTO;
import ServiceLayer.Supplier.SupplierService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SupplierServiceIntegrationTest {

    SupplierService supplierService;

    @BeforeAll
    public void setup() {
        supplierService = SupplierService.getInstance();
    }

    @Test
    public void testAddFindDeleteSupplier() {
        List<ContactDTO> contacts = new ArrayList<>();
        contacts.add(new ContactDTO("123456789", "John Doe", "053","john@example.com"));

        DaysDTO days = DaysDTO.FRIDAY; // assuming default constructor or fill as needed

        SupplierDTO supplier = new SupplierDTO("sup01", "Supplier One", "Bank001", contacts, days);
        supplierService.addSupplier(supplier);


        SupplierDTO found = (SupplierDTO) supplierService.findSupplierById("sup01").getMessage();
        assertNotNull(found);
        assertEquals("Supplier One", found.getName());

        boolean deleted = supplierService.delete_supplier("sup01");
        assertTrue(deleted);

        SupplierDTO afterDelete = (SupplierDTO) supplierService.findSupplierById("sup01").getMessage();
        assertNull(afterDelete);
    }

    @Test
    public void testAddAndRemoveContact() {
        List<ContactDTO> contacts = new ArrayList<>();
        ContactDTO contact = new ContactDTO("987654321", "Jane Doe","054", "jane@example.com");
        contacts.add(contact);

        DaysDTO days = DaysDTO.MONDAY;

        SupplierDTO supplier = new SupplierDTO("sup02", "Supplier Two", "Bank002", contacts, days);
        supplierService.addSupplier(supplier);

        ContactDTO newContact = new ContactDTO("111222333", "New Contact","052", "new@example.com");
        supplierService.addContactToSupplier("sup02", newContact);

        ContactDTO fetched =(ContactDTO) supplierService.get_contact("sup02", "052").getMessage();
        assertNotNull(fetched);
        assertEquals("New Contact", fetched.getName());

        supplierService.remove_contact_from_supplier("sup02", newContact);

        ContactDTO removedContact =(ContactDTO) supplierService.get_contact("sup02", "111222333").getMessage();
        assertNull(removedContact);

        supplierService.delete_supplier("sup02");
    }

    @AfterAll
    public void cleanup() {
        supplierService.clear_suppliers();
    }
}