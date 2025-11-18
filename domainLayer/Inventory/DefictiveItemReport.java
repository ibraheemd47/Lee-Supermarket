package domainLayer.Inventory;

import java.util.Date;

public class DefictiveItemReport extends Report {
    //i dont think that we need end date and start date maybe experation date and the report date yes, 
    private int DefectiveQuantity;
    private Item item;

    
    public DefictiveItemReport(String name, String description, Date startDate, Date endDate,Item item,int defectiveQuantity) {
        super(name, description, startDate, endDate); // Call the constructor of the superclass
        this.item = item;
        this.DefectiveQuantity = defectiveQuantity;
        
    }
    public DefictiveItemReport(int id,String name, String description, Date startDate, Date endDate,Item item,int defectiveQuantity) {
        super(id,name, description, startDate, endDate); // Call the constructor of the superclass
        this.item = item;
        this.DefectiveQuantity = defectiveQuantity;

    }

    public Item getItem() {
        return item;
    }
    public String generateReport() {
        return "Report '" + getName() + "' (ID=" + getId() + "), covering " 
        + getStartDate() + " to " + getEndDate() + ". "
        + getDescription() + ". "
        + "Item details: " + item.getName() 
        + " (ID=" + item.getId() + "), manufactured by " + item.getManufacturer() 
        + ", in category " + item.getCategory().getName() 
        + ". Current quantity: " + item.getQuantity() 
        + ", defective quantity: " + DefectiveQuantity + ".";
    }
@Override
    public void setDefectiveQuantity(int defectiveQuantity2) {
        this.DefectiveQuantity = defectiveQuantity2;
    }
}