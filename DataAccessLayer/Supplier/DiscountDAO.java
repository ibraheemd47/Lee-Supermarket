package DataAccessLayer.Supplier;

import ObjectDTO.Supplier.DiscountDTO;

import java.sql.*;
import java.util.HashMap;

public class DiscountDAO {
    private static DiscountDAO instance = null;
    private Connection connection;
    private HashMap<String, HashMap<String,HashMap<Integer,DiscountDTO>>> mapper;//<supplier id :String , Map<Proudct_id :String, Map< quntity:int , discount >> >

    private DiscountDAO() {
        this.connection = ConnectionManager.getConnection();
        if (this.connection == null) {
            throw new RuntimeException("Failed to establish database connection");
        }
        this.mapper = loadData();
    }

    public static DiscountDAO getInstance() {
        if (instance == null) {
            instance = new DiscountDAO();
        }
        return instance;
    }

    private HashMap<String, HashMap<String,HashMap<Integer,DiscountDTO>>>  loadData() {
        HashMap<String, HashMap<String,HashMap<Integer,DiscountDTO>>> discountMap = new HashMap<>();
        String sql = "SELECT * FROM Discounts";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String productId = rs.getString("product_id");
                String supplierId = rs.getString("supplier_id");
                int quantity = rs.getInt("quantity");
                double discount = rs.getDouble("discount_percentage");

                //String key = generateKey(productId, supplierId);
                DiscountDTO dto = new DiscountDTO(quantity, discount);

                discountMap
                        .computeIfAbsent(supplierId, k -> new HashMap<>())
                        .computeIfAbsent(productId, k -> new HashMap<>())
                        .put(quantity, dto);
// dto with quntity put in proudct map that have supplier
                //discountMap.computeIfAbsent(s, k -> new ArrayList<>()).add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discountMap;
    }

//    private String generateKey(String productId, String supplierId) {
//        return productId + "-" + supplierId;
//    }

    public void insert(String productId, String supplierId, DiscountDTO discountDTO) throws SQLException {

       // System.out.println(" i  want to insert = product id => "+ productId +" supplier id => "+ supplierId +" discount => "+ discountDTO.getDiscountPercentage() +" quantity => "+ discountDTO.getQuantity() );
        String sql = "INSERT OR IGNORE INTO Discounts (product_id, supplier_id, quantity, discount_percentage) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, supplierId);
            pstmt.setInt(3, discountDTO.getQuantity());
            pstmt.setDouble(4, discountDTO.getDiscountPercentage());

            pstmt.executeUpdate();

            //String key = generateKey(productId, supplierId);
            mapper.computeIfAbsent(supplierId, k -> new HashMap<>()).computeIfAbsent(productId,k->new HashMap<>()).put(discountDTO.getQuantity(),discountDTO);
        }
    }

    public HashMap<Integer,DiscountDTO> getDiscountsForProductAndSupplier(String productId, String supplierId) throws SQLException{
        //String key = generateKey(productId, supplierId);
        String sql = "SELECT * FROM Discounts WHERE product_id=? AND supplier_id=?";
        HashMap<Integer,DiscountDTO>  mapToReturn = new HashMap<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.setString(2, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int minQty = rs.getInt("quantity");
                double percent = rs.getDouble("discount_percentage");
                DiscountDTO disDTO=new DiscountDTO(minQty, percent);
                mapToReturn.put(minQty,disDTO);
                mapper.getOrDefault(supplierId, new HashMap<>()).getOrDefault(productId,new HashMap<>()).put(minQty,disDTO);
            }
        }

        return mapToReturn;
        //return mapper.getOrDefault(supplierId, new HashMap<>()).getOrDefault(productId,new HashMap<>());
    }

    public void update(String productId, String supplierId, DiscountDTO discountDTO) throws SQLException {
        String sql = "UPDATE Discounts SET discount_percentage = ? WHERE product_id = ? AND supplier_id = ? AND quantity = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, discountDTO.getDiscountPercentage());
            pstmt.setString(2, productId);
            pstmt.setString(3, supplierId);
            pstmt.setInt(4, discountDTO.getQuantity());
            pstmt.executeUpdate();

            //update mapper
            mapper.get(supplierId).get(productId).put(discountDTO.getQuantity(),discountDTO);

        }
    }

    public void delete(String productId, String supplierId, int quantity) throws SQLException {
        String sql = "DELETE FROM Discounts WHERE product_id = ? AND supplier_id = ? AND quantity = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, supplierId);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();

            //String key = generateKey(productId, supplierId);
            //ArrayList<DiscountDTO> discounts = mapper.get(key);
            if (mapper != null && mapper.containsKey(supplierId)&& mapper.get(supplierId)!=null && mapper.get(supplierId).containsKey(productId)&& mapper.get(supplierId).get(productId)!=null ) {
                mapper.get(supplierId).get(productId).remove(quantity);

            }
        }
    }

    public HashMap<String,HashMap<String,HashMap<Integer,DiscountDTO>>> getAll() {
        HashMap<String,HashMap<String,HashMap<Integer,DiscountDTO>>> map = new HashMap<>();
        String sql = "SELECT * FROM Discounts";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String productId = rs.getString("product_id");
                String supplierId = rs.getString("supplier_id");
                int quantity = rs.getInt("quantity");
                double discount = rs.getDouble("discount_percentage");

                DiscountDTO dto = new DiscountDTO(quantity, discount);
                map
                        .computeIfAbsent(supplierId, k -> new HashMap<>())
                        .computeIfAbsent(productId, k -> new HashMap<>())
                        .put(quantity, dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mapper=map;
        return map;
    }

}
