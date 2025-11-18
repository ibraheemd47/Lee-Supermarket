package PresentationLayer.Inventory;


import java.util.Scanner;


import ServiceLayer.ServiceFactory;

public class ManagerUI {
    //public InventoryManager(int id, String name, String location, String description, int capacity, int currentStock, List<Supplier> suppliers, List<manufacturer> manufacturers, List<Report> reports, List<DefictiveItemReport> itemReports, List<Discount> discounts) {
   public ServiceFactory serviceFactory;
    private final static Scanner scanner = new Scanner(System.in);
    private AlertUi alertUi;
    private CategoryUI categoryUi;
    private ItemUI itemUi;
    private ReportUI reportUi;
    private DiscountUI discountUi;
    public ManagerUI(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        // Constructor for ManagerUI
         alertUi = new AlertUi(serviceFactory);
         categoryUi = new CategoryUI(serviceFactory);
         itemUi = new ItemUI(serviceFactory);
         reportUi = new ReportUI(serviceFactory);
         discountUi = new DiscountUI(serviceFactory);


    }

    public void showMainMenu() {
        int choice;
        do {
            System.out.println("\n===== Super Lee Inventory =====");
            System.out.println("Welcome to the Inventory Management System!");
            
            System.out.println("1) Manage Items");
            System.out.println("2) Manage Categories");
            System.out.println("3) Generate Reports");
            System.out.println("4) Manage Discounts");
            System.out.println("5) Manage Alerts");
            System.out.println("6) load data");
            System.out.println("7) Exit");
            System.out.print("Select an option: \n");
            
            choice = scanner.nextInt();
            switch (choice) {
                case 1: itemUi.showItemMenu();      break;
                case 2: categoryUi.showCategoryMenu();  break;
                case 3: reportUi.showReportMenu();    break;
                case 4: discountUi.showDiscountMenu(); break;
                case 5: alertUi.showAlertMenu(); break;
                case 6: serviceFactory.LoadProgram(); break;

                case 7: System.out.println("Goodbye!"); break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        } while (choice != 7);
    }
    public void loadData(){
            serviceFactory.LoadProgram();

    }
    
}
