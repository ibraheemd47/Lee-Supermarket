package domainLayer.Supplier.Managers;

//import Do.RepositoryIMP;
import ObjectDTO.Supplier.ContactDTO;
import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.SupplierDTO;
import domainLayer.Supplier.Objects.Contact;
import domainLayer.Supplier.Objects.Supplier;
import domainLayer.Supplier.Repository;
import domainLayer.Supplier.RepositoryIMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierManager {

    /// data base
    Repository dataBase = RepositoryIMP.getInstance();


    private final Map<String, Supplier> suppliers;

    public SupplierManager() {
        this.suppliers = new HashMap<>();
    }

    public String addSupplier(SupplierDTO supplier) {
        if (supplier == null || supplier.getId() == null) {
            throw new IllegalArgumentException("Supplier or Supplier ID cannot be null");
        }
        if(suppliers.containsKey(supplier.getId()))
        {
            throw new IllegalArgumentException("Supplier with this id is found");
        }
        suppliers.put(supplier.getId(), new Supplier(supplier));
        DaysDTO daysToAdd=null;
        if(supplier.getDaySup()!=null)
            daysToAdd=DaysDTO.fromString(supplier.getDaySup().toString());

        //SupplierDTO supplierDTO=new SupplierDTO(supplier.getId(),supplier.getName(),supplier.getBankAccount(),new ArrayList<>(),daysToAdd);
        dataBase.insertSupplier(supplier);
        return "Supplier " + supplier.getId() + " added successfully!!";
    }

    public Supplier getSupplierById(String id) {
        //check if i have here without need to go to the db
        if (suppliers.containsKey(id)) {
            return suppliers.get(id);
        }

        //else go get it from the db
        SupplierDTO dto = dataBase.getSupplierById(id);
        if (dto == null) {
            return null;
        }

        Supplier supplier = new Supplier(dto); // Assuming you have a constructor in Supplier to convert from DTO
        suppliers.put(id, supplier);
        return supplier;
    }


    public boolean removeSupplier(String id) {
        Supplier toRemove = suppliers.remove(id);
        if (toRemove != null) {
            dataBase.deleteSupplier(id);  // Remove from database
            return true;
        } else {
            return false;
        }
    }


    public boolean containsSupplier(String id) {
        return suppliers.containsKey(id);
    }

    public Map<String, Supplier> getAllSuppliers() {
        suppliers.clear();//get all from db
        HashMap<String, SupplierDTO> allSuppliersDTO = dataBase.getAllSuppliers();
        for (SupplierDTO dto : allSuppliersDTO.values()) {
            Supplier supplier = new Supplier(dto);  // again assuming a DTO constructor
            suppliers.put(supplier.getId(), supplier);
        }
        return new HashMap<>(suppliers);
    }

    public List<SupplierDTO> getAllSuppliersDTO() {
         HashMap<String, SupplierDTO> allSuppliersDTO = dataBase.getAllSuppliers();
         return new ArrayList<>(allSuppliersDTO.values());
    }

    public String clearAllSuppliers() {
        suppliers.clear();
        dataBase.deleteAllSuppliers();
        return "All suppliers deleted successfully!!";
    }

    public SupplierDTO findSupplierById(String id) {
        if(suppliers.containsKey(id))
            return suppliers.get(id).ConvertToDTO();
        SupplierDTO supplier=dataBase.getSupplierById(id);
        if (supplier != null) {
            suppliers.put(supplier.getId(),new Supplier(supplier));
        }
        return supplier;
    }

    public ContactDTO getContact(String supplierId, String contactNumber) {
        Contact toReturn=null;
        Supplier supplier=suppliers.get(supplierId);
        if(supplier==null||supplier.getContact()==null){
            throw new IllegalArgumentException("Contact not found or Supplier not found.");
        }
        List<Contact> con_list=supplier.getContact();
        if(con_list==null|| con_list.isEmpty()){
            throw new IllegalArgumentException("Contact not found.");
        }
        for(Contact contact:con_list){
            if(contact.getPhone().equals(contactNumber)){
                toReturn= contact;
            }
        }
        return (toReturn!=null)?toReturn.ConvertToDTO():null;
    }
    public String updateSupplier(SupplierDTO supplier) {//todo: check if needed
        if (supplier == null || supplier.getId() == null) {
            throw new IllegalArgumentException("Supplier or ID cannot be null");
        }
        suppliers.replace(supplier.getId(), new Supplier(supplier));
        SupplierDTO dto = new SupplierDTO(supplier.getId(), supplier.getName(),
                supplier.getBankAccount(), new ArrayList<>(),
                DaysDTO.fromString(supplier.getDaySup().toString()));
        dataBase.updateSupplier(dto); // Update in DB
        return "Supplier " + supplier.getId() + " updated successfully!!";
    }

    public String addContact(String supplierId, ContactDTO contact) {
        Supplier supplier = getSupplierById(supplierId);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found");
        }
        supplier.getContact().add(new Contact(contact));
        //ContactDTO dto = contact.ConvertToDTO();  // assuming DTO constructor
        dataBase.insertContact(contact, supplierId);
        return "Contact " + contact.getPhoneNumber() + " added successfully!!";
    }

    public void updateContact(String supplierId, Contact contact) {
        ContactDTO dto = contact.ConvertToDTO();
        dataBase.updateContact(dto);
    }

    public String deleteContact(String supplierId, ContactDTO contact) {
//        ContactDTO dto = contact.ConvertToDTO();
        dataBase.deleteContact(contact.getId());
        Supplier supplier = getSupplierById(supplierId);
        supplier.getContact().removeIf(c -> c.getPhone().equals(contact.getPhoneNumber()));
        return "Contact " + contact.getPhoneNumber() + " deleted successfully!!";
    }



}
