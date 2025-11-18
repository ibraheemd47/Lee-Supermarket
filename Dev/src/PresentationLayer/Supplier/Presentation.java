package PresentationLayer.Supplier;

import jdk.jshell.spi.ExecutionControl;

import java.util.Scanner;

public class Presentation {
    private final Scanner scanner = new Scanner(System.in);

    public void start() throws ExecutionControl.NotImplementedException {
        while (true) {
            printMainMenu();

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> handleSupplierMenu();
                case 2 -> handleOrderMenu();
                case 3 -> handleDayInfoMenu();
                case 0 -> {
                    System.out.println("Thank you for using Super Lee! Goodbye! :)");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("=====================================");
        System.out.println("           Main Menu");
        System.out.println("=====================================");
        System.out.println("1 Manage Suppliers and Agreements");
        System.out.println("2 Manage Orders");
        System.out.println("3 Day Info");
        System.out.println("0 Exit");
        System.out.print("Choose an option: ");
    }

    private void handleSupplierMenu() {
        new SupplierPL().start();
    }

    private void handleOrderMenu() {
        new OrderPL().start();
    }

    private void handleDayInfoMenu() {
        System.out.println("1 What day is today?");
        System.out.println("2 Next day");

        String input = scanner.nextLine();
        int dayChoice;
        try {
            dayChoice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a number.");
            return;
        }

        OrderPL ordersPL = new OrderPL();
        switch (dayChoice) {
            case 1 -> ordersPL.getDay();
            case 2 -> ordersPL.next_day();
            default -> System.out.println("Invalid choice! Try again.");
        }
    }
}
