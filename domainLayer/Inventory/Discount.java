package domainLayer.Inventory;
import DataAccessLayer.Inventory.DiscountDao;
import DataAccessLayer.Inventory.JdbcDiscountDAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class Discount {
    private static int discountCount ; // Static variable to keep track of the number of discounts
    DiscountDao dao = new JdbcDiscountDAO();
    public int generateId() {

        try {
            Optional<Integer> maxIdOpt = dao.findMaxId();
         //   System.out.println("maxIdOpt category: " + maxIdOpt);
            this.discountCount = maxIdOpt.orElse(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int returnedId = discountCount+1;
        return returnedId;

    }
    private int id;
    private String name;
    private double discountPercentage;
    private Date startDate;
    private Date endDate;
    private double oldPrice;
    private String type; // Type of discount (e.g., Category, Item, etc.)
    public InventoryManager Manager ;

    public Discount(String name,String type ,double discountPercentage, Date startDate, Date endDate,InventoryManager Manager) {
        this.id = generateId();
        this.name = name;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type; // Initialize the type of discount
       Map<Integer,Discount> Discounts =  Manager.getDiscounts() ;
       Discounts.put(this.id,this);
    }
    public Discount(int id,String name,String type ,double discountPercentage, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type; // Initialize the type of discount
//        Map<Integer,Discount> Discounts =  Manager.getDiscounts() ;
//        Discounts.put(this.id,this);
    }
    public String getType() {
        return type;
    }
    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }
    public double getOldPrice() {
        return oldPrice;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date newStartDate) {
        this.startDate = newStartDate;
    }
    public void setName(String newName) {
        this.name = newName;
    }
    public void setType(String newType) {
        this.type = newType;
    }

    public Date getEndDate() {
        return endDate;
    }
    public void setEndtDate(Date newEndDate) {
        this.endDate = newEndDate;
    }
    public boolean isExpired() {
        Date currentDate = new Date(); // Get the current date
        return currentDate.getTime()>endDate.getTime(); // Check if the current date is after the end date of the discount
    }
    public boolean isActive() {
        Date currentDate = new Date(); // Get the current date
        return currentDate.after(startDate) && currentDate.before(endDate); // Check if the current date is within the discount period
    }
    public String toString() {
        return "Discount ID: " + id + ", Name: " + name +", Type: "+type+ ", Discount Percentage: " + discountPercentage + "%, Start Date: " + startDate + ", End Date: " + endDate ;
    }
    
}