package DataAccessLayer.Supplier;

import ObjectDTO.Supplier.DiscountDTO;
import ObjectDTO.Supplier.ManufacturerDTO;
import ObjectDTO.Supplier.ProductDTO;
import ObjectDTO.Supplier.SupItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SupItemDAO {
    private static SupItemDAO instance = null;
    private final Connection connection;

    private SupItemDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public static SupItemDAO getInstance() {
        if (instance == null)
            instance = new SupItemDAO();
        return instance;
    }

    public void insertSupItem(SupItemDTO item) throws SQLException {
        String sql = "INSERT INTO SupItems (productId,productName,  supplierId,price, supplierCatNum,agreement_id,manufacturer) VALUES (?,?, ?, ?, ?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getproduct().getId());
            stmt.setString(2, item.getproduct().getName());
            //stmt.setString(2, item.getProductName());
            stmt.setString(3, item.getSupplierId());
            stmt.setDouble(4, item.getPrice());
            stmt.setString(5, item.getSupplierCatNum());
            stmt.setString(6,item.getAgreementId());
            stmt.setString(7,item.getManufacturer().manufacturer_id());
            //System.out.printlnln("i am here ");
            stmt.executeUpdate();
            //System.out.printlnln("i am here 2");
        }
        try{
            for(DiscountDTO d:item.getQuantityDiscounts())
                DiscountDAO.getInstance().insert(item.getproduct().getId(),item.getSupplierId(),d);
            //insertDiscounts(item);
        } catch (SQLException e) {
            System.out.println("[ERORR] from insert discount 47");
        }
        //System.out.printlnln("i am here 3");
    }

    public void update(SupItemDTO item) throws SQLException {
        if(!exist(item)){
            insertSupItem(item);
        }
        String sql = "UPDATE SupItems SET  price=?,  productName=?,supplierCatNum=? ,agreement_id=?,manufacturer=? WHERE productId=? AND supplierId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, item.getPrice());
            stmt.setString(2, item.getproduct().getName());
            stmt.setString(3, item.getSupplierCatNum());
            stmt.setString(4,item.getAgreementId());
            stmt.setString(5,item.getManufacturer().manufacturer_id());
            stmt.setString(6, item.getproduct().getId());
            stmt.setString(7, item.getSupplierId());
            stmt.executeUpdate();
        }
        deleteDiscounts(item.getproduct().getId(), item.getSupplierId());
        insertDiscounts(item);
    }

    private boolean exist(SupItemDTO item) {
        String sql = "SELECT * FROM SupItems WHERE productId=? AND supplierId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getproduct().getId());
            stmt.setString(2, item.getSupplierId());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void delete(String productId, String supplierId) throws SQLException {
        deleteDiscounts(productId, supplierId);
        String sql = "DELETE FROM SupItems WHERE productId=? AND supplierId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.setString(2, supplierId);
            stmt.executeUpdate();
        }
    }

    public HashMap<String, SupItemDTO> getByProductId(String productId) throws SQLException {
        String sql = "SELECT * FROM SupItems WHERE productId=?";
        return fetchSupItems(sql, productId);
    }

    public SupItemDTO getBySupItemId(String productId, String supplierId) throws SQLException {
        String sql = "SELECT * FROM SupItems WHERE productId=? and supplierId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.setString(2, supplierId);
            stmt.executeUpdate();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String productName = rs.getString("productName");
                String manufacturerId = rs.getString("manufacturer");
                double price = rs.getDouble("price");
                String supplierCatNum = rs.getString("supplierCatNum");
                String agreementId=rs.getString("agreement_id");
                ManufacturerDTO manu = ManufacturerDAO.getInstance().getManuDTOById(manufacturerId);
                List<DiscountDTO> discounts = getDiscounts(productId, supplierId);
                return new SupItemDTO(new ProductDTO(productId, productName), manu, supplierId,agreementId, price, supplierCatNum, discounts);
            }
        }
        return null;
    }

    public HashMap<String, SupItemDTO> getBySupplierId(String supplierId) throws SQLException {
        String sql = "SELECT * FROM SupItems WHERE supplierId=?";
        return fetchSupItems(sql, supplierId);
    }

    private HashMap<String, SupItemDTO> fetchSupItems(String sql, String id) throws SQLException {
        HashMap<String, SupItemDTO> items = new HashMap<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String productId = rs.getString("productId");
                String productName = rs.getString("productName");
                String manufacturerId = rs.getString("manufacturer");
                String supplierId = rs.getString("supplierId");
                String agreementId=rs.getString("agreement_id");
                double price = rs.getDouble("price");
                String supplierCatNum = rs.getString("supplierCatNum");
                ManufacturerDTO manu = ManufacturerDAO.getInstance().getManuDTOById(manufacturerId);
                List<DiscountDTO> discounts = getDiscounts(productId, supplierId);

                SupItemDTO dto = new SupItemDTO(new ProductDTO(productId, productName), manu, supplierId,agreementId, price, supplierCatNum, discounts);
                items.put(productId, dto);
            }
        }
        return items;
    }

    private void insertDiscounts(SupItemDTO item) throws SQLException {
        if (item.getQuantityDiscounts() == null) return;
        String sql = "INSERT INTO Discounts (product_Id, supplier_Id, quantity, discount_percentage) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (DiscountDTO d : item.getQuantityDiscounts()) {
                stmt.setString(1, item.getproduct().getId());//product id
             //   stmt.setString(2, item.getproduct().getName());

                stmt.setString(2, item.getSupplierId());//supplier id
                stmt.setInt(3, d.getQuantity());// quntity
                stmt.setDouble(4, d.getDiscountPercentage());//percentage
                stmt.executeUpdate();
            }
            stmt.executeBatch();
        }
    }

    private void deleteDiscounts(String productId, String supplierId) throws SQLException {
        String sql = "DELETE FROM Discounts WHERE product_Id=? AND supplier_Id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.setString(2, supplierId);
            stmt.executeUpdate();
        }
    }

    //todo: move that
    private List<DiscountDTO> getDiscounts(String productId, String supplierId) throws SQLException {
        String sql = "SELECT * FROM Discounts WHERE product_id=? AND supplier_id=?";
        List<DiscountDTO> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.setString(2, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int minQty = rs.getInt("quantity");//fixme zaki here quantity, discount_percentage
                double percent = rs.getDouble("discount_percentage");
                list.add(new DiscountDTO(minQty, percent));
            }
        }
        return list;
    }

    public String getBySupplierPro(SupItemDTO supItemDTO) {
        String sql = "SELECT agreement_id FROM SupItems WHERE supplierId=? and productId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supItemDTO.getSupplierId());
            stmt.setString(2, supItemDTO.getproduct().getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("agreement_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAllAgreementItem(String agreementId) throws  SQLException {
        //deleteDiscounts(productId, supplierId);
        String sql = "DELETE FROM SupItems WHERE agreement_id=? ";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreementId);
            stmt.executeUpdate();
        }
    }
    public SupItemDTO getByAgreementPro(String agreementId, String productId) throws  SQLException{
        String sql = "SELECT * FROM SupItems WHERE agreement_id=? and productId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreementId);
            stmt.setString(2, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String supplier_id=rs.getString("supplierId");
                List<DiscountDTO> dis =getDiscounts(productId,supplier_id );
                ManufacturerDTO manu = ManufacturerDAO.getInstance().getManuDTOById(rs.getString("manufacturer"));
                return new SupItemDTO(
                        new ProductDTO(productId,rs.getString("productName")),
                        manu,
                        supplier_id,
                        agreementId,
                        rs.getDouble("price"),
                        rs.getString("supplierCatNum"),
                        dis);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SupItemDTO remove_item_from_agreement(String agreementId, String productID) throws  SQLException{
        SupItemDTO subToRemove=getByAgreementPro(agreementId,productID);
        if(subToRemove==null)
            return null;
        //delete it from the data base
        String sql = "DELETE FROM SupItems WHERE agreement_id=? AND productId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreementId);
            stmt.setString(2, productID);
            stmt.executeUpdate();
        }
        return subToRemove;
    }

    public HashMap<String, SupItemDTO> getByAgreementId(String agreementId) {
        HashMap<String, SupItemDTO> items = new HashMap<>();
        String sql = "SELECT * FROM SupItems WHERE agreement_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreementId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String productId = rs.getString("productId");
                String productName = rs.getString("productName");
                String supplierId = rs.getString("supplierId");
                double price = rs.getDouble("price");
                String supplierCatNum = rs.getString("supplierCatNum");
                String manufacturerId = rs.getString("manufacturer");

                ManufacturerDTO manu = ManufacturerDAO.getInstance().getManuDTOById(manufacturerId);
                List<DiscountDTO> discounts = getDiscounts(productId, supplierId);

                SupItemDTO item = new SupItemDTO(
                        new ProductDTO(productId, productName),
                        manu,
                        supplierId,
                        agreementId,
                        price,
                        supplierCatNum,
                        discounts
                );

                items.put(productId, item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

}
