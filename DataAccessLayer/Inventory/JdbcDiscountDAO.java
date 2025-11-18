package DataAccessLayer.Inventory;

import DataAccessLayer.Inventory.util.Database;
import ObjectDTO.Inventory.DiscountDTO;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcDiscountDAO implements DiscountDao {
    @Override
    public Optional<DiscountDTO> findById(Integer id) {
        String sql = "SELECT * FROM discounts WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new DiscountDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getDouble("discount_percentage"),
                            Instant.parse(rs.getString("start_date")),
                            Instant.parse(rs.getString("end_date"))
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<DiscountDTO> findAll() {
        String sql = "SELECT * FROM discounts";
        List<DiscountDTO> discounts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                discounts.add(new DiscountDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getDouble("discount_percentage"),
                        Instant.parse(rs.getString("start_date")),
                        Instant.parse(rs.getString("end_date"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }

    @Override
    public List<DiscountDTO> findActive(Date onDate) {
        String sql = "SELECT * FROM discounts WHERE start_date <= ? AND end_date >= ?";
        List<DiscountDTO> discounts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setDate(1, onDate);
            ps.setDate(2, onDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                discounts.add(new DiscountDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getDouble("discount_percentage"),
                        Instant.parse(rs.getString("start_date")),
                        Instant.parse(rs.getString("end_date"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }

    @Override
    public List<DiscountDTO> findByType(String type) {
        String sql = "SELECT * FROM discounts WHERE type = ?";
        List<DiscountDTO> discounts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                discounts.add(new DiscountDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getDouble("discount_percentage"),
                        Instant.parse(rs.getString("start_date")),
                        Instant.parse(rs.getString("end_date"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }



    @Override
    public Integer insertCategoryDiscount(Integer categoryId, Integer discountId,Integer isActive) throws SQLException {
        String sql = "INSERT INTO category_discounts(category_id, discount_id,active) VALUES (?, ?,?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, discountId);
            ps.setInt(3, isActive);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
    }

    @Override
    public boolean deleteCategoryDiscount(Integer categoryId, Integer discountId) throws SQLException {
        String sql = "DELETE FROM category_discounts WHERE category_id = ? AND discount_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, discountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
    }

    @Override
    public boolean updateCategoryDiscountIsActive(Integer categoryId, Integer discountId, Integer isActive) throws SQLException {
       String sql = "UPDATE category_discounts SET active = ? WHERE category_id = ? AND discount_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, categoryId);
            ps.setInt(3, discountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
    }

    @Override
    public boolean getCategoryDiscountIsActive(Integer categoryId, Integer discountId) throws SQLException {
        String sql = "SELECT active FROM category_discounts WHERE category_id = ? AND discount_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, discountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("active") == 1; // Assuming active is stored as 1 for true and 0 for false
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
        return false;
    }

    @Override
    public List<DiscountDTO> findByCategoryIdActive(Integer categoryId) throws SQLException {
        String sql = "SELECT * FROM category_discounts WHERE category_id = ? AND active = 1";
        List<DiscountDTO> discounts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int discountId = rs.getInt("discount_id");
                    Optional<DiscountDTO> discountOpt = findById(discountId);
                    if (discountOpt.isPresent()) {
                        discounts.add(discountOpt.get());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
        return discounts;
    }

    @Override
    public List<DiscountDTO> findByCategoryId(Integer categoryId) throws SQLException {
        String sql = "SELECT * FROM category_discounts WHERE category_id = ?";
        List<DiscountDTO> discounts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int discountId = rs.getInt("discount_id");
                    Optional<DiscountDTO> discountOpt = findById(discountId);
                    if (discountOpt.isPresent()) {
                        discounts.add(discountOpt.get());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
        return discounts;
    }

    @Override
    public Integer insertItemDiscount(Integer itemId, Integer discountId,Integer isActive) throws SQLException {
        String sql = "INSERT INTO item_discounts(item_id, discount_id,active) VALUES (?, ?,?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, discountId);
            ps.setInt(3, isActive);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
    }

    @Override
    public boolean updateItemDiscountIsActive(Integer itemId, Integer discountId, Integer isActive) throws SQLException {
        String sql = "UPDATE item_discounts SET active = ? WHERE item_id = ? AND discount_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, itemId);
            ps.setInt(3, discountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
    }

    @Override
    public boolean getItemDiscountIsActive(Integer itemId, Integer discountId) throws SQLException {
        String sql = "SELECT active FROM item_discounts WHERE item_id = ? AND discount_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, discountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("active") == 1; // Assuming active is stored as 1 for true and 0 for false
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
        return false; // Default to false if no record found
    }

    @Override
    public List<DiscountDTO> findByItemIdActive(Integer itemId) throws SQLException {
        String sql = "SELECT * FROM item_discounts WHERE item_id = ? And active = 1";
        List<DiscountDTO> discounts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int discountId = rs.getInt("discount_id");
                    Optional<DiscountDTO> discountOpt = findById(discountId);
                    if (discountOpt.isPresent()) {
                        discounts.add(discountOpt.get());
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
        return discounts;
    }

    @Override
    public List<DiscountDTO> findByItemId(Integer itemId) throws SQLException {
        String sql = "SELECT * FROM item_discounts WHERE item_id = ?";
        List<DiscountDTO> discounts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int discountId = rs.getInt("discount_id");
                    Optional<DiscountDTO> discountOpt = findById(discountId);
                    if (discountOpt.isPresent()) {
                        discounts.add(discountOpt.get());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
        return discounts;
    }

    @Override
    public boolean deleteItemDiscount(Integer itemId, Integer discountId) throws SQLException {
        String sql = "DELETE FROM item_discounts WHERE item_id = ? AND discount_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, discountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throwing to handle it in the calling method if needed
        }
    }

    @Override
    public Integer insert(DiscountDTO discount) {
        String sql = """
            INSERT INTO discounts(name, type, discount_percentage, start_date, end_date)
            VALUES (?, ?, ?, ?, ?)""";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, discount.name());
            ps.setString(2, discount.type());
            ps.setDouble(3, discount.discountPercentage());
            ps.setString(4, discount.startDate().toString());
            ps.setString(5,discount.endDate().toString());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(DiscountDTO discount) {
        String sql = """
            UPDATE discounts
            SET name = ?, type = ?, discount_percentage = ?, start_date = ?, end_date = ?
            WHERE id = ?
        """;
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, discount.name());
            ps.setString(2, discount.type());
            ps.setDouble(3, discount.discountPercentage());
            ps.setString(4, discount.startDate().toString());
            ps.setString(5,discount.endDate().toString());
            ps.setInt(6, discount.id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM discounts WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Integer> findMaxId() throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM discounts";
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
