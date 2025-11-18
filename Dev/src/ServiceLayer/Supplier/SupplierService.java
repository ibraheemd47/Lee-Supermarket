package ServiceLayer.Supplier;

import ObjectDTO.Supplier.ContactDTO;
import ObjectDTO.Supplier.SupplierDTO;

import ServiceLayer.Inventory.Response;
import domainLayer.Supplier.Factory;
import domainLayer.Supplier.Managers.SupplierManager;


import java.util.List;

public class SupplierService {
     private static SupplierService instance = null;
     private SupplierManager manager;
     private Response response;

     private SupplierService() {
       manager = Factory.getInstance().getSupplierManager();
     }
     public static SupplierService getInstance() {
         if (instance == null) {
             instance = new SupplierService();
         }
         return instance;
     }
     public String addSupplier(SupplierDTO supplier) {
        String result = "";
         try {
             result = manager.addSupplier(supplier);
             response = Response.success(result);
        }
        catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
         return response.toString();
     }

     public Response getAllSuppliers() {
         List<SupplierDTO> result = null;
         try{
             result =  manager.getAllSuppliersDTO();
             response = Response.success(result);
         }catch (Exception e) {
             response = Response.failure(e.getMessage());
         }
         return response;

     }

     public List<SupplierDTO> getSupplierLst_by_itemID(String item_id) {
       return null;
     }

     public Response findSupplierById(String supplier_id) {
         SupplierDTO result = null;
         try {
             result = manager.findSupplierById(supplier_id);
             response = Response.success(result);
         }
         catch (Exception e) {
             response = Response.failure(e.getMessage());
         }
         return response;
     }

     public boolean delete_supplier(String supplier_id) {
         try{
             return manager.removeSupplier(supplier_id);
         }
         catch (Exception e) {
            return false;
         }
     }

     public Response get_contact(String supplierID , String contactNumber) {
         ContactDTO result = null;
         try{
             result = manager.getContact(supplierID, contactNumber);
             response = Response.success(result);
         }
         catch (Exception e) {
             response = Response.failure(e.getMessage());
         }
         return response;
     }

     public String addContactToSupplier(String supplierID, ContactDTO contact) {
         String result = "";
         try {
             result = manager.addContact(supplierID, contact);
             response = Response.success(result);
         }
         catch (Exception e) {
             response = Response.failure(e.getMessage());
         }
         return response.toString();
     }

     public String update_supplier(String supplier_id, SupplierDTO supplier) {
         String result = "";
         try {
             result = manager.updateSupplier(supplier);
             response = Response.success(result);
         } catch (Exception e) {
             response = Response.failure(e.getMessage());
         }
         return response.toString();
     }


    public String remove_contact_from_supplier(String supplierId, ContactDTO contact) {
        String result = "";
        try {
            result = manager.deleteContact(supplierId,contact);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }

    public String clear_suppliers() {
        String result = "";
        try {
            result = manager.clearAllSuppliers();
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }


}
