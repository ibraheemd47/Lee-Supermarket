package PresentationLayer.Inventory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import ServiceLayer.ServiceFactory;

public class CategoryUI {
    private ServiceFactory serviceFactory;
    private final Scanner sc = new Scanner(System.in);
    public CategoryUI(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }
    public void showCategoryMenu() {
        // TODO Auto-generated method stub
        int choice;
        do {
            System.out.println("Category Menu:");
            System.out.println("1. Create Category");
            System.out.println("2. Update Category");
            System.out.println("3. Delete Category");
            System.out.println("4. Show Category");
            System.out.println("5. Show All Categories");
            System.out.println("6. Set Parent Category");
            System.out.println("7. Remove Sub Category");
            System.out.println("8. Add Sub Category");
            System.out.println("9. Remove Item From Category");
            System.out.println("10. Add Item To Category");
            System.out.println("11. Get Category Path");
            System.out.println("12. Get Main Category");
            System.out.println("13. Change Sub Categories");
            System.out.println("14. Apply Discount To Category");
            System.out.println("15. Remove Discount From Category");
            System.out.println("16. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createCategory();
                    break;
                case 2:
                    updateCategory();
                    break;
                case 3:
                    deleteCategory();
                    break;
                case 4:
                    showCategory();
                    break;
                case 5:
                    showCategories();
                    break;
                case 6:
                    setParentCategory();
                    break;
                case 7:
                    removeSubCategory();
                    break;
                case 8:
                    addSubCategory();
                    break;
                case 9:
                    removeItemFromCategory();
                    break;
                case 10:
                    addItemToCategory();
                    break;
                case 11:
                    getCategoryPath();
                    break;
                case 12:
                    getMainCategory();
                    break;
                case 13:
                    changeSubCategories();
                    break;
                case 14:
                    applyDiscountToCategory();
                    break;
                case 15:
                    removeDiscountFromCategory();
                    break;
                case 16:
                    System.out.println("Exiting Category Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 16);
    }
    private void removeDiscountFromCategory() {
        System.out.println("Enter category ID to remove discount from:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter discount ID to remove:");
        int discountId = sc.nextInt(); // Read the discount ID from user input
        sc.nextLine(); // Consume newline
        String Output=serviceFactory.removeDiscountFromCategory(id, discountId); // Call the method to remove discount in InventoryManager
        System.out.println(Output);
    }
    private void applyDiscountToCategory() {
        System.out.println("Enter category ID to apply discount to (Make sure you have created the discount before and you have the Id):");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter discount ID to apply:");
        int discountId = sc.nextInt(); // Read the discount ID from user input
        sc.nextLine(); // Consume newline
        String Output= serviceFactory.applyDiscountToCategory(id, discountId); // Call the method to apply discount in InventoryManager
        System.out.println(Output);
    }
    private void changeSubCategories() {
        System.out.println("Enter main category ID to change subcategories:");
        int mainCatId = sc.nextInt(); // Read the main category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter subcategory IDs (comma-separated):");
        String subCategoriesInput = sc.nextLine(); // Read the subcategory IDs from user input
        String[] subCategoriesArray = subCategoriesInput.split(","); // Split the input string into an array of subcategory IDs
        List<Integer> subCategoriesIds = new ArrayList<>(); // Initialize an empty list to store the subcategory IDs
        for (String subCategoryId : subCategoriesArray) {
            subCategoriesIds.add(Integer.parseInt(subCategoryId.trim())); // Parse each subcategory ID and add it to the list
        }
        String Output= serviceFactory.changeSubCategories(mainCatId, subCategoriesIds); // Call the method to change subcategories in InventoryManager
        System.out.println(Output);
    }
    private void getMainCategory() {
        System.out.println("Enter category ID to get main category:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        String mainCategory = serviceFactory.getMainCategory(id); // Call the method to get main category in InventoryManager
        if (mainCategory != "Category not found") {
            System.out.println("Main Category: " + mainCategory); // Print the main category
        } else {
            System.out.println("Category not found."); // Print error message if category is not found
        }
    }
    private void getCategoryPath() {
        System.out.println("Enter category ID to get path:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        String path = serviceFactory.getCategoryPath(id); // Call the method to get category path in InventoryManager
        if (path != "Category not found") {
            System.out.println("Category Path: " + path); // Print the category path
        } else {
            System.out.println("Category not found."); // Print error message if category is not found
        }
    }
    private void addItemToCategory() {
        System.out.println("Enter category ID to add item to:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter item ID to add:");
        int itemId = sc.nextInt(); // Read the item ID from user input
        sc.nextLine(); // Consume newline
        String Output= serviceFactory.addItemToCategory(id, itemId); // Call the method to add item to category in InventoryManager
        System.out.println(Output);
    }
    private void removeItemFromCategory() {
        System.out.println("Enter category ID to remove item from:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter item ID to remove:");
        int itemId = sc.nextInt(); // Read the item ID from user input
        sc.nextLine(); // Consume newline
        String Output=serviceFactory.removeItemFromCategory(id, itemId); // Call the method to remove item from category in InventoryManager
        System.out.println(Output);
    }
    private void addSubCategory() {
        System.out.println("Enter category ID to add subcategory to:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter subcategory ID to add:");
        int subId = sc.nextInt(); // Read the subcategory ID from user input
        sc.nextLine(); // Consume newline
        String Output= serviceFactory.addSubCategory(id, subId); // Call the method to add subcategory in InventoryManager
        System.out.println(Output);
    }
    private void removeSubCategory() {
        System.out.println("Enter category ID to remove subcategory from:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter subcategory ID to remove:");
        int subId = sc.nextInt(); // Read the subcategory ID from user input
        sc.nextLine(); // Consume newline
        String Output= serviceFactory.removeSubCategory(id, subId); // Call the method to remove subcategory in InventoryManager
        System.out.println(Output);
    }
    private void setParentCategory() {
        System.out.println("Enter category ID to set parent:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter parent category ID (or 0 if no parent):");
        int parentId = sc.nextInt(); // Read the parent category ID from user input
        sc.nextLine(); // Consume newline
        String Output= serviceFactory.setParentCategory(id, parentId); // Call the method to set parent category in InventoryManager
        System.out.println(Output);
    }

    private void showCategories() {
        System.out.println("Categories: ");
        String categories = serviceFactory.showCategories(); // Call the method to show categories in InventoryManager
        System.out.println(categories); // Print the categories
    }
    private void showCategory() {
        System.out.println("Enter category ID to show:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        String category = serviceFactory.showCategory(id); // Call the method to show category in InventoryManager
        if (category != "Category not found") {
            System.out.println("Category information: " + category); // Print the category details
        } else {
            System.out.println("Category not found."); // Print error message if category is not found
        }
    }
    private void deleteCategory() {
        System.out.println("Enter category ID to delete:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        String result = serviceFactory.removeCategory(id); // Call the method to remove category in InventoryManager
        if (result != "Category not found") {
            System.out.println(result); // Print success message
        } else {
            System.out.println("Category not found."); // Print error message if category is not found
        }
    }
    private void updateCategory() {
        System.out.println("Enter category ID to update:");
        int id = sc.nextInt(); // Read the category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter new category name:");
        String name = sc.nextLine(); // Read the new category name from user input
        System.out.println("Enter new parent category ID (or 0 if no parent):");
        int parentId = sc.nextInt(); // Read the new parent category ID from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter new quantity:");
        int quantity = sc.nextInt(); // Read the new quantity from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter new value:");
        int value = sc.nextInt(); // Read the new value from user input
        sc.nextLine(); // Consume newline
        String Output= serviceFactory.updateCategory(id, name, parentId, quantity, value); // Call the method to update category in InventoryManager
        System.out.println(Output);
    }
    private void createCategory() {
        System.out.println("Enter category name:");
        String name = sc.nextLine(); // Read the category name from user input
        System.out.println("Enter parent category ID (or 0 if no parent):");
        int parentId = sc.nextInt(); // Read the parent category ID from user input
        sc.nextLine(); // Consume newline
        //int parentCategoryId = (parentId == 0) ? 0 : parentId; // Set parent category ID to 0 if no parent
        System.out.println("Enter quantity (0 if new category):");
        int quantity = sc.nextInt(); // Read the quantity from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter value (0 if new category):");
        int value = sc.nextInt(); // Read the value from user input
        sc.nextLine(); // Consume newline
        System.out.println("Enter items Id (comma-separated, Enter if there is no items yet):");
        String itemsInput = sc.nextLine(); // Read the items ID from user input
        String[] itemsArray = itemsInput.split(","); // Split the input string into an array of item IDs
        List<Integer> items = new ArrayList<>(); // Initialize an empty list to store the items  
        for (String itemId : itemsArray) {
            items.add(Integer.parseInt(itemId.trim())); // Parse each item ID and add it to the list
        }
        String Output=serviceFactory.addNewCategory(name, parentId, quantity, value, items); // Call the method to add a new category in InventoryManager
        System.out.println(Output);

    }
    
}
