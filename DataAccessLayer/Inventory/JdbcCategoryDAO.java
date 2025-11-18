package DataAccessLayer.Inventory;

import DataAccessLayer.Inventory.util.Database;
import ObjectDTO.Inventory.CategoryDTO;
import ObjectDTO.Inventory.ItemDTO;
import ObjectDTO.Inventory.ParentCategoryDTO;
import jdk.jshell.spi.ExecutionControl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcCategoryDAO implements CategoryDao{
    @Override
    public Optional<CategoryDTO> findById(Integer id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql))  {
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CategoryDTO category = new CategoryDTO(rs.getInt("id"),
                            rs.getInt("parent_id"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getInt("value"));
                    return Optional.of(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<CategoryDTO> findByName(String name) {
        String sql = "SELECT * FROM categories WHERE name = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql))  {
            ps.setString(2,name);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CategoryDTO category = new CategoryDTO(rs.getInt("id"),
                            rs.getInt("parent_id"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getInt("value"));
                    return Optional.of(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<CategoryDTO> findAll() {
        String sql = "SELECT * FROM categories";
        List<CategoryDTO> categories = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(new CategoryDTO(rs.getInt("id"), rs.getInt("parent_id"),rs.getString("name"), rs.getInt("quantity"), rs.getInt("value")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;

    }

    @Override
    public List<CategoryDTO> findAllSons(Integer parent_id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE parent_id = ?";
        List<CategoryDTO> sons = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, parent_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sons.add(new CategoryDTO(rs.getInt("id"), rs.getInt("parent_id"), rs.getString("name"), rs.getInt("quantity"), rs.getInt("value")));
            }
            return sons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public List<Integer> findAllItemsIdInCategory(Integer categoryId) throws SQLException {
        String sql = "SELECT item_id FROM category_items WHERE category_id = ?";
        List<Integer> itemIds = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                itemIds.add(rs.getInt("item_id"));
            }
            return itemIds;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public CategoryDTO insert(CategoryDTO category) {

            String sql = """
                    INSERT INTO categories(id,name,quantity,value,parent_id)
                    VALUES(?,?,?,?,?)
                    """;
            try(PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, category.id());

                ps.setString(2,category.name());
                ps.setInt(3,category.quantity());
                ps.setInt(4,category.value());
                if (category.parentId() == 0) {
                    ps.setNull(5, java.sql.Types.INTEGER); // Set parent_id to NULL if no parent
                } else {
                    ps.setInt(5, category.parentId());
                }
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()){
                    rs.next();
                    CategoryDTO saved =new CategoryDTO(rs.getInt(1), category.parentId(), category.name(),category.quantity(),category.value());
                    return saved;

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

    }
    @Override
    public boolean insertCategoryItem(int categoryId, int itemId) throws SQLException {
        String sql = "INSERT INTO category_item (category_id, item_id) VALUES (?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, itemId);
            int rows = ps.executeUpdate();
            return rows > 0; // True if any row was inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean update(CategoryDTO category) {
        String sql = """
        UPDATE categories
        SET name = ?,parent_id = ? ,quantity = ?, value = ?
        WHERE id = ?
    """;
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {

            ps.setString(1, category.name());
            if (category.parentId() == 0) {
                ps.setNull(2, java.sql.Types.INTEGER); // Set parent_id to NULL if no parent
            } else {
                ps.setInt(2, category.parentId());
            }

            ps.setInt(3, category.quantity());
            ps.setInt(4,category.value());
            ps.setInt(5, category.id());

            int rows = ps.executeUpdate();
            return rows > 0; // True if any row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0; // True if any row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Optional<Integer> findMaxId() {
        String sql = "SELECT MAX(id) AS max_id FROM categories";
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
        return Optional.empty(); // אם אין ערכים בטבלה
    }
}
