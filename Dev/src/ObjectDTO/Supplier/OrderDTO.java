package ObjectDTO.Supplier;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    private String order_id;
    private String supplier_name;
    private String supplier_id;
    private ContactDTO contact;
    private List<ItemDTO> item_list;
    private double order_price;
    private int contract_id;
    private LocalDate date;
    private StatusDTO status;//StatusDTO?
    private String address;

    public OrderDTO(String order_id, String supplier_name, String supplier_id,
                    List<ItemDTO> item_list, ContactDTO contact, double order_price, LocalDate date, String address ,StatusDTO status) {
        this.status = status;
        this.address = address;
        this.order_id = order_id;
        this.supplier_name = supplier_name;
        this.supplier_id = supplier_id;
        this.item_list = item_list;
        this.contact = contact;
        this.order_price = order_price;
        this.date = date;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public List<ItemDTO> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemDTO> item_list) {
        this.item_list = item_list;
    }

    public double getOrder_price() {
        return order_price;
    }

    public int getContract_id() {
        return contract_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void delivered() {
        status = StatusDTO.DELIVERED;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void cancel() {
        status = StatusDTO.CANCELLED;
    }
    @Override
    public String toString() {
        return "OrderDTO{" +
                "order_id='" + order_id + '\'' +
                ", supplier_name='" + supplier_name + '\'' +
                ", supplier_id='" + supplier_id + '\'' +
                ", contact=" + (contact != null ? contact.toString() : "null") +
                ", item_list=" + (item_list != null ? item_list.toString() : "[]") +
                ", order_price=" + order_price +
                ", contract_id=" + contract_id +
                ", date=" + date +
                ", status=" + status +
                ", address='" + address + '\'' +
                '}';
    }

}