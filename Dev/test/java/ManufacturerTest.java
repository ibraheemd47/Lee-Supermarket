import ObjectDTO.Supplier.ManufacturerDTO;
import ServiceLayer.ServiceFactory;
import domainLayer.Supplier.Objects.Manufacturer;
import domainLayer.Supplier.Managers.ManufacturerManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ManufacturerTest {

    private Manufacturer manufacturer;
    private ManufacturerManager manager;

    @BeforeEach
    void setUp() {

        manufacturer = new Manufacturer("M001", "Acme Inc.");
        manager = new ManufacturerManager();
    }
    @AfterEach
    void tearDown() {
        manager = null;
        manufacturer = null;
    }

    @Test
    void testManufacturerConstructorAndGetters() {
        assertEquals("M001", manufacturer.getManfacturer_id());
        assertEquals("Acme Inc.", manufacturer.getName());
    }

    @Test
    void testManufacturerSetters() {
        manufacturer.setManfacturer_id("M002");
        manufacturer.setName("Tech Corp");
        assertEquals("M002", manufacturer.getManfacturer_id());
        assertEquals("Tech Corp", manufacturer.getName());
    }

    @Test
    void testManufacturerToString() {
        String expected = "Manufacturer ID: M001, Name: Acme Inc.";
        assertEquals(expected, manufacturer.toString());
    }

    @Test
    void testAddAndFindManufacturer() {
        manager.addManufacturer(manufacturer);
        ManufacturerDTO found = manager.findManufacturerById("M001");
        assertNotNull(found);
        assertEquals("Acme Inc.", found.manufacturer_name());
    }

    @Test
    void testRemoveManufacturer() {
        manager.addManufacturer(manufacturer);
        boolean removed = manager.removeManufacturer("M001");
        assertTrue(removed);
        assertNull(manager.findManufacturerById("M001"));
    }

    @Test
    void testExistsManufacturer() {
        manager.addManufacturer(manufacturer);
        assertTrue(manager.exists("M001"));
        assertFalse(manager.exists("UnknownID"));
    }

    @Test
    void testGetAllManufacturers() {
        manager.deleteAllManufacturer();
        Manufacturer manufacturer = new Manufacturer("M001", "zaki");
        manager.addManufacturer(manufacturer);
        Map<String, Manufacturer> all = manager.getAllManufacturers();
        for(Manufacturer m : all.values()){
            System.out.println(m.toString());
        }
        assertEquals(1, all.size());
        assertTrue(all.containsKey("M001"));
    }

    //False test [exceptions]
    @Test
    void testAddManufacturerWithNullId() {
        Manufacturer invalid = new Manufacturer(null, "Invalid");
        assertThrows(IllegalArgumentException.class, () -> manager.addManufacturer(invalid));
    }

    @Test
    void testAddNullManufacturer() {
        assertThrows(IllegalArgumentException.class, () -> manager.addManufacturer((Manufacturer) null));
    }
}
