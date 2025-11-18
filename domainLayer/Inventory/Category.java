package domainLayer.Inventory;

import DataAccessLayer.Inventory.CategoryDao;
import DataAccessLayer.Inventory.JdbcCategoryDAO;
import ObjectDTO.Inventory.CategoryDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Category {
    private CategoryDao dao = new JdbcCategoryDAO();
    static int idCounter = 0; // Static counter to generate unique IDs
    public int generateId() {

            try {
                Optional<Integer> maxIdOpt = dao.findMaxId();
               // System.out.println("maxIdOpt category: " + maxIdOpt);
                this.idCounter = maxIdOpt.orElse(0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int returnedId = idCounter+1;
            return returnedId;

    }
    public int id;
    private String name;
    private Category ParentCategory;
    private int Quantity;
    private int Value;
    private List<Item> items; // List of items in the category
    public InventoryManager Manager;
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public int getValue() {
        return Value;
    }
    public Category getParentCategory() {
        return ParentCategory;
    }

    //add list for items to be able to apply discount for all the category 

    private List<Category> subCategories;
    public Category( String name, int quantity,int value) {
        
        this.id = generateId(); // Generate a unique ID for each category
        this.name = name;
        this.ParentCategory = this;
        this.subCategories = new ArrayList<>();
        this.items = new ArrayList<>();
        this.Quantity = quantity;
        this.Value = value;

        this.items = items; // Initialize the list of items in the category
        //Map<Integer, Category> ListCategory = Manager.getCategories();
        //ListCategory.put(this.id,this);
    }
    public Category( int id,String name, int quantity,int value) {

        this.id = id; // Generate a unique ID for each category
        this.name = name;
        this.ParentCategory = this;
        this.subCategories = new ArrayList<>();
        this.items = new ArrayList<>();
        this.Quantity = quantity;
        this.Value = value;

        this.items = items; // Initialize the list of items in the category
        //Map<Integer, Category> ListCategory = Manager.getCategories();
        //ListCategory.put(this.id,this);
    }
    public Category(String name ,Category parentCategory ,int quantity,int value){
        this.id = generateId();
        this.name=name;
        this.ParentCategory = parentCategory;
        this.Quantity = quantity;
        this.Value = value;
        this.subCategories = new ArrayList<Category>();
        this.items = new ArrayList<Item>();
    }

    public Category(int id,String name ,Category parentCategory ,int quantity,int value){
        this.id = id;
        this.name=name;
        this.ParentCategory = parentCategory;
        this.Quantity = quantity;
        this.Value = value;
        this.subCategories = new ArrayList<Category>();
        this.items = new ArrayList<Item>();
    }
    public Category(InventoryManager Manager){
        this.Manager = Manager;
    }
    public void setParentCategory(Category parentCategory) {
        if (this.ParentCategory != null) {
            this.ParentCategory.removeSubCategory(this);
        }
        this.ParentCategory = parentCategory;
        if (parentCategory != null) {
            parentCategory.addSubCategory(this);
        }
    }
    public void removeSubCategory(Category subCategory) {
        if (subCategory != null) {
            subCategories.remove(subCategory);
        }
    }
    public void ChangeSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }
    public List<Category> getSubCategories() {
        return subCategories;
    }
    public String getMainCategory() {
        if(this.ParentCategory == this){
            return this.name;
        }
        return this.ParentCategory.getMainCategory();
    }
    public String getCategoryPath(){
        String path = "";
        if(this.ParentCategory == this){
            path += this.name +"\n";
            if(!this.items.isEmpty()) {

                for (Item item : items) {
                    path += " -" + item.getName() + "\n";
                }
            }

        }else{
            path += this.ParentCategory.getCategoryPath() + ">" + this.name ;
            if(!this.items.isEmpty()) {
                path += "\n";
                for (Item item : items) {
                    path += item.getName() + "\n";
                }
            }
        }
        return path;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(int value) {
        this.Value = value;
    }
    //functoin that adds the quantity of the subcategory to the parent category in a recursive way
    public void addSubCategoryQuantity(int quantity) {
        this.Quantity += quantity;
        int count = 0;
        //System.out.println(ParentCategory);
        if (ParentCategory != this) {
            ParentCategory.addSubCategoryQuantity(quantity);
        }
//        System.out.println(count);
    }

    public void removeSubCategoryQuantity(int quantity) {
        this.Quantity -= quantity;
        if (ParentCategory != null) {
            ParentCategory.removeSubCategoryQuantity(quantity);
        }
    }
    public void addSubCategoryValue(int value) {
        this.Value += value;
        if (ParentCategory != this) {
            ParentCategory.addSubCategoryValue(value);
        }
    }
    public void removeSubCategoryValue(int value) {
        this.Value -= value;
        if (ParentCategory != null) {
            ParentCategory.removeSubCategoryValue(value);
        }
    }
    public int getQuantity() {
        return Quantity;
    }
    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    public void addSubCategory(Category subCategory) {
        if (subCategory != null && !subCategories.contains(subCategory)) {
            subCategories.add(subCategory);
            this.addSubCategoryQuantity(subCategory.getQuantity());
            this.addSubCategoryValue(subCategory.getValue());            
        }
    }
    public void addItemToCategory(Item item) {
        if (item != null && !items.contains(item)) {
            items.add(item);
            this.Quantity += item.getQuantity(); // Update the quantity of the category
            this.Value += item.getSellingPrice(); // Update the value of the category
        }

        ParentCategory.addSubCategoryQuantity(item.getQuantity()); // Update the quantity of the parent category
    }
    public void removeItemFromCategory(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            this.Quantity -= item.getQuantity(); // Update the quantity of the category
            this.Value -= item.getSellingPrice(); // Update the value of the category
        }
        ParentCategory.removeSubCategoryQuantity(item.getQuantity()); // Update the quantity of the parent category
    }
    public void updateItemQuantity(Item item, int newQuantity) {
        if (items.contains(item)) {
            item.setQuantity(newQuantity);
            this.Quantity += (newQuantity); // Update the quantity of the category
        }
    }
    public void applyDiscountToCategory(Discount discount) {
        for (Item item : items) {
            item.applyDiscount(discount); // Apply the discount to each item in the category
        }
        for (Category subCategory : subCategories) {
            subCategory.applyDiscountToCategory(discount); // Apply the discount to each subcategory
        }
    }
    public void removeDiscountFromCategory(int discountId) {
        for (Item item : items) {
            item.removeDiscount(discountId); // Remove the discount from each item in the category
        }
        for (Category subCategory : subCategories) {
            subCategory.removeDiscountFromCategory(discountId); // Remove the discount from each subcategory
        }
    }
    public void removeDiscountsFromCategory() {
        for (Item item : items) {
            item.removeDiscounts(); // Remove the discount from each item in the category
        }
        for (Category subCategory : subCategories) {
            subCategory.removeDiscountsFromCategory(); // Remove the discount from each subcategory
        }
    }

    public String toString() {
        return "Category ID: " + id + ", Name: " + name + ", Quantity: " + Quantity + ", Value: " + Value;
    }
    public Category(CategoryDTO dto, InventoryManager manager) {
        this.id = dto.id(); // לא מייצר ID חדש - מקבל מה-DTO
        this.name = dto.name();
        this.Quantity = dto.quantity();
        this.Value = dto.value();
        this.Manager = manager;
        this.subCategories = new ArrayList<>();
        this.items = new ArrayList<>();

        if (dto.parentId() != null && manager != null) {
            this.ParentCategory = manager.getCategories().get(dto.parentId());
        } else {
            this.ParentCategory = this; // במידה ואין אב - נחשב לקטגוריה ראשית
        }
    }

}