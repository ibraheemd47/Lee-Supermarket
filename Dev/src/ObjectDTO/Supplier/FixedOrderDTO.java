package ObjectDTO.Supplier;


import java.time.LocalDate;
import java.util.List;

public class FixedOrderDTO extends OrderDTO {
    private DaysDTO arrival_day;
    private String order_id;
    public FixedOrderDTO(String order_id, String supplier_name, String supplier_id, List<ItemDTO> item_list, ContactDTO contact , double order_price, LocalDate order_day, DaysDTO arrival_day , String address, StatusDTO status) {
        super(order_id, supplier_name, supplier_id, item_list, contact, order_price, order_day, address, status);
        this.order_id = order_id;
        this.arrival_day = arrival_day;
    }
    public String getOrder_id() {
        return order_id;
    }
    public DaysDTO getArrival_day() {
        return arrival_day;
    }
    public void setArrival_day(DaysDTO arrival_day) {
        this.arrival_day = arrival_day;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public FixedOrderDTO(OrderDTO orderDTO, DaysDTO arrival_day) {
        super(
                orderDTO.getOrder_id(),
                orderDTO.getSupplier_name(),
                orderDTO.getSupplier_id(),
                orderDTO.getItem_list(),
                orderDTO.getContact(),
                orderDTO.getOrder_price(),
                orderDTO.getDate(),
                orderDTO.getAddress(),
                orderDTO.getStatus()
        );
        this.order_id = orderDTO.getOrder_id();
        this.arrival_day = arrival_day;
    }
}
