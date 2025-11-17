import ObjectDTO.Supplier.*;
import domainLayer.Supplier.Managers.AgreementManager;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AgreementTest {

    private AgreementManager manager;
    private AgreementDTO agreement;
    private SupItemDTO item;

    @BeforeEach
    void setUp() {
        manager = new AgreementManager();

        ManufacturerDTO manufacturer = new ManufacturerDTO("M001", "PepsiCo");
        ProductDTO product = new ProductDTO("P001", "Cola");

        List<DiscountDTO> discounts = List.of(
                new DiscountDTO(10, 5.0),
                new DiscountDTO(20, 10.0)
        );

        item = new SupItemDTO(product, manufacturer, "S001", "A001", 10.5, "0", discounts);

        Map<String, SupItemDTO> items = new ConcurrentHashMap<>();
        items.put(item.getSupplierCatNum(), item);

        agreement = new AgreementDTO("A001", "S001", items, true, "17-4-2025,visa");

        manager.removeAgreement("A001"); // ensure clean slate before each test
    }

    @AfterEach
    void tearDown() {
        manager.removeAgreement("A001");
    }

    @Test
    void testAddAndGetAgreement() {
        manager.addAgreement(agreement);

        var fetched = manager.getAgreementById("A001");
        assertNotNull(fetched);
        assertEquals("A001", fetched.getAgreementId());
        assertEquals("S001", fetched.getSupplierId());
        assertTrue(fetched.isSupplierDeliverIt());
        assertEquals(1, fetched.getItems().size());
    }

    @Test
    void testRemoveAgreement() {
        manager.addAgreement(agreement);
        assertTrue(manager.removeAgreement("A001"));
        assertNull(manager.getAgreementById("A001"));
    }

    @Test
    void testGetAgreementsBySupplier() {
        manager.addAgreement(agreement);
        var agreements = manager.getAgreementsBySupplier("S001");
        assertEquals(1, agreements.size());
        assertEquals("A001", agreements.get(0).getAgreementId());
    }

    @Test
    void testContainsAgreement() {
        manager.addAgreement(agreement);
        assertTrue(manager.containsAgreement("A001"));
        assertFalse(manager.containsAgreement("Unknown"));
    }

    @Test
    void testClearAll() {
        manager.addAgreement(agreement);
        manager.clearAll();
        assertTrue(manager.getAllAgreements().isEmpty());
    }

    @Test
    void testUpdateDelivery() {
        manager.addAgreement(agreement);
        manager.updateDeliveryOfSupplier("A001", false);
        var updated = manager.getAgreementById("A001");
        assertNotNull(updated);
        assertFalse(updated.isSupplierDeliverIt());
    }

    @Test
    void testUpdatePaymentType() {
        manager.addAgreement(agreement);
        manager.updatePaymentType("A001", "cash");
        var updated = manager.getAgreementById("A001");
        assertEquals("cash", updated.getPayment());
    }

    @Test
    void testRemoveItemsOF() {
        manager.addAgreement(agreement);
        manager.removeItemsOF("A001");
        var updated = manager.getAgreementById("A001");
        assertTrue(updated.getItems().isEmpty());
    }

    @Test
    void testAddItemToAgreement() {
        manager.addAgreement(agreement);
        SupItemDTO newItem = new SupItemDTO(
                new ProductDTO("P002", "Sprite"),
                new ManufacturerDTO("M002", "CocaCola"),
                "S001", "A001",
                12.0, "1",
                List.of(new DiscountDTO(10, 2.5))
        );
        manager.addItemToAgreement("A001", "P002", newItem);
        var updated = manager.getAgreementById("A001");
        assertEquals(2, updated.getItems().size());
        assertTrue(updated.containItem("P002"));
    }

    @Test
    void testRemoveItemFromAgreement() {
        manager.addAgreement(agreement);
        SupItemDTO removed = manager.remove_item_from_agreement("A001", "P001");
        assertNotNull(removed);
        var updated = manager.getAgreementById("A001");
        assertFalse(updated.containItem("P001"));
    }

    //False test [exceptions]
    @Test
    void testAddNullAgreement() {
        assertThrows(IllegalArgumentException.class, () -> manager.addAgreement( null));
    }
    @Test
    void testRemoveNullAgreement() {
        assertThrows(IllegalArgumentException.class, () -> manager.removeAgreement( null));
    }
}