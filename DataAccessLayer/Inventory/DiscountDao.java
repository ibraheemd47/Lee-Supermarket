package DataAccessLayer.Inventory;

import ObjectDTO.Inventory.DiscountDTO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DiscountDao {
    Optional<DiscountDTO> findById(Integer id) throws SQLException;
    List<DiscountDTO> findAll() throws SQLException;
    List<DiscountDTO> findActive(Date onDate) throws SQLException;
    List<DiscountDTO> findByType(String type) throws SQLException;
    Integer insertCategoryDiscount(Integer categoryId, Integer discountId,Integer isActive) throws SQLException;
    boolean deleteCategoryDiscount(Integer categoryId, Integer discountId) throws SQLException;
    boolean updateCategoryDiscountIsActive(Integer categoryId, Integer discountId,Integer isActive) throws SQLException;
    boolean getCategoryDiscountIsActive(Integer categoryId, Integer discountId) throws SQLException;
    List<DiscountDTO> findByCategoryIdActive(Integer categoryId) throws SQLException;
    List<DiscountDTO> findByCategoryId(Integer categoryId) throws SQLException;
    //insert to item_dsicount table
    Integer insertItemDiscount(Integer itemId, Integer discountId,Integer isActive) throws SQLException;
    boolean updateItemDiscountIsActive(Integer itemId, Integer discountId,Integer isActive) throws SQLException;
    boolean getItemDiscountIsActive(Integer itemId, Integer discountId) throws SQLException;
    List<DiscountDTO> findByItemIdActive(Integer itemId) throws SQLException;
    List<DiscountDTO> findByItemId(Integer itemId) throws SQLException;
    boolean deleteItemDiscount(Integer itemId, Integer discountId) throws SQLException;
    Integer insert(DiscountDTO discount) throws SQLException;
    boolean update(DiscountDTO discount) throws SQLException;
    boolean deleteById(Integer id) throws SQLException;
    Optional<Integer> findMaxId() throws SQLException;
}
