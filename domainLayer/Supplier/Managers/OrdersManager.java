package domainLayer.Supplier.Managers;


import DataAccessLayer.Supplier.FixedOrderDAO;
import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.FixedOrderDTO;
import ObjectDTO.Supplier.ItemDTO;
import ObjectDTO.Supplier.OrderDTO;
import domainLayer.Supplier.Objects.*;
import domainLayer.Supplier.Repository;
import domainLayer.Supplier.RepositoryIMP;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersManager {
    Days today;
    private final Map<String, Order> orders;
    private final Map<String, FixedOrder> fixedOrders;
    private int counter = 0;
    //data base
    private Repository dataBase = RepositoryIMP.getInstance();

    public OrdersManager() {
        this.orders = new HashMap<>();
        this.fixedOrders = new HashMap<>();
        var day = LocalDate.now().getDayOfWeek().toString();
        today = Days.valueOf(day);
    }
    public List<OrderDTO> next_day(){
        int nextOrdinal = (today.ordinal() + 1) % Days.values().length;
        today = Days.values()[nextOrdinal];
        List<OrderDTO> today_order=get_all_order_for_today(DaysDTO.valueOf(today.name()));
        today=Days.values()[nextOrdinal-1];
        if (today_order==null || today_order.isEmpty())
            return null;
        return today_order;
//        for(OrderDTO order:today_order){
//            //todo send it
//            order.getItem_list().forEach(item->{
//                //InventoryManager.getInstance().Recive(item);
//            });
//            System.out.println("sending the order to the inventory manager: "+order+" "+order.getItem_list().size()+" items" );
//            //order.delivered();//update the oder status
//            update_order_satatus(order,STATUS.DELIVERED,fixedOrders.containsKey(order.getOrder_id()));
//        }
    }
    private List<OrderDTO> get_all_order_for_today(DaysDTO daysDTO) {
        List<OrderDTO> toReturn= dataBase.get_all_order_for_today(daysDTO);
        for (OrderDTO orderDTO : toReturn) {
            if(orderDTO instanceof FixedOrderDTO){
                fixedOrders.put(orderDTO.getOrder_id(),new FixedOrder(orderDTO));
                //    orders.put(orderDTO.getOrder_id(),new Order(orderDTO));
            }

            orders.put(orderDTO.getOrder_id(),new Order(orderDTO));
        }
        return toReturn;
    }

    public void addOrder(Order order) {
        if (order == null || order.getId() == null) {
            throw new IllegalArgumentException("Order or Order ID cannot be null");
        }
       // System.out.println("[manager]"+order);
        orders.put(order.getId(), order);
        dataBase.insertOrder(order.toDTO(), false, null);
    }

//    public void addOrder(String orderId, String supplierId, Map<SupItemDTO, Integer> itemIntegerMapdto, LocalDate orderDate, Contact contact) {
//        if(orderId == null || orderId.isEmpty()) {
//            throw new IllegalArgumentException("Order ID cannot be null or empty");
//        }
//        Order order = orders.get(orderId);
//        if (order != null) {
//            throw new IllegalArgumentException("Order with this id is found");
//        }
//        //double price = get_price(itemIntegerMap);
//        Map<SupItem, Integer>  itemIntegerMap =new HashMap<>();
//        for(SupItemDTO supItemDTO:itemIntegerMapdto.keySet()){
//            SupItem supItem=new SupItem(supItemDTO);
//            itemIntegerMap.put(supItem,itemIntegerMapdto.get(supItemDTO));
//        }//fixme : problem with insert the maufacurer check that manfucturer is null
//
//        order=new Order(orderId,supplierId,itemIntegerMap,orderDate,contact);
//        orders.put(order.getId(), order);
//        dataBase.insertOrder(order.toDTO(),false);
//        //
//    }

    // needed order!
    public String addOrder(OrderDTO order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null or empty");
        }
        List<ItemDTO> lst = order.getItem_list();
        Map<SupItem, Integer> itemIntegerMap = new HashMap<>();
        for (ItemDTO item : lst) {
            itemIntegerMap.put(new SupItem(item.supItem()), item.quantity());
        }
        Order newOrder = new Order(order);
       // System.out.println("the new order to add :\n"+newOrder);
        addOrder(newOrder);
        return "added order with id " + order.getOrder_id();
    }


    public String add_fixed_order(OrderDTO order, DaysDTO arriv_day) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null or empty");
        }
        orders.put(order.getOrder_id(), new Order(order));
        fixedOrders.put(order.getOrder_id(), new FixedOrder(order));

        dataBase.insertOrder(order, true,arriv_day);
        return "added fixed order with id "+order.getOrder_id();
    }


    public Map<String, Order> getAllOrders() {
        return new HashMap<>(orders); // return a copy to avoid external changes
    }

    public Map<String, OrderDTO> getAllOrdersDTO() {
        Map<String, OrderDTO> ordersDTO = new HashMap<>();
        for (Order order : orders.values()) {
            ordersDTO.put(order.getId(), order.toDTO());
        }
        for (FixedOrder fixedOrder : fixedOrders.values()) {
            ordersDTO.put(fixedOrder.getId(), fixedOrder.toDTO());
        }
        return ordersDTO;
    }


    public String removeOrder(String id) {
        Order toRemove = orders.remove(id);
        dataBase.deleteOrder(id);
        if (toRemove != null) {
             return  "order :" + toRemove + " removed successfully!";
        } else {
            return "The remove function failed!.";

        }
        //return orders.remove(id) != null;
    }

    public Order getOrderById(String id) {
        return orders.get(id);
    }

    public OrderDTO getOrderDTOById(String id) {
        Order order = orders.get(id);
        return order.toDTO();
    }

    public boolean containsOrder(String id) {
        return orders.containsKey(id);
    }

    public void clearAllOrders() {
        orders.clear();
    }

    /**
     * find the order by the supplier id
     *
     * @param supId supplier id
     * @return the orders that do with the supplier with id = supid
     */
    public List<Order> getOrderBySupId(String supId) {
        List<Order> orders_to_return = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getSupplierId().equals(supId)) {
                orders_to_return.add(order);
            }
        }
        if (orders_to_return.isEmpty()) {
            return null;
        }
        return orders_to_return;
    }

    public List<OrderDTO> getOrderDTOBySupId(String supId) {
        List<OrderDTO> orders_to_return = new ArrayList<>();
        if (getOrderBySupId(supId) != null) {
            getOrderBySupId(supId).forEach(order -> {
                orders_to_return.add(order.toDTO());
            });
        }
        return orders_to_return;
    }


    public String cancel_order(String orderId) {
        boolean is_fixed=dataBase.getFixedOrderById(orderId)!=null;

        //update the maps
        if (orders.get(orderId) != null) {
            orders.get(orderId).cancel();
            if (fixedOrders.containsKey(orderId)) {
                fixedOrders.get(orderId).cancel();

            }
        }

        //update the data base
        if(!dataBase.updateOrder(orders.get(orderId).toDTO(), is_fixed))
            return "Order with id: " + orderId + " does not exist in  data base !";
        return "Order with id: " + orderId + " canceled successfully!";



//        if (fixedOrders.get(orderId) != null) {//is fixed order
//                dataBase.updateOrder(orders.get(orderId).toDTO(), true);
//                return  ("Order with id: " + orderId + " canceled successfully!");
//            } else if (fixedOrders.containsKey(orderId) && fixedOrders.get(orderId) != null) {
//                fixedOrders.get(orderId).cancel();
//                dataBase.updateOrder(fixedOrders.get(orderId).toDTO(), true);
//            } else {
//                 return ("Order with id: " + orderId + " does not exist in fixed orders!");
//            }
//        } else {
//            return ("Order with id: " + orderId + " does not exist!");
//        }
//        return "";
    }

    public String get_day() {
        return today.name();
    }

    public String delivered(String orderId) {
        boolean isFixedOrder = dataBase.getFixedOrderById(orderId) != null;
        if (orders.get(orderId) != null) {
            orders.get(orderId).delivered();
            dataBase.updateOrder(orders.get(orderId).toDTO(), isFixedOrder);
        } else {
            OrderDTO order = dataBase.getOrderById(orderId);
            orders.put(order.getOrder_id(), new Order(order));
            orders.get(orderId).delivered();
            dataBase.updateOrder(orders.get(orderId).toDTO(), isFixedOrder);
        }
        if (isFixedOrder)
            if (fixedOrders.containsKey(orderId)) {
                fixedOrders.get(orderId).delivered();
            }
        return "Order with id: " + orderId + " delivered successfully!";
    }
}

//    /**
//     * @return
//     */
//    public AgreementDTO get_best_deal(List<Pair<Integer, ProductDTO>> ls) {
//        double best_price = 0;
////        String supplierId;
//        Agreement best_agreement=null;
//        List<Agreement> agreements = Factory.getInstance().getAgreementManager().getAllAgreements();
//
//
//        //iterate the agreement and get the best deal according to it
//
//        for (Agreement agreement : agreements) {
//
//            double newPrice=0;
//
//            for (Pair p : ls) {
//                if(agreement.containItem(p.second))
//                {
//                    double add=agreement.getItemPrice(p.first,p.second);
//                    newPrice+=add;
//                }
//                else
//                    break;
//            }
//            if(newPrice<best_price){
//                best_price=newPrice;
//                best_agreement=agreement;
//
//            }
//        }
//        if(best_agreement==null){
//            return null;
//        }
//        return best_agreement.toDTO();
//        //we have now the best agreement to do the deal
////        return do_order_with_sup(best_agreement,ls);
//    }
//
//    private OrderDTO do_order_with_sup(Agreement bestAgreement, List<Pair<Integer, ProductDTO>> ls) {
////        Supplier supplier = Factory.getInstance().getSupplierManager().getSupplierById(bestAgreement.getSupplierId());
////        OrderDTO order = new OrderDTO("make it autmatic",supplier.getName(),supplier.getId(),listItem,supplier.getContact().get(0),
////                LocalDate.now(),)
////    }
//}
//
//class Pair<A, B> {
//    public final A first;
//    public final B second;
//
//    public Pair(A first, B second) {
//        this.first = first;
//        this.second = second;
//    }
//}
