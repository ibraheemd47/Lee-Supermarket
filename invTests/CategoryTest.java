// File: Inventory/BusinessLayer/CategoryTest.java

package invTests;

import static org.junit.jupiter.api.Assertions.*;


import domainLayer.Inventory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class CategoryTest {

    private Category parentCategory;
    private Category subCategory;
    private Item testItem;
    private InventoryManager Manger = new InventoryManager();

    @BeforeEach
    void setUp() throws SQLException {
        List<Item> list = new ArrayList<>();

        list.add(testItem);
        parentCategory = new Category("Electronics",parentCategory ,50, 50);
        subCategory = new Category("Mobile Phones",parentCategory ,10, 20);
        Manger.setParentCategory(2,1);
        parentCategory.addSubCategory(subCategory);
        List<String> SuppliedItems = new ArrayList<>();
        SuppliedItems.add("Iphone 16");
        testItem = new Item(
                "iPhone",
                subCategory,
                new Date(),
                10,
                5, "Ibra","Saris",
                500.0,
                999.99,
                5,
                new Location("1", "1"),
                new Location("2", "2"));
    }

    @Test
    void setParentCategory() {
        Category newParent = new Category("Gadgets", 0, 0);
        subCategory.setParentCategory(newParent);
        assertEquals(newParent, subCategory.getParentCategory());
    }

    @Test
    void removeSubCategory() {
        parentCategory.removeSubCategory(subCategory);
        assertFalse(parentCategory.getSubCategories().contains(subCategory));
    }

    @Test
    void changeSubCategories() {
        List<Category> newSubCategories = new ArrayList<>();
        Category newSub = new Category("Tablets", 0, 0);
        newSubCategories.add(newSub);
        parentCategory.ChangeSubCategories(newSubCategories);
        assertTrue(parentCategory.getSubCategories().contains(newSub));
    }

    @Test
    void addSubCategoryQuantity() {
        int initialQuantity = parentCategory.getQuantity();
        parentCategory.addSubCategoryQuantity(10);
        assertEquals(initialQuantity + 10, parentCategory.getQuantity());
    }

    @Test
    void removeSubCategoryQuantity() {
        parentCategory.addSubCategoryQuantity(10);
        parentCategory.removeSubCategoryQuantity(5);
        assertEquals(5, parentCategory.getQuantity());
    }

    @Test
    void removeSubCategoryValue() {
        parentCategory.addSubCategoryValue(100);
        parentCategory.removeSubCategoryValue(50);
        assertEquals(50, parentCategory.getValue());
    }

    @Test
    void addSubCategory() {
        Category newSubCategory = new Category("Accessories", 0, 0);
        parentCategory.addSubCategory(newSubCategory);
        assertTrue(parentCategory.getSubCategories().contains(newSubCategory));
    }

    @Test
    void addItemToCategory() {
        subCategory.setItems(new ArrayList<>());
        subCategory.addItemToCategory(testItem);
        assertTrue(subCategory.getItems().contains(testItem));
        assertEquals(1, subCategory.getItems().size());
    }

    @Test
    void removeItemFromCategory() {
        subCategory.setItems(new ArrayList<>());
        subCategory.addItemToCategory(testItem);
        subCategory.removeItemFromCategory(testItem);
        assertFalse(subCategory.getItems().contains(testItem));
    }

    @Test
    void updateItemQuantity() {
        subCategory.setItems(new ArrayList<>());
        subCategory.addItemToCategory(testItem);
        int newQuantity = 15;
        subCategory.updateItemQuantity(testItem, newQuantity);
        assertEquals(newQuantity, testItem.getQuantity());
    }

    @Test
    void applyDiscountToCategory() {
        subCategory.setItems(new ArrayList<>());
        subCategory.addItemToCategory(testItem);
        Discount discount = new Discount("15%","Category",0.15, new Date(), new Date(),Manger);
        subCategory.applyDiscountToCategory(discount);
        assertTrue(testItem.getSellingPrice() < 999.99);
    }

    @Test
    void removeDiscountFromCategory() {

        subCategory.setItems(new ArrayList<>());
        subCategory.addItemToCategory(testItem);
        Discount discount = new Discount("15%","Category",0.15, new Date(), new Date(),Manger);
        subCategory.applyDiscountToCategory(discount);
        subCategory.removeDiscountFromCategory(discount.getId());
        assertFalse(testItem.getActiveDiscounts().containsKey(discount.getId()));
    }

    @Test
    void removeDiscountsFromCategory() {

        subCategory.setItems(new ArrayList<>());
        subCategory.addItemToCategory(testItem);
        Discount discount = new Discount("15%","Category",0.15, new Date(), new Date(),Manger);
        subCategory.applyDiscountToCategory(discount);
        subCategory.removeDiscountsFromCategory();
        assertTrue(testItem.getActiveDiscounts().isEmpty());
    }
}