package domainLayer.Supplier;

import DataAccessLayer.Supplier.ConnectionManager;
import domainLayer.Supplier.Managers.*;
import domainLayer.Supplier.Objects.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class Factory {
    private static Factory instance = null;

    private final SupplierManager supplierManager;
    private final OrdersManager ordersManager;
    private final AgreementManager agreementManager;
    private final ManufacturerManager manufacturerManager;
    private final ProductManager productManager;

    private Factory() {
        supplierManager = new SupplierManager();
        ordersManager = new OrdersManager();
        agreementManager = new AgreementManager();
        manufacturerManager = new ManufacturerManager();
        productManager = new ProductManager();
    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public AgreementManager getAgreementManager() {
        return agreementManager;
    }

    public SupplierManager getSupplierManager() {
        return supplierManager;
    }

    public OrdersManager getOrdersManager() {
        return ordersManager;
    }

    public ManufacturerManager getManufacturerManager() {
        return manufacturerManager;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public void Initialize() {
        // clear out any previous demo discounts
//        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
//            stmt.executeUpdate("DELETE FROM Discounts");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        SupplierManager sm = getSupplierManager();
        OrdersManager om = getOrdersManager();
        ManufacturerManager m = getManufacturerManager();
        AgreementManager am = getAgreementManager();

        // Create Manufacturers
        Manufacturer man1 = new Manufacturer("M01", "Tnuva");
        Manufacturer man2 = new Manufacturer("M02", "Unilever");
        Manufacturer man3 = new Manufacturer("M03", "Prigat");

        m.addManufacturer(man1);
        m.addManufacturer(man2);
        m.addManufacturer(man3);

        // Discounts
        Discount discount5 = new Discount(5, 0.1);
        Discount discount6 = new Discount(6, 0.2);
        Discount discount7 = new Discount(7, 0.3);
        Discount discount8 = new Discount(8, 0.4);
        Discount discount9 = new Discount(9, 0.5);
        Discount discount10 = new Discount(10, 0.6);

        List<Discount> discountslst1 = Arrays.asList(discount5, discount6, discount10);
        List<Discount> discountslst2 = Arrays.asList(discount7, discount8, discount9);
        List<Discount> discountslst3 = Arrays.asList(discount6, discount8, discount9);
        List<Discount> discountslst4 = Arrays.asList(discount7, discount10, discount6);

        //contacts
        Contact c1 = new Contact(1 + "", "zaki", "0543571052", "zakimasarwe3@gmail.com");
        Contact c2 = new Contact(2 + "", "mostfa", "+972 53-724-4159", "mostfa@superpharm.com");
        Contact c3 = new Contact(3 + "", "ali", "050-0001111", "ali@ramilevi.com");
        Contact c4 = new Contact(4 + "", "mahmod ", "052-9386601", "mahmoud.heltawe@gmail.com");
        // Supplier 1 - Shufersal
        List<Contact> contacts1 = List.of(c1);
        Supplier supplier1 = new Supplier("1", "Shufersal", "Bank1", contacts1, Days.MONDAY);
        sm.addSupplier(supplier1.ConvertToDTO());

        Product milk = new Product("1", "Milk");
        Product cheese = new Product("2", "Cheese");

        productManager.addProduct(milk);
        productManager.addProduct(cheese);

        // Supplier 2 - SuperPharm
        List<Contact> contacts2 = List.of(c2);
        Supplier supplier2 = new Supplier("2", "SuperPharm", "Bank2", contacts2, Days.MONDAY);
        sm.addSupplier(supplier2.ConvertToDTO());

        Product shampoo = new Product("3", "Shampoo");
        Product soap = new Product("4", "Soap");

        productManager.addProduct(shampoo);
        productManager.addProduct(soap);

        List<Product> products2 = Arrays.asList(shampoo, soap);

        // Supplier 3 - Rami Levi
        List<Contact> contacts3 = new ArrayList<>();
        contacts3.add(new Contact(3 + "", "ali", "050-0001111", "ali@ramilevi.com"));
        Supplier supplier3 = new Supplier("3", "Rami Levi", "Bank3", contacts3, Days.FRIDAY);
        sm.addSupplier(supplier3.ConvertToDTO());

        Product juice = new Product("5", "Apple Juice");
        productManager.addProduct(juice);
        List<Product> products3 = List.of(juice);

        //    public SupItem(Product item,String supplier_id, double price, List<Discount> quantityForDiscount,Manufacturer manufacturer, String supplierCatNum) {
        // Supitem
// Supplier 1 – Shufersal:
        SupItem milk1 = new SupItem(milk, "1","AG01", 10.5, discountslst1, man1, "sup1.1");
        SupItem cheese1 = new SupItem(cheese, "1","AG01", 15.0, discountslst2, man1, "sup1.2");

// … then Agreement1 uses milk1 & cheese1 …


// Supplier 2 – SuperPharm:
        SupItem shampoo2 = new SupItem(shampoo, "2","AG02", 7.5, discountslst3, man2, "sup2.1");
        SupItem milk2 = new SupItem(milk, "2","AG02", 10,new ArrayList<>(), man1, "sup2.2");

        SupItem soap2 = new SupItem(soap, "2","AG02", 5.0, discountslst2, man2, "sup2.2");

// … Agreement2 uses shampoo2 & soap2 …


// Supplier 3 – Rami Levi:
        SupItem juice3 = new SupItem(juice, "3","AG03", 6.5, discountslst4, man3, "sup3.1");
// … Agreement3 uses juice3 …

        // Agreement for supplier 1
        Map<String, SupItem> items1 = new HashMap<>();
        items1.put(milk.getId(), milk1);
        items1.put(cheese.getId(), cheese1);
        Agreement agreement1 = new Agreement("AG01", "1", items1, true, "17-4-2025, cash");

        am.addAgreement(agreement1.toDTO());//convert to dto

// Agreement for supplier 2
        Map<String, SupItem> items2 = new HashMap<>();
        items2.put(shampoo.getId(), shampoo2);
        items2.put(soap.getId(), soap2);
        items2.put(milk.getId(), milk2);
        Agreement agreement2 = new Agreement("AG02", "2", items2, false, "18-4-2025, cash");
        am.addAgreement(agreement2.toDTO());

// Agreement for supplier 3
        Map<String, SupItem> items3 = new HashMap<>();
        items3.put(juice.getId(), juice3);
        Agreement agreement3 = new Agreement("AG03", "3", items3, true, "17-4-2025, visa");
        am.addAgreement(agreement3.toDTO());

        // Orders
        //SupItem milkS = new SupItem(milk, 7.2, null);
        //SupItem cheeseS = new SupItem(cheese, 4.4, null);

        Map<SupItem, Integer> Purchesed_item = new HashMap<>();
        Purchesed_item.put(milk1, 5);
        Purchesed_item.put(cheese1, 10);
        Map<SupItem, Integer> Purchesed_item2 = new HashMap<>();
        //Purchesed_item2.put(milks, 5);
        Purchesed_item2.put(shampoo2, 10);
        Purchesed_item2.put(soap2, 10);
        Map<SupItem, Integer> Purchesed_item3 = new HashMap<>();
        Purchesed_item3.put(juice3, 5);
//        Purchesed_item3.put(shampo_S, 2);
//        //Purchesed_item3.put(soap_s, 10);
//        Purchesed_item3.put(juice_s, 3);

        //contacts


        Order order1 = new Order("100","1", "Shufersal", Purchesed_item, LocalDate.of(2025, 4, 17), c1, "tybe cite almkaser");
        om.addOrder(order1);

        Order order2 = new Order("101", "2","SuperPharm", Purchesed_item2, LocalDate.of(2020, 4, 20), c2, "baer shaeva");
        om.addOrder(order2);

        Order order3 = new Order("102", "3","Rami Levi", Purchesed_item3, LocalDate.of(2010, 1, 17), c4,"here");
        om.addOrder(order3);


        System.out.println("new demo data inserted successfully! (v1.1)");
    }

}
