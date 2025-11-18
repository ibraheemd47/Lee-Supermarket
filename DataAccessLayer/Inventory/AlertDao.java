package DataAccessLayer.Inventory;

import ObjectDTO.Inventory.AlertDTO;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface AlertDao {
    Optional<AlertDTO> findById(Integer id) throws SQLException;
    List<AlertDTO> findAll() throws SQLException;
    List<AlertDTO> findByType(String type) throws SQLException;
    List<AlertDTO> findByDateCreatedBetween(Date from, Date to) throws SQLException;
    List<AlertDTO> findByItemID(int itemID) throws SQLException;


    AlertDTO insert(AlertDTO alert) throws SQLException;
    boolean update(AlertDTO alert) throws SQLException;
    boolean deleteById(Integer id) throws SQLException;
    Optional<Integer> findMaxId() throws SQLException;
}
