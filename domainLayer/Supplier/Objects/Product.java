package domainLayer.Supplier.Objects;
import ObjectDTO.Supplier.ProductDTO;

public class Product {
    private String id;
    private String name;

    public Product(String id, String name) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        this.id = id;
        this.name = name;

    }

    public Product(ProductDTO productDTO) {
        this.id = productDTO.getId();
        this.name = productDTO.getName();

    }
    public String toString() {
       return "Product [id=" + id + ", name=" + name + "]";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public ProductDTO convertToDTO() {
        return new ProductDTO(id, name);
    }
}