package PresentationLayer.Inventory;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ServiceLayer.ServiceFactory;

public class ReportUI {
    private ServiceFactory serviceFactory;
    private final Scanner sc = new Scanner(System.in);
    public ReportUI(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }
    public void showReportMenu() {
        int choice;
        do {
            System.out.println("Report Menu:");
            System.out.println("1. Create Item Report");
            System.out.println("2. Update Report");
            System.out.println("3. Delete Report");
            System.out.println("4. Report Category");
            System.out.println("5. Show Report");
            System.out.println("6. Show All Reports");
            System.out.println("7. Check Items");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createReport();
                    break;
                case 2:
                    updateReport();
                    break;
                case 3:
                    deleteReport();
                    break;
                case 4:
                    reportCategory(); // Assuming this method exists in InventoryManager
                    break;

                case 5:
                    showReport();
                    break;
                case 6:
                    showReports();
                    break;   
                case 7:
                    System.out.println("Checking items for alerts...");
                    // Call the method to check items from InventoryManager
                    serviceFactory.checkItemsForAlert(); // Assuming this method exists in InventoryManager
                    break;     
                case 8:
                    System.out.println("Exiting Report Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
        
    }
    private void showReports() {
        // TODO Auto-generated method stub
        System.out.println("Showing all reports...");
        String Output= serviceFactory.ShowItemReports();
        System.out.println(Output);
    }
    private void showReport() {
        // TODO Auto-generated method stub
        System.out.println("Enter report ID to view details:");
        int id = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter report type (category = 1, item = 2):");
        int typeChoice = sc.nextInt();
        sc.nextLine(); // Consume newline
        String Output= serviceFactory.getReportById(id,typeChoice); // Assuming this method exists in InventoryManager
        System.out.println(Output);
    }
    private void deleteReport() {
        // TODO Auto-generated method stub
        System.out.println("Enter report ID to delete:");
        int id = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter report type (category = 1, item = 2):");
        int typeChoice = sc.nextInt();
        sc.nextLine(); // Consume newline
        boolean deleted = Boolean.parseBoolean(serviceFactory.deleteReport(id,typeChoice)); // Assuming this method exists in InventoryManager
    }
    private void updateReport() {
        // TODO Auto-generated method stub
        System.out.println("Enter report ID to update:");
        int id = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter report type (category = 1, item = 2):");
        int typeChoice = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter new report name:");
        String name = sc.nextLine();
        System.out.println("Enter new report description:");
        String description = sc.nextLine();
        System.out.println("Enter new report start date (YYYY-MM-DD):");
        String sDate = sc.nextLine();
        LocalDate stDate = LocalDate.parse(sDate); // Assuming you want to parse the date
        Date startDate = Date.valueOf(stDate); // Convert LocalDate to java.sql.Date
        System.out.println("Enter new report end date (YYYY-MM-DD):");
        String eDate = sc.nextLine();

        LocalDate enDate = LocalDate.parse(eDate); // Assuming you want to parse the date
        Date endDate = Date.valueOf(enDate); // Convert LocalDate to java.sql.Date
        System.out.println("Enter new Defective quantity:");
        int defectiveQuantity = sc.nextInt();
        String Output=  serviceFactory.updateReport(id,typeChoice ,name, description, startDate, endDate, defectiveQuantity); // Assuming this method exists in InventoryManager

        System.out.println(Output);
    }
    // public void reportDefectiveItems(int Id ,String name, String description, Date startDate, Date endDate, Item item, int defectiveQuantity)
    private void createReport() {
        
        System.out.println("Enter report name:");
        String name = sc.nextLine();
        System.out.println("Enter report description:");
        String description = sc.nextLine();
//        System.out.println("Enter report start date (YYYY-MM-DD):");
//        String sDate = sc.nextLine();
//        LocalDate stDate = LocalDate.parse(sDate); // Assuming you want to parse the date
        Date startDate = new Date(System.currentTimeMillis()); // Convert LocalDate to java.sql.Date
//        System.out.println("Enter report end date (YYYY-MM-DD):");
//        String eDate = sc.nextLine();
//        LocalDate enDate = LocalDate.parse(eDate); // Assuming you want to parse the date
        Date endDate = new Date(System.currentTimeMillis()); // Convert LocalDate to java.sql.Date
        System.out.println("Enter item ID:");
        int itemId = sc.nextInt();
        
        System.out.println("Enter defective quantity:");
        int defectiveQuantity = sc.nextInt();

        String Output=  serviceFactory.CreateReport(name, description, startDate, endDate, itemId, defectiveQuantity); // Assuming this method exists in InventoryManager
        System.out.println(Output);
    }
    private void reportCategory() { // TODO : Put in reportUi and also put and the isbelowmin in every possible function and test the alert and report classes
        System.out.println("Enter report name:");
        String name = sc.nextLine(); // Read the report name from user input
        System.out.println("Enter report description:");
        String description = sc.nextLine(); // Read the report description from user input
        System.out.println("Enter report start date (YYYY-MM-DD):");
        String sDate = sc.nextLine(); // Read the start date from user input
        LocalDate stDate = LocalDate.parse(sDate); // Parse the start date string into a LocalDate object
        java.util.Date startDate = java.sql.Date.valueOf(stDate); // Convert LocalDate to java.sql.Date

        System.out.println("Enter report end date (YYYY-MM-DD):");
        String eDate = sc.nextLine(); // Read the end date from user input
        LocalDate enDate = LocalDate.parse(eDate); // Parse the end date string into a LocalDate object
        java.util.Date endDate = java.sql.Date.valueOf(enDate); // Convert LocalDate to java.sql.Date
        System.out.println("Enter categories ID to report (comma seperated):");
        String categoriesInput = sc.nextLine(); // Read the categories IDs from user input
        String[] categoriesArray = categoriesInput.split(","); // Split the input string into an array of category IDs
        List<Integer> categoriesIds = new ArrayList<>(); // Initialize an empty list to store the category IDs
        for (String categoryId : categoriesArray) {
            categoriesIds.add(Integer.parseInt(categoryId.trim())); // Parse each category ID and add it to the list
        }
        String Output= serviceFactory.createCategoriesReport(name, description, startDate, endDate, categoriesIds); // Call the method to report category in InventoryManager
        System.out.println(Output);
    }
    /*
     * public void checkDefectiveItems(){
        for (Item item : items.values()) {
            if (item.isExpired()) {
                DefictiveItemReport report = new DefictiveItemReport("Defective Item Report", "Item is expired", new Date(), new Date(), item, item.getQuantity()); // Create a new report for the defective item
                itemReports.put(report.getId(),report); // Add the report to the list of defective item reports
                System.out.println(report.generateReport()); // Print the report details
            }
        }
    }
    // add constructor without id and with id for item instead of item
    public void reportDefectiveItems(int Id ,String name, String description, Date startDate, Date endDate, Item item, int defectiveQuantity) {
        DefictiveItemReport report = new DefictiveItemReport(name, description, startDate, endDate, item, defectiveQuantity); // Create a new report for the defective item
        itemReports.put(report.getId(),report);
        System.out.println(report.generateReport()); // Print the report details
    }
    public void ShowItemReports(){
        for (DefictiveItemReport report : itemReports.values()) {
            System.out.println(report.generateReport()); // Print the report details
        }
    }
     */
    
}
