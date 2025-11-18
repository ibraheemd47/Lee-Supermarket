package DataAccessLayer.Inventory;

import DataAccessLayer.Inventory.util.Database;
import ObjectDTO.Inventory.CategoryReportDTO;
import ObjectDTO.Inventory.ItemReportDTO;
import ObjectDTO.Inventory.ReportDTO;

import java.sql.Date;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JdbcReportDAO implements ReportDao{

    @Override
    public Optional<ReportDTO> findReportById(Integer reportId) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate start = LocalDate.parse(rs.getString("start_date"), formatter);
                    LocalDate end = LocalDate.parse(rs.getString("end_date"), formatter);

                    java.util.Date startDate = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    java.util.Date endDate = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    ReportDTO report = new ReportDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            startDate,
                            endDate
                    );

                    return Optional.of(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<ReportDTO> findAllReports() {
        String sql = "SELECT * FROM reports";
        List<ReportDTO> reports = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String rawStart = rs.getString("start_date");
                String rawEnd = rs.getString("end_date");

                LocalDate startDate = LocalDate.parse(rawStart, dateFormatter);
                LocalDate endDate = LocalDate.parse(rawEnd, dateFormatter);

                java.util.Date utilStart = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.util.Date utilEnd = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                reports.add(new ReportDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        utilStart,
                        utilEnd
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }



    @Override
    public List<ReportDTO> findReportsByDateRange(Date from, Date to) {
        String sql = "SELECT * FROM reports WHERE date_created BETWEEN ? AND ?";
        List<ReportDTO> reports = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, from.toString()); // expects format "yyyy-MM-dd"
            ps.setString(2, to.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(new ReportDTO(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            new java.util.Date(rs.getString("start_date")),
                            new java.util.Date(rs.getString("end_date"))));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public ReportDTO insertReport(ReportDTO report) {
     //   System.out.println("Inserting report: " + report);
            String sql = """
                    INSERT INTO reports(id,name,description,start_date,end_date)
                    VALUES(?,?,?,?,?)
                    """;
            try(PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, report.id());
                ps.setString(2,report.name());
                ps.setString(3,report.description());
                ps.setString(4,report.startDate().toString());
                ps.setString(5, report.endDate().toString());

                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()){
                    rs.next();
                    ReportDTO saved =new ReportDTO(rs.getInt(1),
                            report.name(),
                            report.description(),
                            report.startDate(),
                            report.endDate());
                    return saved;

                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

    }

    @Override
    public boolean updateReport(ReportDTO report) {
        String sql = """
        UPDATE reports
        SET name = ?, description = ?, start_date = ?, end_date = ?
        WHERE id = ?
    """;
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
           // System.out.println(report.name()+report.description());
            ps.setString(1, report.name());
            ps.setString(2, report.description());
            ps.setString(3, report.startDate().toString());
            ps.setString(4, report.endDate().toString());
            ps.setInt(5, report.id());

            int rows = ps.executeUpdate();
            return rows > 0; // True if any row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteReportById(Integer reportId) {
        // Delete a report by its ID
        String sql = "DELETE FROM reports WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reportId);
            return ps.executeUpdate() > 0; // Returns true if a row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Optional<CategoryReportDTO> findCategoryReportById(Integer reportId){
        Optional<ReportDTO> reportOpt = this.findReportById(reportId);
        ReportDTO report = reportOpt.get();
        CategoryReportDTO catReport = new CategoryReportDTO(reportId,
                this.findCategoriesIDSReportById(reportId),
                report.name(), // name will be set later
                report.description(), // description will be set later
                report.startDate().toInstant(), // startDate will be set later
                report.endDate().toInstant()  // endDate will be set later
        );
        return Optional.of(catReport);
    }
    private List<Integer> findCategoriesIDSReportById(Integer reportId) {
        String sql = "SELECT category_id FROM category_reports WHERE report_id = ?";
        List<Integer> categoryIds = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categoryIds.add( rs.getInt("category_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryIds;
    }
    public Map<Integer,List<CategoryReportDTO>> findCategoryReportsByDateRange(Date from, Date to) {
        // Ensure the date range is valid
        if (from == null || to == null || from.after(to)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        // Fetch reports in the given date range from the report table
        List<ReportDTO> reportsInRange = this.findReportsByDateRange(from, to); // Ensure such a method exists in your repository

        // Extract category IDs and report IDs
        List<CategoryReportDTO> categoryReportDTOs = new ArrayList<>();
        Map<Integer, List<CategoryReportDTO>> categoryReportMap = new HashMap<>();
        for (ReportDTO report : reportsInRange) {
            int id = report.id();
            String sql = "SELECT * FROM category_reports WHERE report_id = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CategoryReportDTO categoryReport = new CategoryReportDTO(id,
                                this.findCategoriesIDSReportById(id),
                                rs.getString("name"),
                                rs.getString("description"),

                                report.startDate().toInstant(),
                                report.endDate().toInstant()
                        );
                        categoryReportDTOs.add(categoryReport);
                    }
                    categoryReportMap.put(id, categoryReportDTOs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return categoryReportMap;
    }



    @Override
    public List<CategoryReportDTO> insertCategoryReport(CategoryReportDTO report) {
        List<CategoryReportDTO> categoryReports = new ArrayList<>();

        for(Integer categoryId : report.categories_ids()){
            if(!this.insertIndvidualCategoryReport(report.reportId(), categoryId)){
                throw new RuntimeException("Failed to insert category report for category ID: " + categoryId);
            }
            CategoryReportDTO categoryReport = new CategoryReportDTO(
                    report.reportId(),
                    this.findCategoriesIDSReportById(report.reportId()),
                    report.name(),
                    report.description(),
                    report.startDate(),
                    report.endDate()
            );
            categoryReports.add(categoryReport);

        }
        return categoryReports ;

    }
    private boolean insertIndvidualCategoryReport(int reportId,int categoryId) {
        String sql = """
                INSERT INTO category_reports(report_id, category_id)
                VALUES(?,?)
                """;
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reportId);
            ps.setInt(2, categoryId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean deleteCategoryReportById(Integer reportId) {
        List<Integer> categoryIds = this.findCategoriesIDSReportById(reportId);
        if (categoryIds.isEmpty()) {
            return false; // No categories found for the report
        }
        String sql = "DELETE FROM category_reports WHERE report_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reportId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Returns true if any row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<ItemReportDTO> findItemReportById(Integer reportId) {
        //System.out.println("Before Extraction");

        Optional<ReportDTO> reportDTO = findReportById(reportId);
        if (reportDTO.isEmpty()) {
         //   System.out.println("No report found for id: " + reportId);
            return Optional.empty();
        }

        String sql = "SELECT * FROM item_reports WHERE report_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    //System.out.println("Here EXP");
                    ReportDTO dto = reportDTO.get();

                    ItemReportDTO itemReport = new ItemReportDTO(
                            rs.getInt("report_id"),
                            dto.name(),
                            dto.description(),
                            dto.startDate().toInstant(),
                            dto.endDate().toInstant(),
                            rs.getInt("item_id"),
                            rs.getInt("defective_quantity")
                    );
                    return Optional.of(itemReport);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<ItemReportDTO> findItemReportsByDateRange(Date from, Date to) {
        List<ReportDTO> reports = this.findReportsByDateRange(from, to);
        List<ItemReportDTO> itemReports = new ArrayList<>();
        for (ReportDTO report : reports) {
            String sql = "SELECT * FROM item_reports WHERE report_id = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setInt(1, report.id());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ItemReportDTO itemReport = new ItemReportDTO(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                Instant.parse(report.startDate().toString()),
                                Instant.parse(report.endDate().toString()),
                                rs.getInt("item_id"),
                                rs.getInt("defective_quantity")
                        );
                        itemReports.add(itemReport);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return itemReports;
    }



    @Override
    public ItemReportDTO insertItemReport(ItemReportDTO report) {

       // System.out.println("Inserting item report: " + report);
        String sql = """
                INSERT INTO item_reports(report_id, item_id, defective_quantity)
                VALUES(?,?,?)
                """;
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, report.report_id());
            ps.setInt(2, report.item_id());
            ps.setInt(3, report.defectiveQuantity());
            ps.executeUpdate();
//            try (ResultSet rs = ps.getGeneratedKeys()) {
//                rs.next();
//                ItemDTO saved = new ItemDTO(rs.getInt(1), dto.name(), dto.category_Id(), dto.expirationDate(), dto.quantityInWarehouse(), dto.quantityInStore(), dto.supplier(), dto.manufacturer(), dto.buyingPrice(), dto.sellingPrice(), dto.demand(), dto.aisle_store(), dto.shelf_store(), dto.aisle_warehouse(), dto.shelf_warehouse());
//                return saved;
//            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    return new ItemReportDTO(
                            rs.getInt(1),
                            report.name(),
                            report.description(),
                            report.startDate(),
                            report.endDate(),
                            report.item_id(),
                            report.defectiveQuantity()
                    );
                }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert item report", e);
        }


    }



    @Override
    public boolean deleteItemReportById(Integer reportId) {
        String sql = "DELETE FROM reports WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reportId);
            if(this.deleteReportById(reportId)){
                return ps.executeUpdate() > 0; // Returns true if a row was deleted
            }
            else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Integer> findMaxId() throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM reports";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int maxId = rs.getInt("max_id");
                    if (!rs.wasNull()) {
                        return Optional.of(maxId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
