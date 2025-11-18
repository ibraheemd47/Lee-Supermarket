package domainLayer.Inventory;

import DataAccessLayer.Inventory.JdbcReportDAO;
import DataAccessLayer.Inventory.ReportDao;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

public class Report {
    static int reportIdCount=0; // Static counter to generate unique IDs
    ReportDao dao = new JdbcReportDAO();
    public int generateId() {

        try {
            Optional<Integer> maxIdOpt = dao.findMaxId();
         //   System.out.println("maxIdOpt category: " + maxIdOpt);
            this.reportIdCount = maxIdOpt.orElse(0);
          //  System.out.println("Report ID Count: " + reportIdCount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int returnedId = reportIdCount+1;
        return returnedId;

    }

    private int id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;

    public Report( String name, String description, Date startDate, Date endDate) {
        this.id = generateId(); // Increment the report count and assign it as the ID
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Report(int id ,String name, String description, Date startDate, Date endDate) {
        this.id = id; // Increment the report count and assign it as the ID
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    public void setName(String newName) {
        this.name = newName;
    }
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
    public void setStartDate(Date newStartDate) {
        this.startDate = newStartDate;
    }
    public void setEndDate(Date newEndDate) {
        this.endDate = newEndDate;
    }
    
    public String generateReport() {
        // Logic to generate the report based on the provided details
        return "Report generated: "+"ID: "+id+" Name: " + name + " from " + startDate + " to " + endDate;
    }
    public void setDefectiveQuantity(int defectiveQuantity) {

    }
}