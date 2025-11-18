package ServiceLayer.Inventory;


import domainLayer.Inventory.InventoryManager;

public class AlertService {
   private InventoryManager Manager;
   public Response response;

   public AlertService(InventoryManager manager){
       this.Manager = manager;
   }
    public String showAlerts() {
        try {
            String result = InventoryManager.getInstance().showAlerts();
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String checkItemsForAlert() {
        try {
            InventoryManager.getInstance().checkItemsForAlert();
            response = Response.success("Alerts checked successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

   
}
