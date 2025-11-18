package PresentationLayer.Inventory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.Date;

import ServiceLayer.ServiceFactory;

public class ItemUI {
   public ServiceFactory serviceFactory ;
    private final Scanner sc = new Scanner(System.in);
    public ItemUI(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }
    public void showItemMenu() {
        int choice;
        do {
            System.out.println("\n===== Item Management =====");
            System.out.println("1) Add Item");
            System.out.println("2) Update Item");
            System.out.println("3) Delete Item");
            System.out.println("4) View Item Details");
            System.out.println("5) Add unit to item");
            System.out.println("6) Remove unit from item");
            System.out.println("7) View Items");
            System.out.println("8) Apply Discount to Item");
            System.out.println("9) Remove Discount from Item");
            System.out.println("10) View Discounts for Item");
            System.out.println("11) View Price History for Item");
            System.out.println("12) View Expired Items");
            System.out.println("13) Back to Main Menu");
            System.out.print("Select an option: ");
            
            choice = sc.nextInt();
            switch (choice) {
                case 1: addItem();      break;
                case 2: updateItem();   break;
                case 3: deleteItem();   break;
                case 4: viewItem();    break;
                case 5: showAddUnitMenu(); break;
                case 6: showRemoveUnitMenu(); break;
                case 7: viewItems();    break;
                case 8: applyDiscountToItem(); break;
                case 9: removeDiscountFromItem(); break;
                case 10: viewDiscountsForItem(); break;
                case 11: viewPriceHistoryForItem(); break;
                case 12: viewExpiredItems(); break;

                case 13: System.out.println("Returning to Main Menu..."); break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        } while (choice != 13);
    }
    private void viewExpiredItems() {
        // TODO Auto-generated method stub
        System.out.println("Expired items: ");
        System.out.println(serviceFactory.checkExpiredItems());

    }
    private void viewPriceHistoryForItem() {
        // TODO Auto-generated method stub
        System.out.println("Enter item ID to view price history: ");
        int itemId = sc.nextInt();
        String priceHistory = serviceFactory.showPriceHistoryForItem(itemId); // Get the price history for the item
        System.out.println(priceHistory); // Print the price history details
    }
    private void viewDiscountsForItem() {
        // TODO Auto-generated method stub
        System.out.println("Enter item ID to view discounts: ");
        int itemId = sc.nextInt();
        int choice ;
        do {
            System.out.println("1) View active discounts for item");
            System.out.println("2) View All discounts History for item");
            System.out.println("3) Back to Item Menu");
            System.out.print("Select an option: ");
            
            choice = sc.nextInt();
            switch (choice) {
                case 1: System.out.println(serviceFactory.showActiveDiscounts(itemId)); break;
                case 2: System.out.println(serviceFactory.showActiveDiscounts(itemId)); break;
                case 3: System.out.println("Returning to Item Menu..."); break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        } while (choice != 3);
    }
    private void removeDiscountFromItem() {
        // TODO Auto-generated method stub
        System.out.println("Enter item ID to remove discount: ");
        int itemId = sc.nextInt();
        System.out.println("Enter discount ID to remove: ");
        int discountId = sc.nextInt();
        String Output= serviceFactory.removeDiscountFromItem(itemId, discountId); // Remove the discount from the item
        System.out.println(Output);
    }
    private void applyDiscountToItem() {
        // TODO Auto-generated method stub
        System.out.println("Please Make sure that you have created the discount before ");
        System.out.println("Enter item ID to apply discount: ");
        int itemId = sc.nextInt();
        System.out.println("Enter discount ID to apply: ");
        int discountId = sc.nextInt();
       String Output= serviceFactory.applyDiscountForItem(itemId, discountId); // Apply the discount to the item
        System.out.println(Output);
    }
    private void showRemoveUnitMenu() {
        // TODO Auto-generated method stub
        System.out.println("Enter item ID to remove unit: ");
        int itemId = sc.nextInt();
        System.out.println("Enter quantity to remove: ");
        int quantity = sc.nextInt();
        System.out.println("Enter location to remove unit (Warehouse/Store): ");
        String location = sc.next();
        String Output=serviceFactory.removeUnitFromItem(itemId, location, quantity); // Remove the unit from the item
        System.out.println(Output);
    }
    private void showAddUnitMenu() {
        // TODO Auto-generated method stub
        System.out.println("Enter item ID to add unit: ");
        int itemId = sc.nextInt();
        System.out.println("Enter quantity to add: ");
        int quantity = sc.nextInt();
        System.out.println("Enter location to add unit (Warehouse/Store): ");
        String location = sc.next();
        serviceFactory.addUnitToItem(itemId,location ,quantity);
    }
    private void viewItem() {
        // TODO Auto-generated method stub
        System.out.println("Enter item ID to view details: ");
        int itemId = sc.nextInt();
        System.out.println(serviceFactory.getItemById(itemId));
    }
    private void viewItems() {
        // TODO Auto-generated method stub
       System.out.println(serviceFactory.showItems());
        
    }
    private void deleteItem() {
        // TODO Auto-generated method stub
        System.out.println("Enter item ID to delete: ");
        int itemId = sc.nextInt();
        String Output=serviceFactory.removeItem(itemId);
        System.out.println(Output);

    }
    private void addItem() {
        // TODO Auto-generated method stub
        System.out.println("Enter item name: ");
        String name = sc.next();
        System.out.println("Enter item category ID: ");
        int categoryId = sc.nextInt();
        System.out.println("Enter item expiration date (YYYY-MM-DD): ");
        String expirationDate = sc.next();
        LocalDate exp = LocalDate.parse(expirationDate);


        Instant instant = exp.atStartOfDay(ZoneOffset.UTC).toInstant();

        Date expDate = Date.from(instant);

        System.out.println("Enter item quantity at warehouse: ");
        int quantityInWarehouse = sc.nextInt();
        System.out.println("Enter item quantity at store: ");
        int quantityInStore = sc.nextInt();
        System.out.println("Enter item supplier name: ");
        String supplierName = sc.next();
        System.out.println("Enter item manufacturer name: ");
        String manufacturerName = sc.next();
        System.out.println("Enter item buying price: ");
        double buyingPrice = sc.nextDouble();
        System.out.println("Enter item selling price: ");
        double sellingPrice = sc.nextDouble();
        System.out.println("Enter item demand: ");
        int demand = sc.nextInt();

        System.out.println("Enter item location at store (aisle, shelf): ");
        String aisle = sc.next();
        String shelf = sc.next();
        System.out.println("Enter item location at warehouse (aisle, shelf): ");
        String aisleW = sc.next();
        String shelfW = sc.next();

        String Output=  serviceFactory.addItem(name,categoryId,expDate,quantityInWarehouse,quantityInStore,supplierName,manufacturerName,buyingPrice,sellingPrice,demand,aisle,shelf,aisleW,shelfW);
        System.out.println(Output);
    }
    //public Item(String name,Category category , Date expirationDate, int quantityInWarehouse,int quantityInStore, Supplier supplier, double buyingPrice,double sellingPrice,int demand ,Location locationAtStore, Location locationAtWarehouse)
    public void updateItem(){
        System.out.println("Enter item Id: ");
        int ItemId = sc.nextInt();
        System.out.println("Enter new item name: ");
        String name = sc.next();
        System.out.println("Enter item's new category Id: ");
        int categoryId = sc.nextInt();
        System.out.println("Enter item expiration date (YYYY-MM-DD): ");
        String expirationDate = sc.next();

        LocalDate exp = LocalDate.parse(expirationDate);
        Instant instant = exp.atStartOfDay(ZoneOffset.UTC).toInstant();
        Date expDate = Date.from(instant);

        System.out.println("Enter item quantity in warehouse: ");
        int quantityInWarehouse = sc.nextInt();
        System.out.println("Enter item quantity in store: ");
        int quantityInStore = sc.nextInt();
        System.out.println("Enter item new Supplier name: ");
        String supplierNewName = sc.next();
        System.out.println("Enter item buying price: ");
        double buyingPrice = sc.nextDouble();
        System.out.println("Enter item selling price: ");
        double sellingPrice = sc.nextDouble();
        System.out.println("Enter item demand: ");
        int demand = sc.nextInt();
        System.out.println("Enter item location at store (aisle, shelf): ");
        String aisle = sc.next();
        String shelf = sc.next();
        System.out.println("Enter item location at warehouse (aisle, shelf): ");
        String aisleW = sc.next();
        String shelfW = sc.next();
        // Call the method to add the item to the inventory manager
        String Output=serviceFactory.updateItem(ItemId,aisle,shelf,aisleW,shelfW,name,categoryId,expDate,quantityInStore,quantityInWarehouse,supplierNewName,buyingPrice,sellingPrice,demand );
        System.out.println(Output);
    }
}
