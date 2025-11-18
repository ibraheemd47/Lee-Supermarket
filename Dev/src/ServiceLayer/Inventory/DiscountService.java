package ServiceLayer.Inventory;

import domainLayer.Inventory.InventoryManager;
import java.util.Date;

public class DiscountService {
    private  InventoryManager manager;
    private Response response;

    public DiscountService(InventoryManager manager) {
        this.manager = manager;
    }

    public String createDiscount(String name, String type, double discountPercentage, Date startDate, Date endDate) {
        try {
            String result = manager.createDiscount(name, type, discountPercentage, startDate, endDate);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String showDiscounts() {
        try {
            String result = manager.showDiscounts();
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String showActiveDiscounts(int itemId) {
        try {
            String result = manager.showActiveDiscounts(itemId);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }
    public String showItemDiscounts(int itemId) {
        try {
            String result = manager.showItemDiscounts(itemId);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }
    public String applyDiscountForItem(int itemId, int discountId) {
        try {
            boolean result = manager.applyDiscountForItem(itemId, discountId);
            if (result)
                response = Response.success("Discount applied to item successfully.");
            else
                response = Response.failure("Item not found or discount application failed.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String getDiscountDetails(int id) {
        try {
            String result = manager.getDiscountDetails(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String isDiscountActive(int id) {
        try {
            boolean result = manager.isDiscountActive(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String setDiscountEndDate(int id, Date newEndDate) {
        try {
            manager.setDiscountEndDate(id, newEndDate);
            response = Response.success("Discount end date updated successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String setDiscountPercentage(int id, double percentage) {
        try {
            manager.setDiscountPercentage(id, percentage);
            response = Response.success("Discount percentage updated successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String removeDiscountFromItem(int itemId, int discountId) {
        try {
            String result = manager.getInstance().removeDiscountFromItem(itemId, discountId);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String deleteDiscount(int id) {
        try {
            String result = manager.deleteDiscount(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String updateDiscount(int id, String name, String type, double discountPercentage, Date startDate, Date endDate) {
        try {
            String result = manager.updateDiscount(id, name, type, discountPercentage, startDate, endDate);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String showAllDiscounts() {
        try {
            String result = manager.showAllDiscounts();
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String getDiscountById(int id) {
        try {
            String result = manager.getDiscountById(id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }
}
