package ServiceLayer.Supplier;

import ObjectDTO.Supplier.ManufacturerDTO;
import ServiceLayer.Inventory.Response;
import domainLayer.Supplier.Managers.ManufacturerManager;

public class ManufacturerService {
    private static ManufacturerService instance;
    private ManufacturerManager manager;
    private Response response;
    private ManufacturerService() {
        manager = new ManufacturerManager();
    }
    public static ManufacturerService getInstance() {
        if (instance == null) {
            instance = new ManufacturerService();
        }
        return instance;
    }

    public Response find_manufacturer(String manufacturerID) {
        ManufacturerDTO result = null;
        try{
            result = manager.findManufacturerById(manufacturerID);
            return Response.success(result);
        }catch(Exception e){
            response = Response.failure(e.getMessage());
        }
        return response;

    }

    public String add_manufacturer(ManufacturerDTO manu) {
        String result = "";
        try {
            result =manager.addManufacturer(manu);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }

//    public String get_manufacturerID_by_ItemID(String itemID) {
//          todo:
//
//    }
}
