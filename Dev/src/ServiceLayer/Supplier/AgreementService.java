package ServiceLayer.Supplier;

import ObjectDTO.Supplier.AgreementDTO;
import ObjectDTO.Supplier.ProductDTO;
import ObjectDTO.Supplier.SupItemDTO;
import ObjectDTO.Supplier.SupplierDTO;
import ServiceLayer.Inventory.Response;
import domainLayer.Supplier.Managers.AgreementManager;
import domainLayer.Supplier.Managers.Pair;

import java.util.List;

public class AgreementService {
    private AgreementManager manager;
    private static AgreementService instance = null;
    private Response response;
    private AgreementService(){
        manager = new AgreementManager();
    }
    public static AgreementService getInstance(){
        if(instance == null){
            instance = new AgreementService();
        }
        return instance;
    }

    public boolean check_Agreement_with_supplierID(String supplierId) {
        try{
            return !manager.getAgreementsBySupplier(supplierId).isEmpty();
        }catch (Exception e){
            return false;
        }

    }

    public String add_agreement(AgreementDTO agreement) {
        String result = "";
        try {
            result = manager.addAgreement(agreement);
            response = Response.success(result);
        }catch (Exception e){
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }

    public Response getAllAgreementOf(String supplierId) {
        List<AgreementDTO> result = null;
        try {
            //todo implement
            result = manager.getAgreementsDTOBySupplier(supplierId);
            return Response.success(result);
        }catch (Exception e){
           response = Response.failure(e.getMessage());
        }
        return response;

    }

    public boolean delete_agreement(String agreementId) {
        try {
            return manager.removeAgreement(agreementId);
        }
        catch (Exception e){
            return false;
        }

    }

    public String update_DeliveryOfSupplier(String agID, boolean supplierDeliverIt) {
        String result = "";
        try {
            result= manager.updateDeliveryOfSupplier(agID,supplierDeliverIt);
            response = Response.success(result);
        }catch (Exception e){
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }

    public String removeItems(String agreementId) {
        String result = "";
        try {
            result =manager.removeItemsOF(agreementId);
            response = Response.success(result);
        }catch (Exception e){
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String addItem(String agID, String itemID, SupItemDTO toAdd) {
        String result = "";
        try {
           result = manager.addItemToAgreement(agID, itemID, toAdd);
           response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public Response getAgreement(String aglID) {
        AgreementDTO result = null;
        try{
            result = manager.getAgreementDTOById(aglID);
            return Response.success(result);
        }
        catch (Exception e){
            response = Response.failure(e.getMessage());
        }
        return response;
    }

    public String updatePayment(String aglID, String payment) {
        String result = "";
        try {
            result =manager.updatePaymentType(aglID, payment);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }

    public Response get_best_deal(List<Pair<Integer, ProductDTO>> ls) {
        AgreementDTO result = null;
        try {
            result = manager.get_best_deal(ls);
            response = Response.success(result);
        }
        catch (Exception e){
            response = Response.failure(e.getMessage());
        }
        return response;

    }

    public Response remove_item_from_agreement(String agreementId, String productID) {
        SupItemDTO result = null;
        try {
            result = manager.remove_item_from_agreement(agreementId,productID);
            response = Response.success(result);
        }
        catch (Exception e){
            response = Response.failure(e.getMessage());
        }
        return response;
    }

    public boolean check_item_include_with_supplier_agreement(SupplierDTO supplierDTO, ProductDTO productDTO) {
        try {
            return manager.check_item_include_with_supplier_agreement( supplierDTO,  productDTO);
        }
        catch (Exception e){
            return false;
        }

    }
    public boolean  checkItemAgreement(String aglID, String itemID) {
        return manager.isProudctExist(aglID,itemID);
    }
}
