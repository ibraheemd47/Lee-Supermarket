package DataAccessLayer.Inventory;

import DataAccessLayer.Inventory.util.Database;
import ObjectDTO.Inventory.SupplierItemDTO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSupplierItemDaoTest {
    private static JdbcSupplierItemDao dao;
    @BeforeAll
    static void setup() throws SQLException {
        dao = new JdbcSupplierItemDao();

    }
    @AfterEach
    void tearDown() throws SQLException {
        Connection conn = Database.getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM supplier_item");
        }
    }


    @Test
    void insertSupplierItem() {
        SupplierItemDTO dto = new SupplierItemDTO("1", 101);
        SupplierItemDTO result = null;
        try {
            result = dao.insertSupplierItem(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        assertEquals("1", result.supplier_id());
        assertEquals(101, result.Item_Id());
    }

    @Test
    void findBySupplierId() throws SQLException {
        SupplierItemDTO dto = new SupplierItemDTO("1", 101);
        dao.insertSupplierItem(dto);
        Optional<SupplierItemDTO> result = null;
        ;
        try {
            result = dao.findBySupplierId("1");
            System.out.println(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertTrue(result.isPresent());
        assertEquals("1", result.get().supplier_id());
        assertEquals(101, result.get().Item_Id());
    }

    @Test
    void findAll() throws SQLException {
        List<SupplierItemDTO> allItems = null;
        SupplierItemDTO dto = new SupplierItemDTO("1", 101);
        dao.insertSupplierItem(dto);

        try {
            allItems = dao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, allItems.size());
        assertEquals("1", allItems.get(0).supplier_id());
        assertEquals(101, allItems.get(0).Item_Id());
    }

    @Test
    void deleteBySupplierId() throws SQLException {
        SupplierItemDTO dto = new SupplierItemDTO("1", 101);
        dao.insertSupplierItem(dto);
        boolean deleted = false;
        try {
            deleted = dao.deleteBySupplierId("1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertTrue(deleted);

        Optional<SupplierItemDTO> result = null;
        try {
            result = dao.findBySupplierId("1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertTrue(result.isEmpty());
    }
}