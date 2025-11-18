package PresentationLayer.Supplier;

import ObjectDTO.Supplier.*;
import PresentationLayer.Supplier.Controllers.OrderController;
import domainLayer.Supplier.Managers.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static PresentationLayer.Supplier.Logger.print;
import static PresentationLayer.Supplier.Logger.printSuccess;
import static domainLayer.Supplier.Logger.printE;


public class OrderPL {
    private final OrderController controller;
    private final Scanner scanner = new Scanner(System.in);

    public OrderPL() {
        this.controller = new OrderController();
    }

    public void start() {
        int choice = -1;
        do {
            choice = -1;
            System.out.println("==============================");
            System.out.println("           Orders Menu");
            System.out.println("==============================");
            System.out.println("1. Display All Orders");
            System.out.println("2. Add needed Order");
            System.out.println("3. Delete Order");
            System.out.println("4. display order by id");
            System.out.println("5. display order for  supplier");
            System.out.println("6. print a supplier for the order");
            System.out.println("7. Check Order Status");
            System.out.println("8. Confirm Order Received");
            System.out.println("9. Add fixed Order");
            System.out.println("10. Add Order by Products");
            System.out.println("11. Add Fixed Order by Products");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            if (scanner.hasNextInt()) {

                choice = scanner.nextInt();
            }
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1 -> controller.displayOrders();
                case 2 -> handleAddingOrder(null);
                case 3 -> handleDeleteOrder();
                case 4 -> handle_Print_Order_By_Id();
                case 5 -> handle_print_order_for_sup();
                case 6 -> handle_print_sup();
                case 7 -> get_status();
                case 8 -> confirm_recive();
                case 9 -> handleAddingFixedOrder();
                case 10 -> handleAddNewOrderByProductList();
                case 11 -> handleAddFixedOrderByProductList();
                case 0 -> print("Returning to Main Menu...");
                default -> printE("Invalid option. Please try again.");

            }
        } while (choice != 0);
    }

    private void handleAddingFixedOrder() {

        //****add regular order first*********

        // Get Order ID from a user and validate
        System.out.print("Enter Order ID to be fixed: ");
        String order_id = scanner.nextLine();
        if (!valid_str_input(order_id)) {
            return;
        }


        // Check if order already exists
        OrderDTO o = controller.findOrderById(order_id);
        if (o == null) {
            print("you still did not add the order");
            print("i will navigate to add order , enter the same id " + order_id);
            //the order still not added
            handleAddingOrder(order_id);//navigate to add order
            o = controller.findOrderById(order_id);
            if (o == null) {
                printE("order not added");
                return;
            } else {
                print("order added");
            }

        }
        //o not null

        //then add it as fixed order

        System.out.println("Enter the arrival day (1-7 Sunday-Saturday):");
        int dayOfSupplier;
        while (true) {
            if (!scanner.hasNextInt()) {
                scanner.nextLine(); // consume invalid input
                printE("Invalid input. Please enter a number between 1 and 7.");
                continue;
            }

            dayOfSupplier = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (dayOfSupplier >= 1 && dayOfSupplier <= 7) {
                break;
            } else {
                printE("Invalid day. Please enter a number between 1 and 7.");
            }
        }

        scanner.nextLine();
        DaysDTO daySup = DaysDTO.values()[dayOfSupplier - 1];


        controller.add_fixed_order(new FixedOrderDTO(o, daySup), daySup);

    }

    private void confirm_recive() {
        print("enter the order id:");

        String orderId = scanner.nextLine();
        if (orderId == null || orderId.isEmpty()) {
            printE("Invalid order id.");
            return;
        } else {
            OrderDTO order = controller.findOrderById(orderId);
            if (order == null) {
                printE("order id Not found.");
            } else {
                controller.delivered(orderId);
                printSuccess("Order " + "'" + orderId + "'" + " has been delivered successfully!");
            }
        }
    }

    private void get_status() {
        System.out.println("enter the order id:");

        String orderId = scanner.nextLine();
        if (orderId == null || orderId.isEmpty()) {
            printE("Invalid order id.");
            return;
        } else {
            OrderDTO order = controller.findOrderById(orderId);
            if (order == null) {
                printE("order id Not found.");
            } else {
                System.out.println(order.getStatus());
            }
        }
    }

    private void handle_print_sup() {
        System.out.println("enter the order id:");

        String orderId = scanner.nextLine();
        if (orderId == null || orderId.isEmpty()) {
            printE("Invalid order id.");
            return;
        } else {
            OrderDTO order = controller.findOrderById(orderId);
            if (order == null) {
                printE("order id Not found.");
            } else {
                String supId = order.getSupplier_id();
                if (supId == null || supId.isEmpty()) {
                    printE("Invalid supplier id .");
                } else {
                    SupplierDTO s = controller.getSupplierID(supId);
                    if (s == null) {
                        printE("supplier id not found.");

                    } else {
                        System.out.println(s);
                    }
                }
            }
        }
    }

    /**
     * printing all the order from supplier
     */
    private void handle_print_order_for_sup() {
        System.out.println("enter the sup id:");

        String supId = scanner.nextLine();
        if (supId == null || supId.isEmpty()) {
            printE("Invalid sup id, try again.....");
        } else {
            List<OrderDTO> orders = controller.findOrderBySupId(supId);
            if (orders != null) {
                for (OrderDTO order : orders) {
                    System.out.println(order);
                }
            }
        }
    }

    private void handle_Print_Order_By_Id() {

        System.out.println("enter the order id:");

        String orderId = scanner.nextLine();
        if (orderId == null || orderId.isEmpty()) {
            printE("Invalid order id.");
        } else {
            OrderDTO order = controller.findOrderById(orderId);
            if (order == null) {
                printE("order id Not found.");

            } else {
                System.out.println(order);
            }
        }
    }

    private void handleDeleteOrder() {
        System.out.println("enter the order id:");

        String orderId = scanner.nextLine();
        if (orderId == null || orderId.isEmpty()) {
            printE("Invalid order id.");
        } else {
            OrderDTO order = controller.findOrderById(orderId);
            if (order == null) {
                printE("order id Not found.");
            } else {
                if (order.getStatus().equals(StatusDTO.DELIVERED) || order.getStatus().equals(StatusDTO.CANCELLED))
                    printE("order is already delivered or cancelled");
                else {
                    String output = controller.canel_Order(orderId);
                    //order.cancel();
                    printSuccess(output);

                }
            }
        }

    }

    /**
     * Handles the process of adding a new order:
     * - Takes input from the user (Order ID, Supplier ID, Contact, etc.)
     * - Validates inputs
     * - Retrieves product list for the order
     * - Adds the order to the system if all data is valid
     */
    private void handleAddingOrder(String arg) {

        String order_id;
        if (arg != null && arg.isEmpty()) {
            System.out.println("get not null");
            order_id = arg;//the order id is the the input
        } else {//else add regular order take the id
            // Get Order ID from user and validate

            System.out.print("Enter Order ID: ");

            order_id = scanner.nextLine();
            if (!valid_str_input(order_id)) {
                return;
            }

            // Check if order already exists
            OrderDTO o = controller.findOrderById(order_id);
            if (o != null) {
                printE("Order already exists.try another order id");
                return;
            }
        }

        //-----------------------------------------------
        // TODO: delete this code , in get_Product_List() method use get_best_deal()
        System.out.print("Enter Order supplierId: ");

        String supplierId = scanner.nextLine();
        if (!valid_str_input(supplierId)) {///check if supplier id is valid
            return;
        }
        if (!checkSupplierId(supplierId)) {//check if supplier exists
            return;
        }
        //todo:check if needed =>get_best_deal();
        //--------------------------------------------

        // Get delivery address and validate
        System.out.println("enter the address:");

        String address = scanner.nextLine();
        if (!valid_str_input(address)) {
            return;
        }


        // Get contact name and validate
        System.out.println("enter the contact number:");

        String contact_phone = scanner.nextLine();
        if (!valid_str_input(contact_phone)) {
            return;
        }

        // Verify contact exists for supplier
        ContactDTO contact = null;

        contact = controller.get_contact(supplierId, contact_phone);
        if (contact == null) {
            printE("Contact not found for supplier with id " + supplierId);
            return;
        }


        // Get product list and quantities for the order
        List<ItemDTO> itemIntegerMap = get_Product_List(supplierId, order_id);

        // Set current date as the order date
        LocalDate order_date = LocalDate.now();


        scanner.nextLine(); // clear buffer

        // Add order if products were provided and not empty
        if (itemIntegerMap != null && !itemIntegerMap.isEmpty()) {

            //todo add function to order to get the price automatically
            controller.add_needed_order(new OrderDTO(order_id, controller.getSupplierID(supplierId).getName(), supplierId, itemIntegerMap, contact, 0, order_date, address, StatusDTO.PURCHASED));
        }
    }

    /**
     * Checks if the supplier with the given ID has an agreement in place.
     * If no agreement is found, an error message is printed, and the method returns false.
     *
     * @param supplierId the ID of the supplier to be checked
     * @return true if the supplier has an agreement, false otherwise
     */
    private boolean checkSupplierId(String supplierId) {
        if (supplierId == null || supplierId.trim().isEmpty()) {
            printE("Supplier ID cannot be null or empty");
            return false;
        }

        if (!controller.check_Agreement_with_supplierID(supplierId)) {
            printE("Supplier with ID " + supplierId + " has no agreement yet!");
            return false;
        }
        return true;
    }

    /**
     * Allows the user to build a product order list from a given supplier.
     * The user can enter products by name or serial number, specify quantity, and continue adding items.
     * If a product does not exist in the agreement, the user can choose to exit or continue.
     *
     * @param supplierId the ID of the supplier from whom to order products
     * @param order_id
     * @return a Map of SupItem objects and their requested quantities, or null if the user exits early
     */
    private List<ItemDTO> get_Product_List(String supplierId, String order_id) {
        List<ItemDTO> item_to_order = new ArrayList<>();
        System.out.println("Start adding products to the order.");
        while (true) {
            SupItemDTO p = null;
            //TODO : here we will use get_best_deal(),getSupplierLst_by_itemID(String item_id) in supplierService,agreementService
            // Ask how the user wants to search
            int searchMethod = getValidInt("Insert by: 1) Serial Number  2) Product Name", 1, 2);
            if (searchMethod == 1) {//insert nby serial number
                System.out.print("Enter product serial number: ");

                String serial = scanner.nextLine();
                p = controller.get_product_by_sn(serial, supplierId);
            } else {//insert by name
                System.out.print("Enter product name: ");

                String name = scanner.nextLine();
                p = controller.get_product_by_name(name, supplierId);
            }

            if (p == null) {
                printE("Product not found in the agreement with this supplier.");
                String choice = getYesNo("Would you like to go back? (yes/no): ");
                if (choice.equalsIgnoreCase("yes")) {
                    return null;
                } else {
                    continue;
                }
            } else {

                // Get quantity
                int quantity = getPositiveInt("Enter quantity to order: ");
                item_to_order.add(new ItemDTO(p, (order_id), quantity, p.getPrice()));

                // Ask if the user wants to add another product
                String more = getYesNo("Add another product? (yes/no): ");
                if (more.equalsIgnoreCase("no")) {
                    break;
                }
            }
        }

        return item_to_order;
    }

    /**
     * Helper method to get a valid integer between min and max.
     */
    private int getValidInt(String message, int min, int max) {
        int value;
        while (true) {
            try {
                System.out.print(message + " ");

                value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    break;
                }
                System.out.println("Please enter a valid option between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                printE("Invalid input. Please enter a number.");
            }
        }
        return value;
    }

    /**
     * Helper method to get a positive integer.
     */
    private int getPositiveInt(String message) {
        int value;
        while (true) {
            try {
                System.out.print(message + " ");
                value = Integer.parseInt(scanner.nextLine());
                if (value > 0) break;
                printE("Quantity must be greater than 0.");
            } catch (NumberFormatException e) {
                printE("Invalid input. Please enter a valid number.");
            }
        }
        return value;
    }

    /**
     * Helper method to get a yes/no answer.
     */
    private String getYesNo(String message) {
        String answer;
        while (true) {
            System.out.print(message);

            answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("yes") || answer.equals("no")) {
                break;
            }
            printE("Please answer 'yes' or 'no'.");
        }
        return answer;
    }


    private boolean valid_str_input(String input) {
        if (input == null || input.isEmpty()) {
            printE("Invalid input.try again ......");
            return false;
        }
        return true;
    }



    private void handleAddNewOrderByProductList() {
        List<Pair<Integer, ProductDTO>> items = buildProductList();
        if (items == null || items.isEmpty()) {
            printE("No products were added.");
            return;
        }

        boolean success = controller.addOrder(items);
        if (success)
            printSuccess("Order added successfully.");
        else
            printE("Failed to add order.");
    }

    private void handleAddFixedOrderByProductList() {
        List<Pair<Integer, ProductDTO>> items = buildProductList();
        if (items == null || items.isEmpty()) {
            printE("No products were added.");
            return;
        }

        int dayOfSupplier = getValidInt("Enter the arrival day (1-7, Sunday-Saturday):", 1, 7);
        DaysDTO day = DaysDTO.values()[dayOfSupplier - 1];

        boolean success = controller.addFixedOrder(items, day);
        if (success)
            printSuccess("Fixed order added successfully.");
        else
            printE("Failed to add fixed order.");
    }
    private List<Pair<Integer, ProductDTO>> buildProductList() {
        List<Pair<Integer, ProductDTO>> products = new ArrayList<>();
        while (true) {
            System.out.print("Enter product ID: ");
            String id = scanner.nextLine();
            if (!valid_str_input(id)) return null;

            ProductDTO product = controller.getProductById(id);
            if (product == null) {
                printE("Product not found.");
                continue;
            }

            int quantity = getPositiveInt("Enter quantity: ");
            products.add(new Pair<>(quantity, product));

            String more = getYesNo("Add another product? (yes/no): ");
            if (more.equals("no")) break;
        }
        return products;
    }

    public void getDay() {
        System.out.println(controller.get_day());
    }

    public void next_day() {
        controller.next_day();
    }
}
