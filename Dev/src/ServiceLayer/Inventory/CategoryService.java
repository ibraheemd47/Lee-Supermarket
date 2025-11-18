package ServiceLayer.Inventory;

import domainLayer.Inventory.Category;
import domainLayer.Inventory.InventoryManager;

import java.util.List;

public class CategoryService{
    public InventoryManager Manager;
    private Response response;

    public CategoryService(InventoryManager manager) {
        this.Manager=manager;
    }
    public String showCategories() {
        try {
            String result = InventoryManager.getInstance().showCategories();
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }
    public String addNewCategory(String name , int parentId, int quantity, int value, List<Integer> itemsId) {
        try {
        Category result = InventoryManager.getInstance().addNewCategory(name, parentId, quantity, value, itemsId);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString();
    }
    public String removeCategory(int Id) {
        try {
            String result = InventoryManager.getInstance().removeCategory(Id);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }
    public String showCategory(int Id) {
        try {
        String result = InventoryManager.getInstance().showCategory(Id);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString();  }
    public String changeSubCategories(int mainCatId,List<Integer> subCategoriesIds) {
        try {
        String result = InventoryManager.getInstance().changeSubCategories(mainCatId,subCategoriesIds);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString();  }
    public String setParentCategory(int Id,int parentId) {
        try {
        String result = InventoryManager.getInstance().setParentCategory(Id ,parentId);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString(); }
    public String addSubCategory(int Id , int subId) {
        try {
              InventoryManager.getInstance().addSubCategory(Id,subId);
            response = Response.success("Sub Category was Added Successfully");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }
    public String removeSubCategory(int Id,int subId) {
        try {
        InventoryManager.getInstance().removeSubCategory(Id,subId);
        response = Response.success("Sub Category was Removed Successfully ");
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString(); }
    public String addItemToCategory(int Id ,int ItemId) {
        try {
        String result = InventoryManager.getInstance().addItemToCategory(Id,ItemId);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString(); }
    public String removeItemFromCategory(int Id,int ItemId) {
        try {
        String result = InventoryManager.getInstance().removeItemFromCategory(Id,ItemId);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString();
    }
    public String getCategoryPath(int Id) {
        try {
        String result = InventoryManager.getInstance().getCategoryPath(Id);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString();  }
    public String getMainCategory(int Id) {
        try {
        String result = InventoryManager.getInstance().getMainCategory(Id);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString();
    }
    public String updateCategory(int id, String name, int parentId, int quantity, int value) {
        try {
         InventoryManager.getInstance().updateCategory( id,  name,  parentId,  quantity,  value);// this will need checking if its updated
        response = Response.success("The Category was Updated Successfully");
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString();
    }
    public String applyDiscountToCategory(int Id,int discountId) {
        try {
        String result = InventoryManager.getInstance().applyDiscountToCategory(Id,discountId);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString(); }
    public String removeDiscountFromCategory(int Id,int discountId) {
        try {
        String result = InventoryManager.getInstance().removeDiscountFromCategory(Id,discountId);
        response = Response.success(result);
    } catch (Exception e) {
        response = Response.failure(e.getMessage());
    }
        return response.toString(); }
}