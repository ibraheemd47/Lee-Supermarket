
package domainLayer.Supplier.Objects;
import ObjectDTO.Supplier.ManufacturerDTO;

public class Manufacturer {
    private String manfacturer_id;
    private String name;

    public Manufacturer() {
    }

    public Manufacturer(String manfacturer_id , String name) {
        this.manfacturer_id = manfacturer_id;
        this.name = name;
    }

    public Manufacturer(ManufacturerDTO manufacturer) {
        this.manfacturer_id=manufacturer.manufacturer_id();
        this.name=manufacturer.manufacturer_name();

    }

    public String getManfacturer_id() {
        return manfacturer_id;
    }

    public void setManfacturer_id(String manfacturer_id) {
        this.manfacturer_id = manfacturer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Manufacturer ID: " + manfacturer_id + ", Name: " + name;
    }

    public ManufacturerDTO toDTO() {
        return new ManufacturerDTO(manfacturer_id, name);
    }
}