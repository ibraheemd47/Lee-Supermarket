package domainLayer.Supplier;

import ObjectDTO.Supplier.*;
import domainLayer.Supplier.Objects.Manufacturer;
import domainLayer.Supplier.Objects.Product;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Repository {

    // Connection-related
    Connection getConnection();
    void close();
    boolean isConnected();
    boolean isInitialized();
    void setInitialized(boolean initialized);
    void initializeTables();

    // Supplier operations
    HashMap<String, SupplierDTO> getAllSuppliers();
    SupplierDTO getSupplierById(String id);
    void insertSupplier(SupplierDTO dto);
    void updateSupplier(SupplierDTO dto);
    void deleteSupplier(String id);

    // Contact operations
    HashMap<String, ContactDTO> getContactsForSupplier(String supplierId);
    void insertContact(ContactDTO dto, String supplierId);
    void updateContact(ContactDTO dto);
    void deleteContact(String id);

    // Agreement operations
    HashMap<String, AgreementDTO> getAllAgreements();
    AgreementDTO getAgreementById(String id);
    HashMap<String, AgreementDTO> getAgreementsBySupplierId(String supplierId);
    void insertAgreement(AgreementDTO dto);
    void updateAgreement(AgreementDTO dto);
    void deleteAgreement(String id);

    // SupItems
    HashMap<String , SupItemDTO> getSupItemsByProductId(String productId);
    HashMap<String,SupItemDTO> getSupItemsBySupplierId(String supplierId);
    void insertSupItem(SupItemDTO dto);
    void updateSupItem(SupItemDTO dto);
    void deleteSupItem(String productId, String supplierId);
    boolean removeAllAgreementItems(String agreementId);

    // Discounts
    void insertDiscount(String productId, String supplierId, int quantity, double discountPercentage);
    void updateDiscount(String productId, String supplierId, int quantity, double discountPercentage);
    void deleteDiscount(String productId, String supplierId, int quantity);
    HashMap<Integer, DiscountDTO>   getDiscounts(String productId, String supplierId);
    // Order operations
    /**
     * Inserts a new order.
     * @param dto the OrderDTO to insert
     * @param isFixed whether this order is fixed
     */
    void insertOrder(OrderDTO dto, boolean isFixed, DaysDTO day)  ;

    /**
     * Retrieves an order by ID, including its items.
     * @param orderId the order ID
     * @return the OrderDTO or null if not found
     */
    OrderDTO getOrderById(String orderId)  ;

    /**
     * Updates an existing order.
     * @param dto the updated OrderDTO
     * @param isFixed whether this order is fixed
     * @return true if update succeeded
     */
    boolean updateOrder(OrderDTO dto, boolean isFixed)  ;

    /**
     * Deletes an order (and its items) by ID.
     * @param orderId the order ID
     * @return true if deletion succeeded
     */
    boolean deleteOrder(String orderId)  ;

    // Item_Order operations
    /**
     * Fetch all items for a given order.
     */
    List<ItemDTO> getItemsByOrderId(String orderId)  ;

    /**
     * Inserts an order item.
     */
    void insertOrderItem(ItemDTO item)  ;

    /**
     * Deletes all items for a given order.
     */
    void deleteItemsByOrderId(String orderId)  ;

    // Fixed order operations
    /**
     * Inserts a fixed order.
     */
    void insertFixedOrder(FixedOrderDTO fixedDto, OrderDTO baseOrder)  ;

    /**
     * Retrieves a fixed order by ID.
     */
    FixedOrderDTO getFixedOrderById(String orderId)  ;

    /**
     * Retrieves all fixed orders.
     */
    List<FixedOrderDTO> getAllFixedOrders()  ;

    /**
     * Updates the arrival day of a fixed order.
     */
    boolean updateFixedOrderArrival(String orderId, DaysDTO newDay)  ;

    /**
     * Deletes a fixed order by ID.
     */
    boolean deleteFixedOrder(String orderId)  ;

    SupItemDTO remove_item_from_agreement(String agreementId, String productID);

    void add_manufacturer(Manufacturer manufacturerToAdd);

    ManufacturerDTO getManufacurer(String id);

    boolean removeManufacurer(String id);

    boolean existManufacturer(String id);
    Map<String, String> getAllManufacturer();

    ProductDTO get_product(String id);

    Product get_product_nt_name(String name);

    void remove_product(String id);

    Map<String, Product> get_all_product();

    boolean exist_product(String id);

    void deleteAllSuppliers();

    List<OrderDTO> get_all_order_for_today(DaysDTO daysDTO);

    List<OrderDTO> get_all_order();
    boolean insert_product(ProductDTO productDTO);

    void clearAllAgreementes();

    void deleteAllManufacturers();
}
