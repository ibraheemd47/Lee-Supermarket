import ObjectDTO.Supplier.*;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Supplier.AgreementService;
import ServiceLayer.Supplier.OrderService;
import ServiceLayer.Supplier.SupplierService;
import domainLayer.Supplier.Managers.Pair;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceIntegrationTest {

    private static OrderService orderService;
    private static SupplierService supplierService;
    private static AgreementService agreementService;

    @BeforeEach
    public void setup() {
        orderService = OrderService.getInstance();
        supplierService = SupplierService.getInstance();
        agreementService = AgreementService.getInstance();
    }
    @AfterEach
    public void tearDown() {
        orderService.remove_order("0");
        orderService.remove_order("order1");
        orderService.remove_order("order2");
        supplierService.clear_suppliers();
        orderService.remove_order("nextDayOrderId");
        orderService.remove_order("deliveredOrderId");
        orderService.remove_order("existingOrderId");
    }

    @Test
    public void testAddAndFindNeededOrder() {
        // Setup a dummy order with minimum details; you'll want to fill with your real DTOs
        OrderDTO order = new OrderDTO(
                "order1",
                "SupplierName",
                "supplierId",
                List.of(), // empty items list for simplicity
                new ContactDTO("10","c1","053","gmail@a.com"), // contact
                0.0,
                LocalDate.now(),
                "store",
                null // status
        );

        orderService.add_needed_order(order);

        OrderDTO foundOrder = orderService.findOrderById("order1").isError() ? null : (OrderDTO) orderService.findOrderById("order1").getMessage();
        assertNotNull(foundOrder);
        assertEquals("SupplierName", foundOrder.getSupplier_name());
    }

    @Test
    public void testAddAndGetFixedOrder() {
        // Create a FixedOrderDTO using the OrderDTO above + DaysDTO
        OrderDTO order = new OrderDTO("order2", "SupplierName2", "supplierId2", List.of(), new ContactDTO("10","c1","053","gmail@a.com"), 0.0, LocalDate.now(), "store", null);
        DaysDTO day = DaysDTO.FRIDAY; // create and set days as per your DTO constructor
        FixedOrderDTO fixedOrder = new FixedOrderDTO(order, day);

        orderService.add_fixed_order(fixedOrder, day);

        Map<String, OrderDTO> allOrders = orderService.get_All_orders().isError() ?null : (Map<String, OrderDTO>) orderService.get_All_orders().getMessage();
        assertTrue(allOrders.containsKey("order2") || allOrders.containsKey("order1")); // depending on your manager implementation
    }

    @Test
    public void testRemoveAndCancelOrder() {
        // Add an order to remove/cancel
        OrderDTO order = new OrderDTO("orderToRemove", "SupplierName", "supplierId", List.of(), new ContactDTO("10","c1","053","gmail@a.com"), 0.0, LocalDate.now(), "store", null);
        orderService.add_needed_order(order);

        orderService.cancel_order("orderToRemove");
        OrderDTO canceled = orderService.findOrderById("orderToRemove").isError() ? null : (OrderDTO) orderService.findOrderById("orderToRemove").getMessage();
        // depends on your manager implementation if cancel_order deletes or marks status, adjust accordingly
        assertNotNull(canceled);

        orderService.remove_order("orderToRemove");
        OrderDTO removed = orderService.findOrderById("orderToRemove").isError() ? null : (OrderDTO) orderService.findOrderById("orderToRemove").getMessage();
        assertNull(removed);
    }


    @Test
    public void testIsUniqId() {

        assertTrue(orderService.is_uniq_id("uniqueOrderId"));
        OrderDTO order = new OrderDTO("existingOrderId", "SupplierName", "supplierId", List.of(), new ContactDTO("10", "c1", "053", "gmail@a.com"), 0.0, LocalDate.now(), "store", null);
        orderService.add_needed_order(order);
        assertFalse(orderService.is_uniq_id("existingOrderId"));
    }

    @Test
    public void testNextDay() {
        List<OrderDTO> ordersToday = orderService.next_day();
        assertNull(ordersToday);
    }

    @Test
    public void testNextDay_WithOrders() {
        SupplierDTO supplier = new SupplierDTO("supplierId", "SupplierName", "Bank001", List.of(), DaysDTO.valueOf(LocalDate.now().plusDays(1).getDayOfWeek().toString()));
        supplierService.addSupplier(supplier);
        OrderDTO order = new OrderDTO("nextDayOrderId", "SupplierName", "supplierId", List.of(), new ContactDTO("10", "c1", "053", "gmail@a.com"), 0.0, LocalDate.now(), "store", null);
        orderService.add_needed_order(order);
        assertEquals(1, orderService.next_day().size());
    }


    @Test
    public void testGetDay() {
        String currentDay = orderService.get_day();
        assertNotNull(currentDay);
        assertTrue(List.of("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY").contains(currentDay));
    }

    @Test
    public void testDelivered() {
        OrderDTO order = new OrderDTO("deliveredOrderId", "SupplierName", "supplierId", List.of(), new ContactDTO("10", "c1", "053", "gmail@a.com"), 0.0, LocalDate.now(), "store", null);
        orderService.add_needed_order(order);

        orderService.delivered("deliveredOrderId");
        OrderDTO deliveredOrder = orderService.findOrderById("deliveredOrderId").isError() ? null : (OrderDTO) orderService.findOrderById("deliveredOrderId").getMessage();
        assertNotNull(deliveredOrder);
    }

    @Test
    public void testNextDay_WithOrders2() {
        List<ContactDTO> contacts = new ArrayList<>();
        contacts.add(new ContactDTO("1","mostfa_test","02","<EMAIL>"));
        SupplierDTO supplier1 = new SupplierDTO("supplierId1", "SupplierName1", "Bank001", contacts, DaysDTO.valueOf(LocalDate.now().plusDays(1).getDayOfWeek().toString()));
        supplierService.addSupplier(supplier1);
        Map<String,SupItemDTO> map=new HashMap<>();
        map.put("1",new SupItemDTO(new ProductDTO("1","milk"),new ManufacturerDTO("0","tnova"),"item01", "Item Name", 10, "1", new ArrayList<>()));
        String textOfadd=agreementService.add_agreement(new AgreementDTO("agreementId", "supplierId1",map , true, "cash"));
        System.out.println("the add agreement "+textOfadd);
        System.out.println("the agreements that i have :"+agreementService.getAllAgreementOf("supplierId1"));
        //FixedOrderDTO fixedOrder = new FixedOrderDTO(new OrderDTO("nextDayFixedOrderId", "SupplierName", "supplierId",new ArrayList<ItemDTO>().add(new ItemDTO())  , new ContactDTO("10", "c1", "053", "gmail@a.com"), 0.0, LocalDate.now(), "store", null), DaysDTO.WEDNESDAY);
        ArrayList<Pair<Integer, ProductDTO>> list = new ArrayList();
        list.add(new Pair<>(1, new ProductDTO("1", "milk")));
        ServiceFactory.getInstance().addFixedOrder(list, DaysDTO.valueOf(LocalDate.now().plusDays(1).getDayOfWeek().toString()));
        System.out.println(DaysDTO.valueOf(LocalDate.now().plusDays(1).getDayOfWeek().toString()));
        //List<OrderDTO> l=orderService.next_day();
        //System.out.println("the next day orders "+l+" size  "+l.size());
        //l.forEach(System.out::println);
        assertEquals( 1,orderService.next_day().size());
    }
}


