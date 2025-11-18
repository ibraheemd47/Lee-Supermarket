package domainLayer.Supplier.Managers;


import ObjectDTO.Supplier.ManufacturerDTO;
import domainLayer.Supplier.Objects.Manufacturer;
import domainLayer.Supplier.Repository;
import domainLayer.Supplier.RepositoryIMP;

import java.util.HashMap;
import java.util.Map;

public class ManufacturerManager {
    private final Map<String, Manufacturer> manufacturers;
    Repository dataBase = RepositoryIMP.getInstance();

    public ManufacturerManager() {
        this.manufacturers = new HashMap<>();
    }

    public void addManufacturer(Manufacturer manufacturer) {
        if (manufacturer == null || manufacturer.getManfacturer_id() == null) {
            throw new IllegalArgumentException("Invalid manufacturer or ID");
        }
        manufacturers.put(manufacturer.getManfacturer_id(), manufacturer);
    }

    public String addManufacturer(ManufacturerDTO manufacturer) {
        if (manufacturer == null || manufacturer.manufacturer_id() == null) {
            throw new IllegalArgumentException("Invalid manufacturer or ID");
        }
        Manufacturer manufacturerToAdd = new Manufacturer(manufacturer);
        manufacturers.put(manufacturerToAdd.getManfacturer_id(), manufacturerToAdd);
        dataBase.add_manufacturer(manufacturerToAdd);
        return "Manufacturer " + manufacturerToAdd.getManfacturer_id() + " added successfully!!";
    }

    public ManufacturerDTO findManufacturerById(String id) {
        if (manufacturers.get(id) != null)
            return manufacturers.get(id).toDTO();

        return dataBase.getManufacurer(id);
    }

    public boolean removeManufacturer(String id) {
        boolean removed = manufacturers.remove(id) != null;

        removed = removed || dataBase.removeManufacurer(id);
        return removed;
    }

    public Map<String, Manufacturer> getAllManufacturers() {
        Map<String , String > map= dataBase.getAllManufacturer();
        Map<String, Manufacturer> toRetutn=new HashMap<>();
        for(var x : map.entrySet()){
            toRetutn.put(x.getKey(),new Manufacturer(x.getKey(),x.getValue()));
        }
        toRetutn.putAll(manufacturers);
        return new HashMap<>(toRetutn);

    }

    public boolean exists(String id) {

        if (manufacturers.containsKey(id))
            return true;
        return dataBase.existManufacturer(id);
    }

    //for test
    public void deleteAllManufacturer() {
        manufacturers.clear();
        dataBase.deleteAllManufacturers();
    }
}
