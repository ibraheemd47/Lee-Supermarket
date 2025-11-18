package PresentationLayer.Supplier;

import ObjectDTO.Supplier.*;
import PresentationLayer.Supplier.Controllers.SupplierController;
import domainLayer.Supplier.Factory;

import java.util.*;

import static domainLayer.Supplier.Logger.*;


public class SupplierPL {
    private final SupplierController controller;
    private final Scanner scanner = new Scanner(System.in);

    public SupplierPL() {
        this.controller = new SupplierController();
    }

    public void start() {
        int choice = -1;// init value, will change accord to the choice
        do {
            choice = -1;
            System.out.println("================================");
            System.out.println("         Supplier Menu");
            System.out.println("================================");
            System.out.println("1. Display All Suppliers");
            System.out.println("2. Add Supplier");
            System.out.println("3. Find Supplier by ID");
            System.out.println("4. Delete Supplier by ID");
            System.out.println("5. Create Agreement with Supplier");
            //new options
            System.out.println("6. Update Agreement");
            System.out.println("7. Delete Agreement");
            //-------
            System.out.println("8. Show Supplier Contacts");
            System.out.println("9. Add Contact");
            System.out.println("10. View Supplier Agreements");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Invalid choice, try again....");
                break;
            }
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> displaySuppliers();
                case 2 -> handleAddingSupplier();
                case 3 -> handlePrintingSupplier();
                case 4 -> Delete_Supplier_by_ID();
                case 5 -> creat_agreement();
                //---------------
                case 6 -> update_agreement();
                case 7 -> delete_agreement();
                //---------------
                case 8 -> Show_contacts_of_sup();
                case 9 -> add_contact();
                case 10 -> display_agreements();
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 0);

    }

    public void delete_agreement() {
        System.out.println("\n=== Delete Agreement ===");
        System.out.println("enter the agreement ID");
        String agID = scanner.nextLine();
        if (check_NonExisting_agreement(agID)) {
            printE("No such agreement!");
            return;
        }
        if (controller.delete_agreement(agID)) {
            printSuccess("Agreement deleted successfully!");
        } else {
            printE("Failed to delete agreement.");
        }
    }

    public void update_agreement() {
        System.out.println("\n=== Update Agreement ===");
        System.out.println("enter the agreement ID");
        String agID = scanner.nextLine();
        if (check_NonExisting_agreement(agID)) {
            printE("No such agreement!");
            return;
        }
        int choice = -1;// init value, will change accord to the choice
        do {
            choice = -1;
            System.out.println("\n=== Choose update type: ===");
            System.out.println("1. Update items");
            System.out.println("2. Update delivery of supplier");
            System.out.println("3. Update payment");
            System.out.println("0. Back to Supplier Menu");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                printE("Invalid choice, try again....");
            }
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: {
                    int choice2 = -1;
                    //serial number
                    System.out.println("1. Remove items");
                    System.out.println("2. Add item");
                    System.out.println("3. Remove item by ID");
                    System.out.println("0. Back");
                    while (!scanner.hasNextInt()) {//loop until get a right num
                       scanner.nextLine();
                        System.out.println("Invalid choice, try again....");

                    }
                    choice2 = scanner.nextInt();

                    scanner.nextLine(); // consume newline
                    switch (choice2) {
                        case 1: {

                            System.out.println(controller.removeItems(agID));
                            break;
                        }
                        case 2: {
                            //first should check if item already exist in the agreement!
                            System.out.println("Enter the Product ID:");
                            while (!scanner.hasNextInt()) {
                                System.out.println("Invalid input. Please try again...");
                                scanner.nextLine(); // consume the invalid input
                            }
                            int productId = scanner.nextInt();
                            scanner.nextLine(); // consume the leftover newline character

                            AgreementDTO curr = controller.getAgreement(agID);

                            for (Map.Entry<String, SupItemDTO> entry : curr.items().entrySet()) {
                                if (entry.getValue().getproduct().getId().equals(productId)) {
                                    System.out.println("Item already exists!");
                                    break;
                                }
                            }
                            //else not exist add it to the agreement!

                            //check if that exist or new proudct
                            ProductDTO productDTO = controller.getProudct(productId+"");
                            String productName;
                            if (productDTO == null) {
                                System.out.println("enter the Product Name");
                                productName = scanner.nextLine();
                                while (!validate_input(productName)) {
                                    productName = scanner.nextLine();
                                }
                                productDTO = new ProductDTO(productId+"", productName);
                                String output = controller.addProduct(productDTO);
                                print(output);
                            }
                            controller.add_Item_inventory(productId+"", curr.supplierId());
                            System.out.println("enter the supplier catalog number:");
                            String catalogNumber = scanner.nextLine();
                            while (!validate_input(catalogNumber)) {
                                catalogNumber = scanner.nextLine();
                            }
                            System.out.println("enter the Product manufacturer id:");
                            String manufacturerId = scanner.nextLine();
                            while (!validate_input(manufacturerId)) {
                                manufacturerId = scanner.nextLine();
                            }
                            ManufacturerDTO manu =controller.find_Manfacurer(manufacturerId);
                            if(manu==null) {
                                System.out.println("enter the Product manufacturer name:");
                                String manufacturerName = scanner.nextLine();
                                while (!validate_input(manufacturerName)) {
                                    manufacturerName = scanner.nextLine();
                                }
                                manu=new ManufacturerDTO(manufacturerId,manufacturerName);
                                controller.add_manufacturer(manu);//added to manufacturer if not exist
                            }

                            System.out.println("how many type of discount this item have ?");
                            if (!scanner.hasNextInt()) {
                                scanner.nextLine();
                                printE("Invalid input. Please try again....");
                                break;
                            }
                            int discount = scanner.nextInt();
                            scanner.nextLine();
                            List<DiscountDTO> discounts_list = new ArrayList<>();
                            for (int j = 0; j < discount; j++) {
                                System.out.println("enter the discount percentage");
                                double percentage = scanner.nextDouble();
                                scanner.nextLine();
                                System.out.println("enter the discount quantity to get this percentage " + percentage + "%");
                                if (!scanner.hasNextInt()) {
                                    scanner.nextLine();
                                    printE("Invalid input. Please try again....");
                                    break;
                                }
                                int discountQuantity = scanner.nextInt();
                                scanner.nextLine();
                                discounts_list.add(new DiscountDTO(discountQuantity, percentage));
                            }

                            System.out.println("enter the normal price");
                            if (!scanner.hasNextDouble()) {
                                printE("Invalid input. Please try again....");
                                printE("Item has not been added! You can add item by updating agreement.");
                                break;
                            }
                            double normalPrice = scanner.nextDouble();
                            scanner.nextLine();
//                            ManufacturerDTO manf = controller.find_Manfacurer(manufacturerId);
//                            if (manf == null) {
//                                manf = new ManufacturerDTO(manufacturerId, manufacturerName);
//                            }
                            //ProductDTO product = new ProductDTO(productId, productName);
                            SupItemDTO toAdd = new SupItemDTO(productDTO, manu, curr.supplierId(), curr.agreementId(), normalPrice, catalogNumber, discounts_list);
                            printSuccess( controller.addItem(agID, ""+productId, toAdd));
                            break;
                        }
                        case 3: {

                            int productID;
                            while (true) {
                                System.out.println("enter the Product ID");
                                while (!scanner.hasNextInt()) {
                                    System.out.println("insert a integer number");
                                }
                                productID = scanner.nextInt();


                                if (!controller.checkItemAgreement(agID,productID+"")) {
                                    printE("Product ID does not exist in the agreement.");
                                    System.out.println("Do you want to continue? (yes/no)");
                                    String yesOrNo = scanner.nextLine();
                                    if (!yesOrNo.trim().toLowerCase().equals("yes")) {

                                        break;
                                    }
                                    continue;
                                }
                                break;
                            }
                            SupItemDTO v = controller.remove_item_from_agreement(agID, productID+"");
                            if (v != null) {
                                controller.remove_supItem_inventory(controller.getAgreement(agID).supplierId(),productID);
                                printSuccess("Item removed successfully!");
                            }
                            else
                                printE("Item does not exist!");
                            break;
                        }
                        default: {
                            print("Returning ...");
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    boolean supplierDeliverIt;
                    String isDelivered;
                    while (true) {
                        System.out.print("Does the supplier deliver it? (yes/no): ");
                        isDelivered = scanner.nextLine().trim().toLowerCase();

                        switch (isDelivered) {
                            case "yes":
                                supplierDeliverIt = true;
                                break;
                            case "no":
                                supplierDeliverIt = false;
                                break;
                            default:
                                printE("Invalid input. Please type 'yes' or 'no'.");
                                continue;
                        }
                        System.out.println(controller.update_DeliveryOfSupplier(agID, supplierDeliverIt));
                        break; // exit the loop if input was valid
                    }
                    break;
                }
                case 3: {
                    System.out.println("enter the payment (when and how):");
                    String payment = scanner.nextLine();
                    printSuccess(controller.updatePayment(agID, payment));
                    break;
                }
            }

        } while (choice != 0);
    }

    private boolean validate_input(String manufacturerName) {
        if (manufacturerName == null || manufacturerName.trim().isEmpty()) {
            printE("Invalid input. Please try again....");
            return false;
        }
        if (manufacturerName.length() > 25) {
            printE("Manufacturer name is too long. Please try again....");
            return false;
        }
        return true;
    }

    /**
     * function that print all the contracts 4
     */


    public void display_agreements() {
        System.out.println("enter the supplier Id :");
        String supplierId = scanner.nextLine();
        if (!valid_SupplierId(supplierId))
            return;

        List<AgreementDTO> agreemt_to_Show = controller.getAllAgreementOf(supplierId);
        if (agreemt_to_Show != null && !agreemt_to_Show.isEmpty()) {
            agreemt_to_Show.forEach(agreemt -> System.out.println(agreemt.toString()));
            //agreemt_to_Show.stream().map(x->x.toString()).forEach(System.out::println);
        } else {
            printE("There is no agreement to show ");
            return;
        }
    }

    public void add_contact() {
        System.out.println("enter the supplier Id :");
        String supplierId = scanner.nextLine();
        if (!valid_SupplierId(supplierId))
            return;

        List<ContactDTO> contacts_to_add = createContactsFromUser();
        if (contacts_to_add == null || contacts_to_add.isEmpty()) {
            printE("Invalid Contacts. Please try again....");
            return;
        }
        for (ContactDTO contact : contacts_to_add) {
            controller.addContactToSupplier(supplierId, contact); // Use controller to add contacts
        }
        printSuccess("Contact(s) added successfully!");
    }

    public void Show_contacts_of_sup() {
        System.out.println("enter the supplier Id :");
        String supplierId = scanner.nextLine();
        if (!valid_SupplierId(supplierId))
            return;
        List<ContactDTO> contacts_to_show = null;
        try {
            SupplierDTO s = controller.findSupplier(supplierId);
            contacts_to_show = s.getContact();

        } catch (Exception e) {
            printE(e.getMessage());
        }
        if (contacts_to_show == null || contacts_to_show.isEmpty()) {
            printE("There is no contact to show ");
            return;
        }
        contacts_to_show.forEach(x -> System.out.println(x.toString()));
    }

    private void creat_agreement2() {
        System.out.println("\n=== Agreement ===");
        System.out.println("enter the agreement ID");
        String agreementId = scanner.nextLine();
        if (!check_NonExisting_agreement(agreementId)) {
            return;
        }
        System.out.println("enter the supplier ID");
        String supplierId = scanner.nextLine();
        if (!valid_SupplierId(supplierId))
            return;
        System.out.println("enter the payment (when and how):");
        String payment = scanner.nextLine();

        boolean supplierDeliverIt;
        String isDelivered;
        while (true) {
            System.out.print("Does the supplier deliver it? (yes/no): ");
            isDelivered = scanner.nextLine().trim().toLowerCase();

            switch (isDelivered) {
                case "yes":
                    supplierDeliverIt = true;
                    break;
                case "no":
                    supplierDeliverIt = false;
                    break;
                default:
                    printE("Invalid input. Please type 'yes' or 'no'.");
                    continue;
            }
            break; // exit the loop if input was valid
        }
        //supplierDeliverIt = scanner.nextBoolean();
        List<SupItemDTO> items = get_the_Item_List(supplierId, agreementId);
        Map<String, SupItemDTO> map_item = conert_to_map(items);//<serial number , subitem>
        if (map_item.isEmpty()) {
            printE("No items in agreement. Aborting.");
            return;
        }
        AgreementDTO agreement = new AgreementDTO(agreementId, supplierId, map_item, supplierDeliverIt, payment);
        controller.add_agreement(agreement);
        printSuccess("Agreement added successfully!");

    }

    public void creat_agreement() {
        System.out.println("\n=== Agreement ===");
        System.out.println("enter the agreement ID");
        String agreementId = scanner.nextLine();
        if (!check_NonExisting_agreement(agreementId)) {
            printE("Agreement already exists or ID invalid.");
            return;
        }
        System.out.println("enter the supplier ID");
        String supplierId = scanner.nextLine();
        if (!valid_SupplierId(supplierId)) return;
        System.out.println("enter the payment (when and how):");
        String payment = scanner.nextLine();

        boolean supplierDeliverIt;
        while (true) {
            System.out.print("Does the supplier deliver it? (yes/no): ");
            String isDelivered = scanner.nextLine().trim().toLowerCase();
            if (isDelivered.equals("yes")) {
                supplierDeliverIt = true;
                break;
            } else if (isDelivered.equals("no")) {
                supplierDeliverIt = false;
                break;
            } else {
                printE("Invalid input. Please type 'yes' or 'no'.");
            }
        }

        List<SupItemDTO> items = get_the_Item_List(supplierId, agreementId);
        if (items == null) {
            printE("No items were added to agreement. Agreement creation aborted.");
            return;
        }
        items.forEach(subItem -> controller.add_Item_inventory(subItem.getproduct().getId(),supplierId));//proudct id , supplier id  adding to the inventory data base
        Map<String, SupItemDTO> map_item = conert_to_map(items);
        AgreementDTO agreement = new AgreementDTO(agreementId, supplierId, map_item, supplierDeliverIt, payment);
        controller.add_agreement(agreement);
        printSuccess("Agreement added successfully!");
    }

    /**
     * convert the supitem list to map list with key serial number
     *
     * @param items item list to covert
     * @return the map
     */
    private Map<String, SupItemDTO> conert_to_map(List<SupItemDTO> items) {
        Map<String, SupItemDTO> map_item = new HashMap<>();
        if (items == null || items.isEmpty()) {
            return map_item;
        }
        for (SupItemDTO item : items) {
            map_item.put(item.getSupplierCatNum(), item);
        }
        return map_item;
    }

    private boolean valid_SupplierId(String supplierId) {
        try {
            if (supplierId == null || supplierId.isEmpty()) {
                printE("Invalid Supplier Id. Please try again....");
                return false;
            }
            SupplierDTO s = controller.findSupplier(supplierId);
            if (s == null) {
                printE("no such supplier");
                return false;
            }
            return true;
        } catch (Exception e) {
            printE("Invalid Supplier Id. Please try again....");
            return false;
        }
    }

    private boolean check_NonExisting_agreement(String agreementId) {
        if (agreementId == null || agreementId.isEmpty()) {
            printE("agreement id is null or empty");
            return false;
        }
        if (Factory.getInstance().getAgreementManager().getAgreementById(agreementId) == null) return true;
        print("agreement already exist");
        return false;
    }

    private List<SupItemDTO> get_the_Item_List(String supplierId, String agreementId) {
        List<SupItemDTO> items = new ArrayList<>();
        System.out.println("how many items do you want to add?");
        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            printE("Invalid input. Please try again....");
            return null;
        }
        int quantity = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < quantity; i++) {
            System.out.println("Enter the Product ID:");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please try again... should be integer number.");
                scanner.nextLine(); // consume the invalid input
            }
            int productId = scanner.nextInt();
            scanner.nextLine(); // consume the leftover newline character

            System.out.println("enter the Product Name");
            String productName = scanner.nextLine();
            System.out.println("enter the Product manufacturer name:");
            String manufacturerName = scanner.nextLine();
            System.out.println("enter the Product manufacturer id:");
            String manufacturerId = scanner.nextLine();
            System.out.println("enter the supplier catalog number:");
            String catalogNumber = scanner.nextLine();
            System.out.println("how many type of discount this item have ?");
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                printE("Invalid input. Please try again....");
                return null;
            }
            int discount = scanner.nextInt();
            scanner.nextLine();
            List<DiscountDTO> discounts_list = new ArrayList<>();
            for (int j = 0; j < discount; j++) {
                System.out.println("enter the discount percentage");
                double percentage = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("enter the discount quantity to get this percentage " + percentage + "%");
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    printE("Invalid input. Please try again....");
                    return null;
                }
                int discountQuantity = scanner.nextInt();
                scanner.nextLine();
                discounts_list.add(new DiscountDTO(discountQuantity, percentage));
            }
            System.out.println("enter the normal price");
            if (!scanner.hasNextDouble()) {
                scanner.nextLine();
                printE("Invalid input. Please try again....");
                printE("Item has not been added! You can add item by updating agreement.");
                break;
            }
            double normalPrice = scanner.nextDouble();
            scanner.nextLine();
            ManufacturerDTO manf = controller.find_Manfacurer(manufacturerId);
            if (manf == null) {
                manf = new ManufacturerDTO(manufacturerId, manufacturerName);
            }
            ProductDTO product = new ProductDTO(productId+"", productName);
            SupItemDTO toAdd = new SupItemDTO(product, manf, supplierId, agreementId, normalPrice, catalogNumber, discounts_list);
            items.add(toAdd);


        }
        return items;
    }

    public void Delete_Supplier_by_ID() {
        System.out.println("Enter Supplier ID");
        String supplierId = scanner.nextLine();
        if (supplierId == null || supplierId.isEmpty()) {
            printE("Invalid supplier ID");
            return;
        }
        if (controller.findSupplier(supplierId) == null) {
            printE("Supplier does not exist");
            return;
        }
        controller.delete_supplier(supplierId);
        printSuccess("Supplier deleted successfully!");
    }


    public void displaySuppliers() {

        controller.displaySuppliers();

    }

    private void displaySupplier(String id) {
        SupplierDTO s = controller.findSupplier(id);
        if (s != null)
            System.out.println(s);
        else
            printE("Supplier not found.");
    }

    public void handleAddingSupplier() {
        System.out.print("Enter Supplier ID: ");
        String id = scanner.nextLine();
        if (id == null || id.isEmpty()) {
            printE(" id is null or empty");

            return;
        } else {

            if (controller.findSupplier(id) != null) {
                printE("supplier with this id already exist \n");
                return;
            }

        }
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Bank: ");
        String bank = scanner.nextLine();

        String hasFixedDays;
        while (true) {
            System.out.print("Does the supplier have fixed delivery days? (yes/no): ");
            hasFixedDays = scanner.nextLine().trim().toLowerCase();
            if (hasFixedDays.equals("yes") || hasFixedDays.equals("no")) {
                break;
            }
            printE("Invalid input. Please type 'yes' or 'no'.");
        }
        DaysDTO daySup = null;

        if (hasFixedDays.equals("yes")) {
            System.out.println("Enter the date of supplier (1-7 Sunday-Saturday)");
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                printE("Invalid input. Please try again....");
                return;
            }
            int dayOfSupplier = scanner.nextInt();
            scanner.nextLine();
            while (dayOfSupplier < 1 || dayOfSupplier > 7) {
                printE("Invalid input. Please enter a number between 1 and 7");
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    printE("Invalid input. Please try again....");
                    return;
                }
                dayOfSupplier = scanner.nextInt();
                scanner.nextLine();
            }
            daySup = DaysDTO.values()[dayOfSupplier - 1];
        }

        List<ContactDTO> contact = createContactsFromUser();


        printSuccess( controller.addSupplier(id, name, bank, contact, daySup));



    }

    public void handlePrintingSupplier() {
        System.out.print("Enter Supplier ID to search: ");
        String id = scanner.nextLine();
        if (!valid_SupplierId(id))
            return;
        displaySupplier(id);
    }

    /**
     * function get all the contact of the supplier from the user
     *
     * @return list of contacts
     */
    private List<ContactDTO> createContactsFromUser() {
        List<ContactDTO> contacts = new ArrayList<>();
        int numOfContacts;

        while (true) {
            System.out.print("Enter number of contacts: ");
            if (scanner.hasNextInt()) {
                numOfContacts = scanner.nextInt();
                scanner.nextLine(); // clean buffer
                break;
            }
            scanner.nextLine();
            printE("Invalid input. Please try again....");
        }

        for (int i = 0; i < numOfContacts; i++) {
            System.out.println("Contact #" + (i + 1));
            int id;

            while (true) {
                System.out.print("Enter Contact ID: ");
                if (scanner.hasNextInt()) {
                    id = scanner.nextInt();
                    scanner.nextLine(); // clean buffer
                    break;
                }
                scanner.nextLine();
                printE("Invalid ID. Please enter a number.");
            }

            String name;
            while (true) {
                System.out.print("Enter Contact Name: ");
                name = scanner.nextLine();
                if (isValidName(name)) {
                    break;
                }
                printE("Invalid name format. Name should contain only letters and spaces.");
            }

            String phone;
            while (true) {
                System.out.print("Enter Contact Phone: ");
                phone = scanner.nextLine();
                if (isValidPhone(phone)) {
                    break;
                }
                printE("Invalid phone format. Phone should contain only digits, '+', '-' and spaces.");
            }

            String email;
            while (true) {
                System.out.print("Enter Contact Email (or '-' to skip): ");
                email = scanner.nextLine().trim();
                if (email.equals("-") || email.isEmpty()) {
                    email = "";
                    break;
                }
                if (isValidEmail(email)) {
                    break;
                }
                printE("Invalid email format. Please try again or enter '-' to skip.");
            }

            contacts.add(new ContactDTO(id + "", name, phone, email));
        }
        return contacts;
    }

    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return name.matches("^[a-zA-Z\\s]+$");
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return phone.matches("^[0-9+\\-\\s]+$");
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        if (email.trim().isEmpty()) return true;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}