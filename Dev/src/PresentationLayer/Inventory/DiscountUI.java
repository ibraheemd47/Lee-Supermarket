package PresentationLayer.Inventory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Scanner;

import ServiceLayer.ServiceFactory;

public class DiscountUI {

    private ServiceFactory serviceFactory;
    private final Scanner sc = new Scanner(System.in);
    
    public DiscountUI(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        
    }
    //public Discount(String name,String type ,double discountPercentage, Date startDate, Date endDate)
    public void showDiscountMenu() {
        int choice;
        do {
            System.out.println("Discount Menu:");
            System.out.println("1. Create Discount");
            System.out.println("2. Update Discount");
            System.out.println("3. Delete Discount");
            System.out.println("4. Show Discount");
            System.out.println("5. Show All Discounts");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createDiscount();
                    break;
                case 2:
                    updateDiscount();
                    break;
                case 3:
                    deleteDiscount();
                    break;
                case 4:
                    showDiscount();
                    break;
                case 5:
                    showDiscounts();
                    break;    
                case 6:
                    System.out.println("Exiting Discount Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
        
    }
    private void showDiscount() {
        System.out.println("Enter discount ID to view details:");
        int id = sc.nextInt();
        sc.nextLine(); // Consume newline
        String discountDetails = serviceFactory.getDiscountById(id); // Assuming this method exists in InventoryManager
        if (discountDetails != null) {
            System.out.println(discountDetails);
        } else {
            System.out.println("Discount not found.");
        }
    }
    private void showDiscounts() {
        // TODO Auto-generated method stub
        String allDiscounts = serviceFactory.showAllDiscounts(); // Assuming this method exists in InventoryManager
        if (allDiscounts != null && !allDiscounts.isEmpty()) {
            System.out.println("All Discounts:");
            System.out.println(allDiscounts);
        } else {
            System.out.println("No discounts available.");
        }
    }
    private void deleteDiscount() {
        // TODO Auto-generated method stub
        System.out.println("Enter discount ID to delete:");
        int id = sc.nextInt();
        sc.nextLine(); // Consume newline
        String result = serviceFactory.deleteDiscount(id); // Assuming this method exists in InventoryManager
        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("Discount not found.");
        }
    }
    private void updateDiscount() {
        // TODO Auto-generated method stub
        System.out.println("Enter discount ID to update:");
        int id = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter new discount name:");
        String name = sc.nextLine();
        System.out.println("Enter new discount type (e.g., Category, Item):");
        String type = sc.nextLine();
        System.out.println("Enter new discount percentage:");
        double discountPercentage = sc.nextDouble();
        sc.nextLine(); // Consume newline
        System.out.println("Enter new start date (yyyy-mm-dd):");
        String startDateStr = sc.nextLine();
        LocalDate sDate = LocalDate.parse(startDateStr);
        Date startDate = java.sql.Date.valueOf(sDate); // Convert LocalDate to Date
        System.out.println("Enter new end date (yyyy-mm-dd):");
        String endDateStr = sc.nextLine();
        LocalDate eDate = LocalDate.parse(endDateStr);
        Date endDate = java.sql.Date.valueOf(eDate); // Convert LocalDate to Date
        String result = serviceFactory.updateDiscount(id, name, type, discountPercentage, startDate, endDate); // Assuming this method exists in InventoryManager
        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("Discount not found.");
        }
    }
    public void createDiscount(){
        System.out.println("Enter discount name:");
        String name = sc.nextLine();
        System.out.println("Enter discount type (e.g., Category, Item):");
        String type = sc.nextLine();
        System.out.println("Enter discount percentage:");
        double discountPercentage = sc.nextDouble();
        sc.nextLine(); // Consume newline
        System.out.println("Enter start date (yyyy-mm-dd):");
        String startDateStr = sc.nextLine();
        LocalDate sDate = LocalDate.parse(startDateStr);
        Instant instant = sDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Date stDate = Date.from(instant); // Convert LocalDate to Date
        System.out.println("Enter end date (yyyy-mm-dd):");
        String endDateStr = sc.nextLine();
        LocalDate eDate = LocalDate.parse(endDateStr);
        Instant instant2 = eDate.atStartOfDay(ZoneOffset.UTC).toInstant();

        Date endDate = Date.from(instant2);
        System.out.println(serviceFactory.createDiscount(name, type, discountPercentage, stDate, endDate));
    }
}
