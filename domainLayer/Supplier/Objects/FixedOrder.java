package domainLayer.Supplier.Objects;

import ObjectDTO.Supplier.OrderDTO;

import java.time.LocalDate;
import java.util.Map;

public class FixedOrder extends Order {


    public FixedOrder(String orderId, String supplier_name,String supplierId, Map<SupItem, Integer> itemIntegerMap, LocalDate orderDate, Contact contact, String address) {
        super(orderId,  supplier_name,supplierId, itemIntegerMap, orderDate, contact, address);

    }

    public FixedOrder(OrderDTO order) {
        super(order);
    }
}
