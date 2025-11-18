package PresentationLayer.Supplier.Controllers;

import java.util.List;

import ObjectDTO.Supplier.*;
import ServiceLayer.Inventory.Response;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Supplier.AgreementService;
import ServiceLayer.Supplier.ManufacturerService;
import ServiceLayer.Supplier.ProductService;
import ServiceLayer.Supplier.SupplierService;

public class SupplierController {
    private final SupplierService supplierService;
    private final AgreementService agreementService;
    private final ManufacturerService manufacturerService;
    public SupplierController() {
        this.supplierService = ServiceController.getInstance().getServiceFactory().getSupplierService();
        this.agreementService = ServiceController.getInstance().getServiceFactory().getAgreementService();
        this.manufacturerService = ServiceController.getInstance().getServiceFactory().getManufacturerService();
    }


    public void displaySuppliers() {
        Response response =supplierService.getAllSuppliers();
        List<SupplierDTO> suppliers = null;
        if(!response.isError()){
            suppliers = (List<SupplierDTO>) response.getMessage();
        }
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found");
        }
        for (SupplierDTO supplier : suppliers) {
            System.out.println(supplier);
        }
    }

    public String addSupplier(String id, String name, String bank, List <ContactDTO> contact , DaysDTO daySup) {
        SupplierDTO s = new SupplierDTO(id, name, bank,contact,daySup);
        String output = supplierService.addSupplier(s);//for test
        if(output.contains("Supplier with this id is found"))
        {
            throw new IllegalArgumentException("Supplier with this id is found");//for test
        }
        return output;
    }

    public SupplierDTO findSupplier(String id) {
        Response response = supplierService.findSupplierById(id);
        if (response.isError()) {
            return null;
        }
        else {
            return (SupplierDTO) response.getMessage();
        }
    }

    public void delete_supplier(String supplierId) {
        if(supplierId==null || supplierId.isEmpty()){
            throw new IllegalArgumentException("Supplier ID cannot be null or empty");
        }
        else {
            if(!supplierService.delete_supplier(supplierId))
                System.out.println("Supplier with ID " + supplierId + " could not be deleted(Not found!).");

        }
    }

    public void add_agreement(AgreementDTO agreement) {
        if (agreement == null) {
            throw new IllegalArgumentException("Agreement cannot be null.");
        }
        String output= agreementService.add_agreement(agreement);
        System.out.println(output);
    }

    public ManufacturerDTO find_Manfacurer(String manufacturerId) {
        if(manufacturerId==null || manufacturerId.isEmpty()){
            throw new IllegalArgumentException("Manufacturer ID cannot be null or empty");
        }
        ManufacturerDTO man=manufacturerService.find_manufacturer(manufacturerId).isError() ? null : (ManufacturerDTO) manufacturerService.find_manufacturer(manufacturerId).getMessage();
        if (man == null) {
            System.out.println("Manufacturer with id " + manufacturerId + " not found");
        }
        return man;
    }

    public List<AgreementDTO> getAllAgreementOf(String supplierId) {
        return agreementService.getAllAgreementOf(supplierId).isError() ? null : (List<AgreementDTO>) agreementService.getAllAgreementOf(supplierId).getMessage();
    }

    public boolean delete_agreement(String agreementId) {
        return agreementService.delete_agreement(agreementId);
    }

    public String update_DeliveryOfSupplier(String agID, boolean supplierDeliverIt) {
        return agreementService.update_DeliveryOfSupplier(agID, supplierDeliverIt);
    }

    public String removeItems(String agreementId) {

        return agreementService.removeItems(agreementId);
    }

    public String addItem(String agID,String itemID, SupItemDTO toAdd) {
        return agreementService.addItem(agID,itemID,toAdd);
    }
    public AgreementDTO getAgreement(String agID) {
        return agreementService.getAgreement(agID).isError() ? null : (AgreementDTO) agreementService.getAgreement(agID).getMessage();
    }

    public String updatePayment(String agID, String payment) {
        return agreementService.updatePayment(agID,payment);
    }
    public void addContactToSupplier(String supplierId, ContactDTO contact) {
        if (supplierId == null || contact == null) {
            throw new IllegalArgumentException("Supplier ID or contact cannot be null");
        }
        String output = supplierService.addContactToSupplier(supplierId, contact);
        System.out.println(output);
    }

    public void updateSupplier(String id, String name, String bank, DaysDTO daySup) {
        SupplierDTO existing = (SupplierDTO) supplierService.findSupplierById(id).getMessage();;
        if (existing == null) {
            throw new IllegalArgumentException("Supplier not found");
        }
        existing.setName(name);
        existing.setBankAccount(bank);
        existing.setDaySup(daySup);
        String output= supplierService.update_supplier(existing.getId(), existing);
        System.out.println(output);
    }

    public void removeContactFromSupplier(String supplierId, ContactDTO contact) {
        if (supplierId == null || contact == null) {
            throw new IllegalArgumentException("Supplier ID or contact cannot be null");
        }
        String output= supplierService.remove_contact_from_supplier(supplierId,contact);
        System.out.println(output);
    }


    public ContactDTO findContact(String supplierId, String phone) {
        return supplierService.get_contact(supplierId,phone).isError() ? null : (ContactDTO) supplierService.get_contact(supplierId,phone).getMessage();
    }



    public void clearSuppliers() {
        String output = supplierService.clear_suppliers(); // Clears only memory
        System.out.println(output);
    }


    public SupItemDTO remove_item_from_agreement(String agreement_id, String productID) {
        var deleted =agreementService.remove_item_from_agreement(agreement_id,productID);
        if(deleted.isError()){
            return null;
        }
        return (SupItemDTO)deleted.getMessage();
       //return  agreementService.remove_item_from_agreement(agreement_id,productID).isError() ? null : (SupItemDTO) agreementService.remove_item_from_agreement(agreement_id,productID).getMessage();
    }

    public ProductDTO getProudct(String productId) {
        return ProductService.getInstance().get_product_by_Id(productId).isError() ? null : (ProductDTO) ProductService.getInstance().get_product_by_Id(productId).getMessage();
    }

    public String addProduct(ProductDTO productDTO) {
       return ProductService.getInstance().add_product(productDTO);
    }

    public String add_manufacturer(ManufacturerDTO manu) {
        return manufacturerService.add_manufacturer(manu);
    }

    public void add_Item_inventory(String id, String supplierId) {
        if(!ServiceFactory.getInstance().addSupplier(supplierId,Integer.parseInt(id)))
            System.out.println("[ERROR] adding the supplier item  ]"+supplierId+ " item "+id);
    }

    public void remove_supItem_inventory(String supplierId, int productID) {
        if(!ServiceFactory.getInstance().removeSupplier(supplierId,productID))
            System.out.println("[ERROR] removeing the supplier item  ]");
    }

    public boolean checkItemAgreement(String agId , String productId) {
        return ServiceFactory.getInstance().checkItemAgreement(agId,productId);
    }
}