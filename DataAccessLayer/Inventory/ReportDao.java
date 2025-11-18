package DataAccessLayer.Inventory;

import ObjectDTO.Inventory.CategoryReportDTO;
import ObjectDTO.Inventory.ItemReportDTO;
import ObjectDTO.Inventory.ReportDTO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReportDao {

    Optional<ReportDTO> findReportById(Integer reportId) throws SQLException;
    List<ReportDTO> findAllReports() throws SQLException;
    List<ReportDTO> findReportsByDateRange(Date from, Date to) throws SQLException;

    ReportDTO insertReport(ReportDTO report) throws SQLException;
    boolean updateReport(ReportDTO report) throws SQLException;
    boolean deleteReportById(Integer reportId) throws SQLException;


    Optional<CategoryReportDTO> findCategoryReportById(Integer reportId) throws SQLException;
    Map<Integer,List<CategoryReportDTO>> findCategoryReportsByDateRange(Date from, Date to) throws SQLException;
    List<CategoryReportDTO> insertCategoryReport(CategoryReportDTO report) throws SQLException;
    boolean deleteCategoryReportById(Integer reportId) throws SQLException;


    Optional<ItemReportDTO> findItemReportById(Integer reportId) throws SQLException;
    List<ItemReportDTO> findItemReportsByDateRange(Date from, Date to) throws SQLException;
    ItemReportDTO insertItemReport(ItemReportDTO report) throws SQLException;
    boolean deleteItemReportById(Integer reportId) throws SQLException;
    Optional<Integer> findMaxId() throws SQLException;
}
