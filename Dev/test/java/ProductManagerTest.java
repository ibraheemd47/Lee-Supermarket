import ObjectDTO.Supplier.ProductDTO;
import domainLayer.Supplier.Managers.ProductManager;
import domainLayer.Supplier.Objects.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductManagerTest {

    private ProductManager productManager;

    @BeforeEach
    void setUp() {
        productManager = new ProductManager();
    }

    @Test
    void testAddProductUsingDTO() {
        ProductDTO dto = new ProductDTO("1", "Apple");
        productManager.addProduct(dto);

        ProductDTO retrieved = productManager.getProductById("1");
        assertEquals("1", retrieved.getId());
        assertEquals("Apple", retrieved.getName());
    }

    @Test
    void testAddProductUsingObject() {
        Product product = new Product("2", "Banana");
        productManager.addProduct(product);

        ProductDTO retrieved = productManager.getProductById("2");
        assertEquals("2", retrieved.getId());
        assertEquals("Banana", retrieved.getName());
    }

    @Test
    void testGetProductDTOByName() {
        ProductDTO dto = new ProductDTO("3", "Carrot");
        productManager.addProduct(dto);

        ProductDTO retrieved = productManager.getProductDTOByName("Carrot");
        assertEquals("3", retrieved.getId());
        assertEquals("Carrot", retrieved.getName());
    }

    @Test
    void testRemoveProductById() {
        ProductDTO dto = new ProductDTO("4", "Donut");
        productManager.addProduct(dto);

        boolean removed = productManager.removeProductById("4");
        assertTrue(removed);

        // Try to get again
        ProductDTO result = productManager.getProductById("4");
        assertNull(result); // Will be null unless loaded from DB
    }

    @Test
    void testExistsTrue() {
        ProductDTO dto = new ProductDTO("5", "Egg");
        productManager.addProduct(dto);

        assertTrue(productManager.exists("5"));
    }

    @Test
    void testExistsFalse() {
        assertFalse(productManager.exists("non-existing-id"));
    }
}