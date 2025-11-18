package domainLayer.Inventory;

import DataAccessLayer.Inventory.AlertDao;
import DataAccessLayer.Inventory.JdbcAlertDAO;

import java.sql.SQLException;
import java.util.Date;
import java.time.LocalDate;
import java.util.Optional;


public class Alert {

    private int id;
    private String message;
    private String type;
    private Date dateCreated;
    private Item item;
    public InventoryManager Manager;
    static int alertIdCount ; // Static variable to keep track of the number of alerts
    AlertDao dao = new JdbcAlertDAO();
    public int generateId() {

        try {
            Optional<Integer> maxIdOpt = dao.findMaxId();
          //  System.out.println("maxIdOpt category: " + maxIdOpt);
            this.alertIdCount = maxIdOpt.orElse(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int returnedId = alertIdCount+1;
        return returnedId;

    }
    public void updateAlert(String message, String type, Date dateCreated,Item item) {
        this.item = item;
        this.id = id;
        this.message = message;
        this.type = type;
        this.dateCreated = dateCreated;


    }
    public Alert(String message, String type, Date dateCreated,Item item) {
        this.item = item;
        this.message = message;
        this.type = type;
        this.dateCreated = dateCreated;
        this.id = generateId(); // Increment the alert count and assign it as the ID
//        Manager.AlertsRecord.add(this);
    }
    public Alert(int id,String message, String type, Date dateCreated,Item item) {
        this.item = item;
        this.message = message;
        this.type = type;
        this.dateCreated = dateCreated;
        this.id = id;
    }
    public Alert(InventoryManager Manager){
        this.Manager = Manager;
    }
    //public Alert() {
      //  this.id = alertCount++; // Increment the alert count and assign it as the ID
    //}
    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
    public Item getItem() {
        return item;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    
    public String generateAlert() {
        // Logic to generate the alert based on the provided details
        return "Alert generated: \n"+"Alert ID: "+id+"\n"+"Item Id: "+item.getId()+" Message: " + message + " Type: " + type + " Date Created: " + LocalDate.now()+ "\n";
    }
}