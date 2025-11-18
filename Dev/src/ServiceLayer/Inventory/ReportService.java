package ServiceLayer.Inventory;

import domainLayer.Inventory.InventoryManager;
import java.util.Date;
import java.util.List;

public class ReportService {

    private final InventoryManager manager;
    private Response response;

    public ReportService(InventoryManager manager) {
        this.manager = manager;
    }

    public String checkDefectiveItems() {
        try {
            manager.checkDefictiveItems();
            response = Response.success("Defective items checked successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String reportDefectiveItems(String name, String description, Date startDate, Date endDate, int itemId, int defectiveQuantity) {
        try {
            manager.reportDefectiveItems(name, description, startDate, endDate, itemId, defectiveQuantity);
            response = Response.success("Defective item reported successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String ShowItemReports() {
        try {
           String result = manager.ShowItemReports();
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String deleteReport(int id,int type) {
        try {
            boolean deleted = manager.deleteReport(id,type);
            response = Response.success("Report deleted successfully."+ deleted);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String getReportById(int id,int type) {
        try {
            manager.getReportById(id,type);
            response = Response.success("Report printed successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String updateReport(int id, int type,String name, String description, Date startDate, Date endDate, int defectiveQuantity) {
        try {
            manager.updateReport(id, type,name, description, startDate, endDate, defectiveQuantity);
            response = Response.success("Report updated successfully.");
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String createCategoriesReport(String name, String description, Date startDate, Date endDate, List<Integer> categoryIds) {
        try {
            String result = manager.createCategoriesReport(name, description, startDate, endDate, categoryIds);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String CreateReport(String name, String description, java.sql.Date startDate, java.sql.Date endDate, int itemId, int defectiveQuantity) {
        try {
            String result = manager.CreateReport(name, description, startDate, endDate, itemId,defectiveQuantity);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }
    }

