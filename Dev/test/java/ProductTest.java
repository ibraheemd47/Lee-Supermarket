import ObjectDTO.Supplier.ProductDTO;
import domainLayer.Supplier.Objects.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product("P001", "Milk");
        productDTO = new ProductDTO("P002", "Bread");
    }

    @Test
    void testConstructorWithFields() {
        assertEquals("P001", product.getId());
        assertEquals("Milk", product.getName());
    }

    @Test
    void testConstructorWithDTO() {
        Product productFromDTO = new Product(productDTO);
        assertEquals("P002", productFromDTO.getId());
        assertEquals("Bread", productFromDTO.getName());
    }

    @Test
    void testConvertToDTO() {
        ProductDTO dto = product.convertToDTO();
        assertNotNull(dto);
        assertEquals("P001", dto.getId());
        assertEquals("Milk", dto.getName());
    }

    @Test
    void testToString() {
        String expected = "Product [id=P001, name=Milk]";
        assertEquals(expected, product.toString());
    }


    //False test [exceptions]
    @Test
    void testConstructorWithNullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(null, "Milk");
        });
        assertEquals("Product ID cannot be null", exception.getMessage());
    }
    @Test
    void testConstructorWithNullName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product("P001", null);
        });
        assertEquals("Product name cannot be null", exception.getMessage());
    }
}
