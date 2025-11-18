package ServiceLayer.Supplier;

import ObjectDTO.Supplier.ProductDTO;
import ObjectDTO.Supplier.SupItemDTO;
import ServiceLayer.Inventory.Response;
import domainLayer.Supplier.Factory;
import domainLayer.Supplier.Managers.ProductManager;


public class ProductService {
    ProductManager manager;
    private static ProductService instance=null;
    private Response response;
    private ProductService() {
        manager = new ProductManager();
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    public Response get_product_by_name(String productName,String supplier_id){
        SupItemDTO result = null;
        try {
            result = Factory.getInstance().getAgreementManager().checkProductByname(productName,supplier_id);
            response = Response.success(result);
        }catch (Exception e){
            response = Response.failure(e.getMessage());
        }
        return response;

    }

    public Response get_product_by_serialNumber(String productSerial , String supplierId){
        SupItemDTO result = null;
        try {
            result = Factory.getInstance().getAgreementManager().checkProductBySerialNumber(productSerial, supplierId);
            response = Response.success(result);
        }
        catch (Exception e){
            response = Response.failure(e.getMessage());
        }
        return response;
    }


    public Response get_product_by_Id(String productId) {

        ProductDTO result = null;
        try {
            result = manager.getProductById(productId);;
            response = Response.success(result);
        }
        catch (Exception e){
            response = Response.failure(e.getMessage());
        }
        return response;

    }

    public String add_product(ProductDTO productDTO) {
        String result = "";
        try {
            result =manager.addProduct(productDTO);
            response = Response.success(result);
        }
        catch(Exception e){
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }
}
