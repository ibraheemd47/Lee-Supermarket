package DataAccessLayer.Supplier;

import ObjectDTO.Supplier.ItemDTO;
import ObjectDTO.Supplier.SupItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Item_OrderDAO {
    private static Item_OrderDAO instance = null;
    private final Connection connection;
    private final HashMap<String , HashMap<String ,ItemDTO>> mapperByOrderId;//<order_id:String , Map<Item_Id : String , itemDTO>>
//    private final HashMap<String , ItemDTO> mapperByItemId;
    private Item_OrderDAO() {
        this.connection = ConnectionManager.getConnection();
        mapperByOrderId=new HashMap<>();
        //mapperByItemId=new HashMap<>();

    }
    public static Item_OrderDAO getInstance() {
        if(instance==null)
            instance=new Item_OrderDAO();
        return instance;
    }
    /**
     * Retrieve all supply items associated with a given order ID.
     */
    public List<ItemDTO> getItems(String orderId) throws SQLException {

        // try to get from the mapper if exist
        HashMap<String ,ItemDTO> itemsHash=mapperByOrderId.get(orderId);
        if(itemsHash!=null){
            return new ArrayList<>(itemsHash.values());
        }

        //else get it from the data base
        List<ItemDTO> items = new ArrayList<>();
        String sql = "SELECT product_id, supplier_id, agreement_id, quantity, price FROM Item_Order WHERE order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                //SupItemDTO item = new SupItemDTO(
                  String productId=      rs.getString("product_id");
                  String supId=       rs.getString("supplier_id");
                  String agreementId=      rs.getString("agreement_id");
                  int quntity=      rs.getInt("quantity");
                  double price=      rs.getDouble("price");
                //);
                SupItemDTO supItem=SupItemDAO.getInstance().getBySupItemId(productId,supId);
                ItemDTO itemDTO = new ItemDTO(supItem, orderId, quntity, price);
                items.add(itemDTO);



            }

            //insert to the mapper
            HashMap<String ,ItemDTO> ma=new HashMap<>();
            for(ItemDTO item : items){
     //           mapperByItemId.put(item.supItem().getproduct().getId(),item);
                ma.put(item.supItem().getproduct().getId(),item);
            }
            mapperByOrderId.put(orderId,ma);

        } catch (SQLException e) {
            System.err.println("Error fetching items for order ID " + orderId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Inserts an item into the Item_Order table.
     */
    public void insertItem(ItemDTO item) throws SQLException {
        String agreementId=SupItemDAO.getInstance().getBySupplierPro(item.supItem());
        String sql = "INSERT INTO Item_Order (order_id, product_id, supplier_id, agreement_id, quantity, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.orderId());
            stmt.setString(2, item.supItem().getproduct().getId());
            stmt.setString(3, item.supItem().getSupplierId());
            stmt.setString(4, agreementId);
            stmt.setInt(5, item.quantity());
            stmt.setDouble(6, item.price());
            stmt.executeUpdate();
        }
    }

    /**
     * Delete all items associated with an order ID.
     */
    public void deleteItemsByOrderId(String orderId) throws SQLException {

        String sql = "DELETE FROM Item_Order WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            stmt.executeUpdate();

        }
        mapperByOrderId.remove(Integer.parseInt(orderId));
    }

}
