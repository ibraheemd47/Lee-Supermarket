package ServiceLayer.Inventory;

import ObjectDTO.Inventory.ItemDTO;
import domainLayer.Inventory.InventoryManager;
import java.util.Date;

public class ItemService {

    private final InventoryManager manager;
    private Response response;

    public ItemService(InventoryManager manager) {
        this.manager = manager;
    }

    public String showItems() {
        try {
            String result = manager.showItems();
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public ItemDTO getItemById(int id) {
        ItemDTO result = null ;
        try {
             result = manager.getItemById(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
            System.out.println("Error: get item by is item service " + e.getMessage());
        }
        return result;
    }

    public String addItem(String name, int categoryId, Date expirationDate, int quantityInWarehouse,
                          int quantityInStore, String supplier, String manufacturer, double buyingPrice,
                          double sellingPrice, int demand, String aisleAtStore, String shelfAtStore,
                          String aisleAtWarehouse, String shelfAtWarehouse) {
        try {
           int result = manager.addItem(name, categoryId, expirationDate, quantityInWarehouse, quantityInStore, supplier, manufacturer,
                    buyingPrice, sellingPrice, demand, aisleAtStore, shelfAtStore, aisleAtWarehouse, shelfAtWarehouse);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return (response.isError())?response.toString():response.getMessage().toString();
    }

    public String removeItem(int id) {
        try {
            manager.removeItem(id);
            response = Response.success("Item removed successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String updateItem(int id, String locationAtStoreAisle, String locationAtStoreShelf,
                             String locationAtWarehouseAisle, String locationAtWarehouseShelf, String name,
                             int categoryId, Date expirationDate, int quantityInStore, int quantityInWarehouse,
                             String supplier, double buyingPrice, double sellingPrice, int demand) {
        try {
            boolean result = manager.updateItem(id, locationAtStoreAisle, locationAtStoreShelf,
                    locationAtWarehouseAisle, locationAtWarehouseShelf, name, categoryId, expirationDate,
                    quantityInStore, quantityInWarehouse, supplier, buyingPrice, sellingPrice, demand);
          //  System.out.println("Update result: " + result);
            if (result)
                response = Response.success("Item updated successfully.");
            else
                response = Response.failure("Item not found.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String changeItemSupplierById(int id, String supplier) {
        try {
            manager.changeItemSupplierById(id, supplier);
            response = Response.success("Supplier changed successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String removeUnitFromItem(int id, String location, int quantity) {
        try {
            manager.removeUnitFromItem(id, location, quantity);
            response = Response.success("Unit removed successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String addUnitToItem(int id, String location, int quantity) {
        try {
            manager.addUnitToItem(id, location, quantity);
            response = Response.success("Unit added successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] Adding unit failed.]"+e.getMessage());
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String isExpired(int id) {
        try {
            boolean result = manager.isExpired(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String getQuantity(int id) {
        try {
            int result = manager.getQuantity(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String setQuantity(int id, int qty) {
        try {
            manager.setQuantity(id, qty);
            response = Response.success("Quantity updated successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String isBelowThreshold(int id) {
        try {
            boolean result = manager.isBelowThreshold(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }
    public boolean isBelowThresholdById(int id) {
        try {
            return manager.isBelowThreshold(id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    public String getBuyingPrice(int id) {
        try {
            double result = manager.getBuyingPrice(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String setBuyingPrice(int id, double price) {
        try {
            manager.setBuyingPrice(id, price);
            response = Response.success("Buying price updated successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String getSellingPrice(int id) {
        try {
            double result = manager.getSellingPrice(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String setSellingPrice(int id, double price) {
        try {
            manager.setSellingPrice(id, price);
            response = Response.success("Selling price updated successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String checkExpiredItems() {
        try {
            String result = manager.checkExpiredItems();
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }
    public boolean addSupplier(String supplierId, int item_id){
        return manager.addSupplier(supplierId, item_id);
    }
    public boolean removeSupplier(String supplierId, int item_id) {
        return manager.removeSupplier(supplierId, item_id);
    }
}
