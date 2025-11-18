package domainLayer.Supplier.Objects;

import ObjectDTO.Supplier.ItemDTO;
import ObjectDTO.Supplier.OrderDTO;
import ObjectDTO.Supplier.StatusDTO;
import ObjectDTO.Supplier.SupItemDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Order {
    Contact contact;
    //  private List<SupItem> products;
    Map<String, PurchaseItem> purchaseItemMap;//<serial number,PurchesItem>

    public void setStatus(STATUS status) {
        this.status = status;
    }

    STATUS status;
    private String id;
    private String supplierId;
    private LocalDate deliveryDate;
    private double price;
    private String address;
    private String supplier_name;

    public Order(String orderId, String supplierId,String supplier_name, Map<SupItem, Integer> itemIntegerMap, LocalDate orderDate, Contact contact, String address) {
        this.supplier_name = supplier_name;
        this.address = address;
        this.id = orderId;
        this.supplierId = supplierId;
        this.purchaseItemMap = get_purchase_item(itemIntegerMap);
        this.status = STATUS.PURCHASED;
        this.deliveryDate = orderDate;
        this.contact = contact;
        this.price = getTotalPrice();
    }

    public Order(OrderDTO dto) {
        if(dto==null)
            return ;
        this.id = String.valueOf(dto.getOrder_id());
        this.supplierId = dto.getSupplier_id();
        this.supplier_name = dto.getSupplier_name();
        this.address = dto.getAddress();
        this.contact = new Contact(dto.getContact()); // Assuming Contact has a constructor from ContactDTO
        this.deliveryDate = dto.getDate();
        this.status = STATUS.PURCHASED; // Or retrieve from DTO if you add status there
        this.purchaseItemMap = new HashMap<>();
        for (ItemDTO itemDTO : dto.getItem_list()) {
            SupItem supItem = new SupItem(itemDTO.supItem()); // Custom method to convert or fetch SupItem
            int quantity = itemDTO.quantity();
            double lastPrice = itemDTO.price();

            double discount = 1 - (lastPrice / supItem.getPrice());
            PurchaseItem pi = new PurchaseItem(supItem, quantity, lastPrice, discount, supItem.getPrice());
            purchaseItemMap.put(supItem.getSupplierCatNum(), pi);
        }

        this.price = getTotalPrice();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * get the total price of the order before normal
     *
     * @return total price before the discount
     */
    private double get_Total_Price_before_discount() {
        return purchaseItemMap.values().stream().mapToDouble(item -> item.normalPrice).sum();
    }

    /**
     * get the total price of the order
     *
     * @return total price after the discount
     */
    private double getTotalPrice() {
        return purchaseItemMap.values().stream().mapToDouble(item -> item.getLastPrice() * item.getQuantity()).sum();
    }

    /**
     * convert a map from supitem to purchase item
     *
     * @param products
     * @return
     */
    private Map<String, PurchaseItem> get_purchase_item(Map<SupItem, Integer> products) {
        Map<String, PurchaseItem> purchaseItemMap = new HashMap<String, PurchaseItem>();
        for (Map.Entry<SupItem, Integer> entry : products.entrySet()) {
            PurchaseItem purchaseItem_to_insert = get_price(entry.getKey(), entry.getValue());//get the purchase object from subitem
            String purchaseItem_id = entry.getKey().getSupplierCatNum();
            purchaseItemMap.put(purchaseItem_id, purchaseItem_to_insert);//insert to the map
        }
        return purchaseItemMap;
    }


    /**
     * get an object of subitem and convert to purchese item
     *
     * @param supItem  subitem object that ordered
     * @param quantity the quantity of the sup item
     * @return purchase item
     */
    private PurchaseItem get_price(SupItem supItem, int quantity) {
        if (supItem == null) {
            throw new NullPointerException("supItem is null");
        }
        if (quantity <= 0) {
            throw new NullPointerException("supItem is null");
        }
        PurchaseItem purchaseItem_to_return;
        /*
        List<Agreement> agreements=Factory.getInstance().getAgreementManager().getAgreementsBySupplier(supplierId);//getting the agreement of supplier
        for(Agreement agreement : agreements){
            if(agreement.)
        }
        */
        double price = supItem.getPrice();

        double price_after_discount = supItem.getPriceAfterDiscount(quantity);
        //double last_price = (1 - discount_percentage) * supItem.getPrice();
        double discount_percentage = (price_after_discount / price)+1;
        purchaseItem_to_return = new PurchaseItem(supItem, quantity,price_after_discount,discount_percentage, price);

        return purchaseItem_to_return;

    }

    public String getId() {
        return id;
    }

    public String getSupplierId() {
        return supplierId;
    }


    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", supplierId='" + supplierId + '\'' + ", products=" + purchaseItemMap + ", deliveryDate=" + deliveryDate + ", price=" + price + '}';
    }

    public STATUS getStatus() {
        return status;
    }

    public void cancel() {
        status = STATUS.CANCELLED;
    }

    public void delivered() {
        status = STATUS.DELIVERED;
    }

    public OrderDTO toDTO() {
        // Convert PurchaseItem map to a list of ItemDTOs
        List<ItemDTO> items = purchaseItemMap.values().stream().map(pi -> {
            SupItem sup = pi.getSupItem();
            // Create SupItemDTO from domain SupItem
            SupItemDTO supDto = sup.toDTO();
            return new ItemDTO(supDto, (this.id), pi.getQuantity(), pi.getLastPrice());
        }).collect(Collectors.toList());

        // Calculate days until arrival
        int tillArrival = (int) ChronoUnit.DAYS.between(LocalDate.now(), this.deliveryDate);

        // Build the OrderDTO
        return new OrderDTO((this.id), supplier_name, this.supplierId, items, contact.ConvertToDTO(), this.price, deliveryDate, address, StatusDTO.valueOf(status.toString()));
    }
}