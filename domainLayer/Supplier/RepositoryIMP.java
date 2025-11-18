package domainLayer.Supplier;

import DataAccessLayer.Supplier.ConnectionManager;
import DataAccessLayer.Supplier.DataBase;
import DataAccessLayer.Supplier.*;
import ObjectDTO.Supplier.*;
import domainLayer.Supplier.Objects.Manufacturer;
import domainLayer.Supplier.Objects.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryIMP implements Repository {
    private static Repository instance = null;
    private final Connection connection;
    private boolean initialized = false;

    private RepositoryIMP() {
        this.connection = ConnectionManager.getConnection();
        initializeTables();
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new RepositoryIMP();
        }
        return instance;
    }

    // ------------------- Connection Methods -------------------
    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public void initializeTables() {
        try {
            DataBase.initializeTables(connection);
            this.initialized = true;
        } catch (Exception e) {
            System.err.println("Error initializing tables: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing DB connection: " + e.getMessage());
        }
    }

    // ------------------- Supplier Methods -------------------
    @Override
    public HashMap<String, SupplierDTO> getAllSuppliers() {
        try {
            return SupplierDAO.getInstance().getAll();
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e);
            return null;
        }
    }

    @Override
    public void deleteAllSuppliers(){
        try {
            SupplierDAO.getInstance().deleteAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SupplierDTO getSupplierById(String id) {
        if (id == null || id.isEmpty()) return null;
        try {
            return SupplierDAO.getInstance().get(id);
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e);
            return null;
        }
    }

    @Override
    public void insertSupplier(SupplierDTO dto) {
        if (dto == null || dto.getId() == null || dto.getId().isEmpty()) {
            System.err.println("insertSupplier: Invalid supplier DTO");
            return;
        }
        try {
            for (ContactDTO contact : dto.getContact()) {
                ContactDAO.getInstance().insert(contact, dto.getId());
               // System.out.println("contact inserted" + contact.getName());
            }
            //System.out.println("after contact for");
            SupplierDAO.getInstance().insert(dto);
        } catch (Exception e) {
            System.err.println("Error inserting supplier: " + e.getMessage());
        }
    }

    @Override
    public void updateSupplier(SupplierDTO dto) {
        if (dto == null || dto.getId() == null || dto.getId().isEmpty()) {
            System.err.println("updateSupplier: Invalid supplier DTO");
            return;
        }
        try {
            SupplierDAO.getInstance().update(dto);
        } catch (Exception e) {
            System.err.println("Error updating supplier: " + e.getMessage());
        }
    }

    @Override
    public void deleteSupplier(String id) {
        if (id == null || id.isEmpty()) {
            System.err.println("deleteSupplier: Invalid ID");
            return;
        }
        try {
            SupplierDAO.getInstance().delete(id);
        } catch (Exception e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
        }
    }

    // ------------------- Contact Methods -------------------
    @Override
    public HashMap<String, ContactDTO> getContactsForSupplier(String supplierId) {
        if (supplierId == null || supplierId.isEmpty()) return new HashMap<>();
        try {
            return ContactDAO.getInstance().getContactsBySupplier(supplierId);
        } catch (Exception e) {
            System.out.println("[ERROR] " + e);
            return null;
        }
    }

    @Override
    public void insertContact(ContactDTO dto, String supplierId) {
        if (dto == null || dto.getId() == null || dto.getId().isEmpty() || supplierId == null || supplierId.isEmpty()) {
            System.err.println("insertContact: Invalid contact or supplier ID");
            return;
        }
        try {
            ContactDAO.getInstance().insert(dto, supplierId);
        } catch (Exception e) {
            System.err.println("Error inserting contact: " + e.getMessage());
        }
    }

    @Override
    public void updateContact(ContactDTO dto) {
        if (dto == null || dto.getId() == null || dto.getId().isEmpty()) {
            System.err.println("updateContact: Invalid contact DTO");
            return;
        }
        try {
            ContactDAO.getInstance().update(dto);
        } catch (Exception e) {
            System.err.println("Error updating contact: " + e.getMessage());
        }
    }

    @Override
    public void deleteContact(String id) {
        if (id == null || id.isEmpty()) {
            System.err.println("deleteContact: Invalid ID");
            return;
        }
        try {
            ContactDAO.getInstance().delete(id);
        } catch (Exception e) {
            System.err.println("Error deleting contact: " + e.getMessage());
        }
    }

    // ------------------- Agreement Methods -------------------
    @Override
    public HashMap<String, AgreementDTO> getAllAgreements() {
        try {
            return AgreementDAO.getInstance().getAllAgreement();
        } catch (Exception e) {
            System.out.println("[ERROR] " + e);
            return null;
        }
    }

    @Override
    public AgreementDTO getAgreementById(String id) {
        if (id == null || id.isEmpty()) return null;
        try {
            return AgreementDAO.getInstance().get(id);
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e);
            return null;
        }
    }

    @Override
    public HashMap<String, AgreementDTO> getAgreementsBySupplierId(String supplierId) {
        if (supplierId == null || supplierId.isEmpty()) return new HashMap<>();
        return AgreementDAO.getInstance().getBySupplier(supplierId);
    }

    @Override
    public void insertAgreement(AgreementDTO dto) {
        if (dto == null || dto.agreementId() == null || dto.agreementId().isEmpty()) {
            System.err.println("insertAgreement: Invalid agreement DTO");
            return;
        }
        try {
            AgreementDAO.getInstance().insert(dto);
            for (SupItemDTO itemToInsert : dto.items().values()) {
                if (itemToInsert != null) {
                    ManufacturerDTO manuDTO = itemToInsert.getManufacturer();
                    if (!ProductDAO.getInstance().exists(itemToInsert.getproduct().getId()))
                        ProductDAO.getInstance().insert((itemToInsert.getproduct()));//insert the prouduct to product table if not exist

                    if (!ManufacturerDAO.getInstance().exists(manuDTO.manufacturer_id()))
                        ManufacturerDAO.getInstance().insert(manuDTO.manufacturer_id(), manuDTO.manufacturer_name());
                    for (DiscountDTO dis : itemToInsert.getQuantityDiscounts()) {
                        DiscountDAO.getInstance().insert(itemToInsert.getproduct().getId(), itemToInsert.getSupplierId(), dis);
                    }
                    //System.out.println("are thier manufactur in the data with id " + manuDTO.manufacturer_id() + " ?" + ManufacturerDAO.getInstance().exists(manuDTO.manufacturer_id()));
                    SupItemDAO.getInstance().insertSupItem(itemToInsert);    // insert the supitem to supitem table if not exist
                    //System.out.println("after add sup item");
                }
            }

        } catch (Exception e) {
            System.err.println("Error inserting agreement: " + e.getMessage());
        }
    }

    @Override
    public void updateAgreement(AgreementDTO dto) {
        if (dto == null || dto.agreementId() == null || dto.agreementId().isEmpty()) {
            System.err.println("updateAgreement: Invalid agreement DTO");
            return;
        }
        try {
//            AgreementDAO.getInstance().update(dto);
            AgreementDAO.getInstance().delete(dto.agreementId());
            AgreementDAO.getInstance().insert(dto);
            for(var x:dto.items().entrySet()){
                SupItemDAO.getInstance().update(x.getValue());
            }
        } catch (Exception e) {
            System.err.println("Error updating agreement: " + e.getMessage());
        }
    }

    @Override
    public void deleteAgreement(String id) {
        if (id == null || id.isEmpty()) {
            System.err.println("deleteAgreement: Invalid ID");
            return;
        }
        try {
            AgreementDAO.getInstance().delete(id);
        } catch (Exception e) {
            System.err.println("Error deleting agreement: " + e.getMessage());
        }
    }

    //*************SupItem*******************


    @Override
    public void insertSupItem(SupItemDTO dto) {
        if (dto == null || dto.getproduct() == null || dto.getSupplierId() == null) {
            System.err.println("insertSupItem: Invalid DTO");
            return;
        }
        try {
            SupItemDAO.getInstance().insertSupItem(dto);
        } catch (Exception e) {
            System.err.println("Error inserting SupItem: " + e.getMessage());
        }
    }

    @Override
    public void updateSupItem(SupItemDTO dto) {
        if (dto == null || dto.getproduct() == null || dto.getSupplierId() == null) {
            System.err.println("updateSupItem: Invalid DTO");
            return;
        }
        try {
            SupItemDAO.getInstance().update(dto);
        } catch (Exception e) {
            System.err.println("Error updating SupItem: " + e.getMessage());
        }
    }

    @Override
    public void deleteSupItem(String productId, String supplierId) {
        if (productId == null || productId.isEmpty() || supplierId == null || supplierId.isEmpty()) {
            System.err.println("deleteSupItem: Invalid IDs");
            return;
        }
        try {
            SupItemDAO.getInstance().delete(productId, supplierId);
        } catch (Exception e) {
            System.err.println("Error deleting SupItem: " + e.getMessage());
        }
    }


    // ------------------- SupItem Methods -------------------
    @Override
    public SupItemDTO remove_item_from_agreement(String agreementId, String productID) {
        try {

            return SupItemDAO.getInstance().remove_item_from_agreement(agreementId,productID);
        } catch (Exception e) {
            System.out.println("[Error] removing item from agreement: " + e.getMessage());
            return null;
        }
    }
//***************Manufacturer function*******************
    @Override
    public void add_manufacturer(Manufacturer manufacturerToAdd) {
        try{
            ManufacturerDAO.getInstance().insert(manufacturerToAdd.getManfacturer_id(),manufacturerToAdd.getName());
        }
        catch(Exception e){
            System.out.println("[Error] adding manufacturer: " + e.getMessage());
        }
    }

    @Override
    public ManufacturerDTO getManufacurer(String id) {
        try{
            ManufacturerDTO manu=ManufacturerDAO.getInstance().getManuDTOById(id);
            return manu;
        } catch (SQLException e) {
            System.out.println("[Error] getting manufacturer: " + e.getMessage());
            return null;
        }

    }

    @Override
    public boolean removeManufacurer(String id) {
        try{
            ManufacturerDAO.getInstance().delete(id);
            return true;
        } catch (Exception e) {
            System.out.println("[Error] removing manufacturer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean existManufacturer(String id) {
        try{

            return ManufacturerDAO.getInstance().exists(id);
        }
        catch (Exception e) {
            System.out.println("[Error] checking manufacturer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Map<String, String> getAllManufacturer() {
        try{
            return ManufacturerDAO.getInstance().getAll();
        }
        catch (Exception e) {
            System.out.println("[Error] getting all manufacturer: " + e.getMessage());
            return null;
        }
        //return null;
    }

    @Override
    public boolean removeAllAgreementItems(String agreementId) {
        if (agreementId == null || agreementId.isEmpty()) {
            System.err.println("removeAllAgreementItems: Invalid agreement ID");
        }
        try {
            SupItemDAO.getInstance().deleteAllAgreementItem(agreementId);

            return true;
        } catch (Exception e) {
            System.out.println("[Error] removing all agreement Items" + e.getMessage());
        }
        return false;
    }

    @Override
    public HashMap<String, SupItemDTO> getSupItemsByProductId(String productId) {
        if (productId == null || productId.isEmpty()) {
            System.err.println("getSupItemsByProductId: Invalid product ID");
            return new HashMap<>();
        }
        try {
            return SupItemDAO.getInstance().getByProductId(productId);
        } catch (Exception e) {
            System.err.println("Error retrieving SupItems by product ID: " + e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public HashMap<String, SupItemDTO> getSupItemsBySupplierId(String supplierId) {
        if (supplierId == null || supplierId.isEmpty()) {
            System.err.println("getSupItemsBySupplierId: Invalid supplier ID");
            return new HashMap<>();
        }
        try {
            return SupItemDAO.getInstance().getBySupplierId(supplierId);
        } catch (Exception e) {
            System.err.println("Error retrieving SupItems by supplier ID: " + e.getMessage());
            return new HashMap<>();
        }
    }


    @Override
    public void insertDiscount(String productId, String supplierId, int quantity, double discountPercentage) {
        try {
            DiscountDTO dto = new DiscountDTO(quantity, discountPercentage);
            DiscountDAO.getInstance().insert(productId, supplierId, dto);
        } catch (SQLException e) {
            System.err.println("Error inserting discount: " + e.getMessage());
        }
    }

    @Override
    public void updateDiscount(String productId, String supplierId, int quantity, double discountPercentage) {
        try {
            DiscountDTO dto = new DiscountDTO(quantity, discountPercentage);
            DiscountDAO.getInstance().update(productId, supplierId, dto);
        } catch (SQLException e) {
            System.err.println("Error updating discount: " + e.getMessage());
        }
    }

    @Override
    public void deleteDiscount(String productId, String supplierId, int quantity) {
        try {
            DiscountDAO.getInstance().delete(productId, supplierId, quantity);
        } catch (SQLException e) {
            System.err.println("Error deleting discount: " + e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, DiscountDTO> getDiscounts(String productId, String supplierId) {
        try {
            return DiscountDAO.getInstance().getDiscountsForProductAndSupplier(productId, supplierId);
        } catch (Exception e) {
            System.out.println("[ERORR] getDiscounts function ");
        }
        return null; //
    }

    // ------------------- Order operations -------------------
    @Override
    public void insertOrder(OrderDTO dto, boolean isFixed, DaysDTO day) {
        try {
            //System.out.println("[inserting the order ]"+dto);
            OrderDAO.getInstance().insert_order(dto, isFixed);
            for(ItemDTO x:dto.getItem_list())
            {
                Item_OrderDAO.getInstance().insertItem(x);
            }
            if(isFixed)
            {
                FixedOrderDAO.getInstance().insert(new FixedOrderDTO(dto,day),dto);
                //System.out.println("inserted to fixed order table");
            }
        } catch (SQLException e) {
            System.out.println("[Error] inserting order: " + e.getMessage());
           // System.out.println("this is your input  :"+dto.toString());

        }
    }

    @Override
    public OrderDTO getOrderById(String orderId) {
        try {
            return OrderDAO.getInstance().getOrderById(orderId);
        } catch (SQLException e) {
            System.out.println("[Error] getting order by id: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateOrder(OrderDTO dto, boolean isFixed) {
        try {
            return OrderDAO.getInstance().updateOrder(dto, isFixed);
        } catch (SQLException e) {
            System.out.println("[Error] updating order: " + e.getMessage());

            return false;
        }
    }

    @Override
    public boolean deleteOrder(String orderId) {
        try {
            return OrderDAO.getInstance().deleteOrder(orderId);
        } catch (SQLException e) {
            System.out.println("[Error] deleting order: " + e.getMessage());

            return false;
        }
    }

    // ------------------- Item_Order operations -------------------
    @Override
    public List<ItemDTO> getItemsByOrderId(String orderId) {
        try {
            return Item_OrderDAO.getInstance().getItems(orderId);
        } catch (SQLException e) {
            System.out.println("[Error] getting items by order id: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void insertOrderItem(ItemDTO item) {
        try {
            Item_OrderDAO.getInstance().insertItem(item);
        } catch (SQLException e) {
            System.out.println("[Error] inserting item: " + e.getMessage());
            ;
        }
    }

    @Override
    public void deleteItemsByOrderId(String orderId) {
        try {
            Item_OrderDAO.getInstance().deleteItemsByOrderId(orderId);
        } catch (SQLException e) {
            System.out.println("[Error] deleting items by order id: " + e.getMessage());
            ;
        }
    }

    // ------------------- FixedOrder operations -------------------
    @Override
    public void insertFixedOrder(FixedOrderDTO fixedDto, OrderDTO baseOrder) {
        try {
            FixedOrderDAO.getInstance().insert(fixedDto, baseOrder);
        } catch (SQLException e) {
            System.out.println("[Error] inserting fixed order: " + e.getMessage());
            ;
        }
    }

    @Override
    public FixedOrderDTO getFixedOrderById(String orderId) {
        try {
            return FixedOrderDAO.getInstance().getByOrderId(orderId);
        } catch (SQLException e) {
            System.out.println("[Error] getting fixed order by id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<FixedOrderDTO> getAllFixedOrders() {
        try {
            return FixedOrderDAO.getInstance().getAll();
        } catch (SQLException e) {
            System.out.println("[Error] getting fixed orders: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateFixedOrderArrival(String orderId, DaysDTO newDay) {
        try {
            return FixedOrderDAO.getInstance().updateArrivalDay(orderId, newDay);
        } catch (SQLException e) {
            System.out.println("[Error] updating fixed order arrival day: " + e.getMessage());

        }
        return false;
    }

    @Override
    public boolean deleteFixedOrder(String orderId) {
        try {
            return FixedOrderDAO.getInstance().delete(orderId);
        } catch (SQLException e) {
            System.out.println("[Error] deleting fixed order: " + e.getMessage());

            return false;
        }
    }

    //*********Product function

    @Override
    public ProductDTO get_product(String id) {
        return ProductDAO.getInstance().get(id);
    }

    @Override
    public Product get_product_nt_name(String name) {
        return ProductDAO.getInstance().getByName(name);
    }

    @Override
    public void remove_product(String id) {
        try{
            ProductDAO.getInstance().delete(id);
        } catch (SQLException e) {
            System.out.println("[Error] removing product: " + e.getMessage());
        }

    }

    @Override
    public Map<String, Product> get_all_product() {
        try{
            return ProductDAO.getInstance().getAll();
        } catch (Exception e) {
            System.out.println("[Error] getting all product: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean exist_product(String id) {
        return ProductDAO.getInstance().exists(id);
    }

    @Override
    public List<OrderDTO> get_all_order_for_today(DaysDTO daysDTO) {
        try{
            //get the fixed order for today
            List<OrderDTO> fixedOrder=FixedOrderDAO.getInstance().get_all_order_for_today(daysDTO);
            List<OrderDTO> orderForToday = new ArrayList<>(fixedOrder);

            //get the rest of order the supplier will deliver today
            HashMap<String,SupplierDTO> supplierForToday=SupplierDAO.getInstance().get_all_supplier_for_today(daysDTO);//the supplier that has fixed day and it today
            //System.out.println("supplier for today: "+supplierForToday.toString());
            List<OrderDTO> orderDTOList=OrderDAO.getInstance().getAll();//get all the order from database
           // System.out.println("order list: ");
           // orderDTOList.forEach(orderDTO -> System.out.println(orderDTO.toString()));
            for(OrderDTO order:orderDTOList) {
                if (order.getStatus().ordinal()==1) {//1 is purchesed
                //    System.out.println("order"+order+" status is purchesed");
                  //  if (Math(order.getDate().getDayOfWeek().getValue() -daysDTO.ordinal()) - 1)//the order is before one day at least
                   // {
                //        System.out.println("day of order is good ");
                        if (supplierForToday.containsKey(order.getSupplier_id())) {//is one of the suppliers that come today
                   //         System.out.println("that supplier is already in the today for this order"+order.getOrder_id());
                            if(!orderForToday.contains(order))
                                orderForToday.add(order);//add it to the list for today order
                   //         else
                   //             System.out.println(" oops that in the list "+order.getOrder_id());
                        }

                   // }
                  //  else{
                       // System.out.println("day of order is bad"+order.getDate().getDayOfWeek().getValue() +" big than "+ daysDTO.ordinal()+"-1");
                   // }
                }
            }
            //System.out.println("returning order for today number : "+ orderForToday.size() );
           // orderForToday.forEach(orderDTO -> System.out.println(orderDTO.toString()));
            return orderForToday;
        } catch (Exception e) {
            System.out.println("[Error] getting all order for today: " + e.getMessage());
            return null;
        }
        //return null;
    }

    @Override
    public List<OrderDTO> get_all_order() {

        try{
            return OrderDAO.getInstance().getAll();
        }
        catch (Exception e) {
            System.out.println("[Error] getting all order: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean insert_product(ProductDTO productDTO) {

        try{
            ProductDAO.getInstance().insert(productDTO);
            return true;
        }
        catch (Exception e) {
            System.out.println("[Error] inserting product: " + e.getMessage());
            return false;
        }
    }
    @Override
    public void clearAllAgreementes(){
        try {
            AgreementDAO.getInstance().deleteAllAgreements();
        }
        catch (Exception e){
            System.out.println("[Error] clearing all agreements: " + e.getMessage());
        }
    }

    @Override
    public void deleteAllManufacturers(){
        try{
            ManufacturerDAO.getInstance().deleteAllMans();
        } catch (Exception e) {
            System.out.println("[Error] deleting all manufacturers: " + e.getMessage());
        }
    }
}
