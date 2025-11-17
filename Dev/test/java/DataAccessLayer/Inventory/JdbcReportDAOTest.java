package DataAccessLayer.Inventory;

import DataAccessLayer.Inventory.util.Database;
import ObjectDTO.Inventory.CategoryReportDTO;
import ObjectDTO.Inventory.ItemReportDTO;
import ObjectDTO.Inventory.ReportDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcReportDAOTest {

    private static ReportDao dao;
    private static ReportDTO report;
    private static Connection conn = Database.getConnection();

    @BeforeAll
    static void setup() throws Exception {
        dao = new JdbcReportDAO();


    }
    @AfterEach
    void tearDown() throws SQLException {
        try ( Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM category_reports");
            stmt.executeUpdate("DELETE FROM item_reports");
            stmt.executeUpdate("DELETE FROM reports");
        }
    }

    @Test
    void insertReport() {
        ReportDTO result = null;
        report = new ReportDTO(2, "Monthly Report", "Desc", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()));
        try {
            if(dao.findReportById(2).isPresent()) {
                dao.deleteReportById(2);
            }
            dao.insertReport(report);
            result = dao.findReportById(2).get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, result.id());
        assertEquals("Monthly Report", result.name());

    }

    @Test
    void findReportById() throws SQLException {
        Optional<ReportDTO> found = null;
        report = new ReportDTO(2, "Monthly Report", "Desc", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()));
        dao.insertReport(report);
        try {
            found = dao.findReportById(2);
            System.out.println(found);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertTrue(found.isPresent());
        assertEquals("Monthly Report", found.get().name());
    }

    @Test
    void findAllReports() throws SQLException {
        List<ReportDTO> reports = null;
        report = new ReportDTO(2, "Monthly Report", "Desc", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()));
        dao.insertReport(report);
        try {
            reports = dao.findAllReports();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertFalse(reports.isEmpty());
        assertEquals("Monthly Report", reports.get(0).name());
    }

    @Test
    void updateReport() throws SQLException {
        report = new ReportDTO(2, "Monthly Report", "Desc", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()));
        dao.insertReport(report);
        boolean success = false;
        try {
           dao.updateReport(new ReportDTO(2, "Updated", "Updated Desc", new java.sql.Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
            System.out.println(dao.findAllReports());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Optional<ReportDTO> reportDTO = dao.findReportById(2);
        System.out.println("UPDATE REPORT TEST "+report);
        assertEquals(2,reportDTO.get().id());
        assertEquals("Updated",reportDTO.get().name());
        assertEquals("Updated Desc",reportDTO.get().description());

        Optional<ReportDTO> fetched = null;
        try {
            fetched = dao.findReportById(2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertCategoryReport() {
        CategoryReportDTO cat = new CategoryReportDTO(1, List.of(10, 20), "Cat Name", "Cat Desc", Instant.now(), Instant.now());
        ReportDTO reportDTO = new ReportDTO(1,"Cat Name","Cat Desc", new java.util.Date(System.currentTimeMillis()),new java.util.Date(System.currentTimeMillis()));
        List<CategoryReportDTO> results = null;
        try {
            results = dao.insertCategoryReport(cat);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertFalse(results.isEmpty());
        assertEquals(1, results.get(0).categories_ids().size());
    }

    @Test
    void findCategoryReportById() throws SQLException {
        report = new ReportDTO(2, "Monthly Report", "Desc", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()));
        dao.insertReport(report);
        CategoryReportDTO cat = new CategoryReportDTO(2, List.of(10, 20), "Cat Name", "Cat Desc", Instant.now(), Instant.now());
        dao.insertCategoryReport(cat);
        Optional<CategoryReportDTO> result = null;
        try {
            result = dao.findCategoryReportById(2);
            System.out.println(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertTrue(result.isPresent());
        assertEquals(2, result.get().reportId());
    }

    @Test
    void deleteCategoryReportById() throws SQLException {
        CategoryReportDTO cat = new CategoryReportDTO(1, List.of(10, 20), "Cat Name", "Cat Desc", Instant.now(), Instant.now());
        dao.insertCategoryReport(cat);
        try {
            assertTrue(dao.deleteCategoryReportById(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertItemReport() {
        ItemReportDTO dto = new ItemReportDTO(1, "Item Name", "Item Desc", Instant.now(), Instant.now(), 55, 3);
        ItemReportDTO result = null;
        try {
            result = dao.insertItemReport(dto);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        assertEquals(1, result.report_id());
        assertEquals(55, result.item_id());
    }

    @Test
    void findItemReportById() throws SQLException {
        Optional<ItemReportDTO> result = null;
        report = new ReportDTO(2, "Monthly Report", "Desc", new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()));
        dao.insertReport(report);
        ItemReportDTO dto = new ItemReportDTO(2, "Item Name", "Item Desc", Instant.now(), Instant.now(), 55, 3);
        dao.insertItemReport(dto);
        try {
            result = dao.findItemReportById(2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertTrue(result.isPresent());
        assertEquals(55, result.get().item_id());
    }

    @Test
    void deleteItemReportById() throws SQLException {
        boolean deleted = false;
        try {
            dao.deleteItemReportById(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
      Optional<ItemReportDTO> ItemReport =   dao.findItemReportById(1);
        assertTrue(ItemReport.isEmpty());
        try {
            assertTrue(dao.findReportById(1).isEmpty());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}