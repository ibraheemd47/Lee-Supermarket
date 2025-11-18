package PresentationLayer.Supplier.Controllers;

import ObjectDTO.Supplier.*;
import ServiceLayer.Inventory.Response;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Supplier.*;
import ServiceLayer.Supplier.SupplierService;
import domainLayer.Supplier.Managers.Pair;

import java.util.List;
import java.util.Map;

import static PresentationLayer.Supplier.Logger.printE;
import static domainLayer.Supplier.Logger.printSuccess;

public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final AgreementService agreementService;
    public OrderController() {
        this.orderService = ServiceController.getInstance().getServiceFactory().getOrderService();
        this.productService = ServiceController.getInstance().getServiceFactory().getProductService();
        this.supplierService = ServiceController.getInstance().getServiceFactory().getSupplierService();
        this.agreementService = ServiceController.getInstance().getServiceFactory().getAgreementService();
    }

    public void add_fixed_order(FixedOrderDTO order , DaysDTO arriv_day) {
        if (order.getItem_list()==null||order.getItem_list().isEmpty()) {
            throw new IllegalArgumentException("No valid products found. Order not added.");

        }
        orderService.add_fixed_order(order,arriv_day);
        printSuccess("Order added successfully: " + order.getOrder_id());
    }
    public void add_needed_order(OrderDTO order) {
        if (order.getItem_list()==null||order.getItem_list().isEmpty()) {
            throw new IllegalArgumentException("No valid products found. Order not added.");

        }
        orderService.add_needed_order(order);
        printSuccess("Order added successfully: " + order.getOrder_id());
    }

    public void displayOrders() {
        Map<String, OrderDTO> orders = orderService.get_All_orders().isError() ? null : (Map<String, OrderDTO>) orderService.get_All_orders().getMessage();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            for (OrderDTO order : orders.values()) {
                System.out.println(order);
            }
        }
    }

//    public void addOrder(String orderId, String supplierId, Map<SupItem, Integer> itemIntegerMap, LocalDate orderDate, Contact contact) {
//        /*List<Product> products = new ArrayList<>();
//
//        for (String name : itemIntegerMap) {
//            Product product = Factory.getInstance().getProductManager().getProductByName(name);
//            if (product != null) {
//                products.add(product);
//            } else {
//                System.out.println("Warning: Product with name '" + name + "' not found.");
//            }
//        }
//*/
//
    ///*
//        Date deliveryDate;
//        try {
//            deliveryDate = java.sql.Date.valueOf(dateStr); // expects format yyyy-mm-dd
//        } catch (IllegalArgumentException e) {
//            System.out.println("Invalid date format. Order not added.");
//            return;
//        }*/
//
//        //Order order = new Order(id, supplierId, products, deliveryDate, price);
//        ordersManager.addOrder(orderId, supplierId, itemIntegerMap, orderDate, contact);
//        printSuccess("Order added successfully: " + orderId);
//    }



    public OrderDTO findOrderById(String id) {
        return orderService.findOrderById(id).isError() ? null : (OrderDTO) orderService.findOrderById(id).getMessage() ;
    }

    public List<OrderDTO> findOrderBySupId(String supId) {
        if (supId == null || supId.isEmpty()) {
            throw new IllegalArgumentException("Invalid supplier Id");
        }
        else{
            return orderService.findOrderBySupID(supId).isError() ? null : (List<OrderDTO>) orderService.findOrderBySupID(supId).getMessage() ;
        }
    }

    public void delete_order(String orderId) {
        if(orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Invalid order Id");
        }
        else{
            String output = orderService.remove_order(orderId);
            System.out.println(output);
        }
    }

    public SupItemDTO get_product_by_name(String productName, String supplierId) {
        if(productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("Invalid product Name");
        }
        else if(supplierId == null || supplierId.isEmpty()) {
            throw new IllegalArgumentException("Invalid supplier Id");
        }
        return productService.get_product_by_name(productName,supplierId).isError() ? null : (SupItemDTO) productService.get_product_by_name(productName,supplierId).getMessage() ;

    }

    public SupItemDTO get_product_by_sn(String productSerial, String supplierId) {
        if(productSerial == null || productSerial.isEmpty()) {
            throw new IllegalArgumentException("Invalid product Name");
        }
        else if(supplierId == null || supplierId.isEmpty()) {
            throw new IllegalArgumentException("Invalid supplier Id");
        }
        return productService.get_product_by_serialNumber(productSerial,supplierId).isError() ? null : (SupItemDTO) productService.get_product_by_serialNumber(productSerial,supplierId).getMessage() ;

    }

    public ContactDTO get_contact(String supplierId, String contactNumber) {
        ContactDTO toReturn=null;
        try {
            toReturn = supplierService.get_contact(supplierId,contactNumber).isError() ? null : (ContactDTO) supplierService.get_contact(supplierId,contactNumber).getMessage();
        }
        catch (Exception e){
            printE(e.getMessage());

        }

        return toReturn;
    }


    /**
     * Checks if there are any agreements associated with the given supplier ID.
     *
     * @param supplierId the unique identifier of the supplier to be checked
     * @return true if one or more agreements exist for the given supplier ID, false otherwise
     */
    public boolean check_Agreement_with_supplierID(String supplierId) {
        List<AgreementDTO> agreements = agreementService.getAllAgreementOf(supplierId).isError() ? null : (List<AgreementDTO>) agreementService.getAllAgreementOf(supplierId).getMessage();
        return agreements != null && !agreements.isEmpty();
    }

    public SupplierDTO getSupplierID(String supId) {
        Response response = supplierService.findSupplierById(supId);
        if (response.isError()) {
            return null;
        }
        else {
            return (SupplierDTO) response.getMessage();
        }

    }

    public String canel_Order(String orderId) {
         return orderService.cancel_order(orderId);
    }

    public boolean addFixedOrder(List<Pair<Integer, ProductDTO>> items, DaysDTO day) {
       return ServiceFactory.getInstance().addFixedOrder(items,day);
    }

    public ProductDTO getProductById(String id) {
      return   productService.get_product_by_Id(id).isError() ? null : (ProductDTO) productService.get_product_by_Id(id).getMessage();
    }

    public boolean addOrder(List<Pair<Integer, ProductDTO>> items) {
        return ServiceFactory.getInstance().addOrder(items);
    }
    public void next_day(){
        ServiceFactory.getInstance().next_day();
        printSuccess("Next day is set");
    }

    public String get_day() {
        return ServiceFactory.getInstance().get_day();
    }

    public String delivered(String orderId) {
        return ServiceFactory.getInstance().delivered( orderId);
    }
}