package DataAccessLayer.Inventory;

import ObjectDTO.Inventory.SupplierItemDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SupplierItemDao {
    // Create
    SupplierItemDTO insertSupplierItem(SupplierItemDTO supplierItem) throws SQLException;

    // Read
    Optional<SupplierItemDTO> findBySupplierId(String supplierId) throws SQLException;
    Optional<SupplierItemDTO> findByItemId(int itemId) throws SQLException;
    List<SupplierItemDTO> findAll() throws SQLException;

    // Update
    boolean updateSupplierItem(SupplierItemDTO supplierItem) throws SQLException;

    // Delete
    boolean deleteBySupplierId(String supplierId) throws SQLException;
    boolean deleteByItemId(int itemId) throws SQLException;
}
