package ObjectDTO.Supplier;

public class ProductDTO {//the product name , id
    private String id;
    private String name;

    public ProductDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}

