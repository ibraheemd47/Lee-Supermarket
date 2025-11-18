import PresentationLayer.Inventory.ManagerUI;
import ServiceLayer.ServiceFactory;
import PresentationLayer.Supplier.Controllers.ServiceController;
import PresentationLayer.Supplier.Presentation;
import jdk.jshell.spi.ExecutionControl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ExecutionControl.NotImplementedException {
        Scanner scanner = new Scanner(System.in);
        boolean isFirstTime = true;

        while (true) {
            System.out.println("=========================================");

            System.out.println("\n Welcome to Super Lee Supply Management System ");
            System.out.println("=======================================");
            System.out.println("would you want to enter the inventory management system? or would you like to enter the supplier management system?\n(1 - inventory management system, 2 - supplier management system )");
            int choice;
            while (!scanner.hasNextInt()) {

                System.out.println("enter a number");
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            while (choice != 1 && choice != 2&& choice!=0) {
                System.out.println("Invalid choice! Try again.");
                if(scanner.hasNextInt())
                {
                    choice=scanner.nextInt();
                    scanner.nextLine();

                }
                else
                    choice=-1;
            }
            switch (choice) {

                case 1: {

                    ServiceFactory serviceFactory = new ServiceFactory();
                    ManagerUI Program = new ManagerUI(serviceFactory);
                    Program.showMainMenu();
                    break;
                }
                case 2: {
                    String input;
                    if(isFirstTime) {
                        System.out.print("Would you like to load initial demo data? (yes/no): ");

                        while (true) {
                            input = scanner.nextLine().trim().toLowerCase();
                            if (!input.equals("yes") && !input.equals("no")) {
                                System.out.print("Invalid input. Please enter 'yes' or 'no': ");
                            } else {
                                break;
                            }

                        }
                        isFirstTime = false;
                    }
                    else
                        input="no";
                    if (input.equals("yes")) {

                        ServiceController.getInstance().initialize();//load the exam data

                        System.out.println("Demo data loaded successfully!\n");
                    } else {
                        if(!isFirstTime)
                            System.out.println("Starting with empty data.\n");
                    }
                    Presentation presentation = new Presentation();
                    System.out.println("Launching the main menu...\n");

                    presentation.start();
                    break;
                }
                default: {
                    System.out.println("Invalid choice! Try again.");
                    scanner.next();
                    break;
                }
                case 0: {
                    System.out.println("Thank you for using Super Lee! Goodbye!:)");
                    return;
                }
            }
        }
    }
}