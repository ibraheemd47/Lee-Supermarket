package ServiceLayer.Supplier;

import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.FixedOrderDTO;
import ObjectDTO.Supplier.OrderDTO;
import ServiceLayer.Inventory.Response;
import domainLayer.Supplier.Factory;
import domainLayer.Supplier.Managers.OrdersManager;

import java.util.List;
import java.util.Map;

public class OrderService {
    private OrdersManager manager;
    private static OrderService instance = null;
    private Response response;
    private OrderService() {
        manager = Factory.getInstance().getOrdersManager();
    }
    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public String add_fixed_order(FixedOrderDTO order, DaysDTO arriv_day) {
        String result = "";
        try {
            result =manager.add_fixed_order(order,arriv_day);
            response = Response.success(result);
        }catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String add_needed_order(OrderDTO order) {

       // System.out.println("Order ID: " + order.getOrder_id());
        String result = "";
        try {
            result =manager.addOrder(order);
            response = Response.success(result);
        }catch (Exception e) {
            System.out.println("[ERROR] add needed order failed: ]+" + e.getMessage());
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }

    public Response get_All_orders() {
        Map<String, OrderDTO> result = null;
        try {
            result = manager.getAllOrdersDTO();
            response = Response.success(result);
        }catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response;

    }

    public Response findOrderById(String id) {
        OrderDTO result = null;
        try{
            result= manager.getOrderDTOById(id);
            response = Response.success(result);
        }
        catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response;
    }

    public Response findOrderBySupID(String supId) {
        List<OrderDTO> result = null;
        try {
            result = manager.getOrderDTOBySupId(supId);
            response = Response.success(result);
        }
        catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response;
    }

    public String remove_order(String orderId) {
        String result = "";
        try {
            result =manager.removeOrder(orderId);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String cancel_order(String orderId) {
        String result = "";
        try {
            result =manager.cancel_order(orderId);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }

    public boolean is_uniq_id(String autoMaticOrderId) {
        return !manager.containsOrder(autoMaticOrderId);
    }

    public List<OrderDTO> next_day(){
        List<OrderDTO> result = null;
        try {
            return manager.next_day();
           // result = manager.next_day();
         //   response = Response.success(result);
        }
        catch (Exception e) {
            System.out.println("[ERROR] next_day failed: ]+" + e.getMessage());
            return null;
          //  response = Response.failure(e.getMessage());
        }
       // return response;
    }

    public String get_day() {
        return manager.get_day();
    }

    public String delivered(String orderId) {
        String result = "";
        try {
            result = manager.delivered(orderId);
            response = Response.success(result);
        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();

    }
}
