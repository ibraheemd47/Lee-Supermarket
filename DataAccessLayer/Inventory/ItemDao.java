package DataAccessLayer.Inventory;

import ObjectDTO.Inventory.ItemDTO;
import ObjectDTO.Inventory.PriceHistoryEntryDTO;
import domainLayer.Inventory.PriceHistoryEntry;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemDao {
    Optional<ItemDTO> findById(Integer id) throws SQLException;
    List<ItemDTO> findAll() throws SQLException;


    Optional<ItemDTO> findByIdWithHistory(Integer id) throws SQLException;
    Map<Integer,List<PriceHistoryEntryDTO>> findAllWithHistory() throws SQLException;
    PriceHistoryEntryDTO insertPriceHistory(PriceHistoryEntryDTO price,Integer item_id) throws SQLException;

    List<PriceHistoryEntryDTO> findHistoryById(Integer id) throws SQLException;

    ItemDTO insert(ItemDTO dto) throws SQLException;

    boolean update(ItemDTO dto) throws SQLException;
    boolean deleteById(Integer id) throws SQLException;
    Optional<Integer> findMaxId() throws SQLException;

}
