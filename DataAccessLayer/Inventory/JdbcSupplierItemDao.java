package DataAccessLayer.Inventory;

import ObjectDTO.Inventory.SupplierItemDTO;
import DataAccessLayer.Inventory.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSupplierItemDao implements SupplierItemDao {

    @Override
    public SupplierItemDTO insertSupplierItem(SupplierItemDTO supplierItem) throws SQLException {
        String sql = "INSERT INTO supplier_item (supplier_id, item_id) VALUES (?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, supplierItem.supplier_id());
            ps.setInt(2, supplierItem.Item_Id());
            ps.executeUpdate();
        }
        return supplierItem;
    }

    @Override
    public Optional<SupplierItemDTO> findBySupplierId(String supplierId) throws SQLException {
        String sql = "SELECT * FROM supplier_item WHERE supplier_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new SupplierItemDTO(
                            rs.getString("supplier_id"),
                            rs.getInt("item_id")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<SupplierItemDTO> findByItemId(int itemId) throws SQLException {
        String sql = "SELECT * FROM supplier_item WHERE item_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new SupplierItemDTO(
                            rs.getString("supplier_id"),
                            rs.getInt("item_id")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<SupplierItemDTO> findAll() throws SQLException {
        List<SupplierItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier_item";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new SupplierItemDTO(
                        rs.getString(1),
                        rs.getInt(2
                )));
            }
        }
        return list;
    }

    @Override
    public boolean updateSupplierItem(SupplierItemDTO supplierItem) throws SQLException {
        // No update logic if there are only two keys and no additional data
        return false;
    }

    @Override
    public boolean deleteBySupplierId(String supplierId) throws SQLException {
        String sql = "DELETE FROM supplier_item WHERE supplier_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, supplierId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteByItemId(int itemId) throws SQLException {
        String sql = "DELETE FROM supplier_items WHERE item_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;
        }
    }
}
