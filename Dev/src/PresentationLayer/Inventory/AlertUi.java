package PresentationLayer.Inventory;

import java.util.Scanner;

import ServiceLayer.ServiceFactory;

public class AlertUi {
    private ServiceFactory serviceFactory;
    private final Scanner sc = new Scanner(System.in);
    public AlertUi(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }
    public void showAlertMenu() {
        int choice;
        do {
            System.out.println("Alerts Menu:");
            System.out.println("1. Show Alerts");
            System.out.println("2. Exit\n");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Showing alerts...");
                    // Call the method to show alerts from InventoryManager
                    System.out.println(serviceFactory.showAlerts()); // Assuming this method exists in InventoryManager
                    break;
                case 2:
                    System.out.println("Exiting Alerts Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 2);
        
        
    }

    
}
