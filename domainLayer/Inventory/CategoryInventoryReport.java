package domainLayer.Inventory;

import java.util.Date;
import java.util.List;

public class CategoryInventoryReport extends Report {

    
    private List<Category> Categories;
    

    public CategoryInventoryReport( List<Category> categories, String name, String description, Date startDate, Date endDate) {
        super(name, description, startDate, endDate); // Call the constructor of the superclass
        this.Categories = categories;
    }
    public CategoryInventoryReport( int id,List<Category> categories, String name, String description, Date startDate, Date endDate) {
        super(id,name, description, startDate, endDate); // Call the constructor of the superclass
        this.Categories = categories;
    }

    

    public List<Category> getCategories() {
        return Categories;
    }
    public void setCategories(List<Category> categories) {
        this.Categories = categories;
    }
    public void addCategory(Category category) {
        this.Categories.add(category);
    }
    public void removeCategory(Category category) {
        this.Categories.remove(category);
    }
    public void clearCategories() {
        this.Categories.clear();
    }
    public String DoCategotriesReport() {
       
        String report =  this.generateReport()+"\n"+"Category Report:\n";
        for (Category category : Categories) {
            report += "Category ID: " + category.getId() + ", Name: " + category.getName() + ", Quantity: "+category.getQuantity()+", Value: "+ category.getValue()+ "\n";
            if(category.getSubCategories().isEmpty()){
                report += "\n";
            }
            else{
                report += "Subcategories:\n";
            }
            
            for (Category subCategory : category.getSubCategories()) {
                report += "  Subcategory ID: " + subCategory.getId() + ", Name: " + subCategory.getName() +", Quantity: "+category.getQuantity()+", Value: "+ category.getValue()+ "\n";
            }
        }
        return report;
    }

}