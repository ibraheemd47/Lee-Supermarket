package DataAccessLayer.Inventory;

import ObjectDTO.Inventory.CategoryDTO;
import ObjectDTO.Inventory.ItemDTO;
import ObjectDTO.Inventory.ParentCategoryDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryDao {
    Optional<CategoryDTO> findById(Integer id) throws SQLException;
    Optional<CategoryDTO> findByName(String name) throws SQLException;
    List<CategoryDTO> findAll() throws SQLException;
    List<CategoryDTO> findAllSons(Integer parent_id) throws SQLException;
    List<Integer> findAllItemsIdInCategory(Integer categoryId) throws SQLException;
    boolean insertCategoryItem(int categoryId, int itemId) throws SQLException ;
    CategoryDTO insert(CategoryDTO category) throws SQLException;
    boolean update(CategoryDTO category) throws SQLException;
    boolean deleteById(Integer id) throws SQLException;
    Optional<Integer> findMaxId() throws SQLException;
}
