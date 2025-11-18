package DataAccessLayer.Inventory;

import DataAccessLayer.Inventory.util.Database;
import ObjectDTO.Inventory.AlertDTO;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAlertDAO implements AlertDao{
    @Override
    public Optional<AlertDTO> findById(Integer id) {
        String sql = "SELECT * FROM alerts WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql))  {
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AlertDTO alert = new AlertDTO(rs.getInt("id"),
                            rs.getString("message"),
                            rs.getString("type"),
                            Instant.parse(rs.getString("date_created")),
                            rs.getInt("item_id")) ;
                    return Optional.of(alert);
                }
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<AlertDTO> findAll() {
        String sql = "SELECT * FROM alerts";
        List<AlertDTO> alerts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)){
             ResultSet rs = ps.executeQuery() ;
                 while(rs.next()) {
                     alerts.add(new AlertDTO(rs.getInt("id"),rs.getString("message"),rs.getString("type"),Instant.parse(rs.getString("date_created")),rs.getInt("item_id")));
                 }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return alerts;
    }

    @Override
    public List<AlertDTO> findByType(String type) {
        String sql = "SELECT * FROM alerts WHERE type = ?";
        List<AlertDTO> alerts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql))  {
            ps.setString(3,type);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    alerts.add( new AlertDTO(rs.getInt("id"),
                            rs.getString("message"),
                            rs.getString("type"),
                            Instant.parse(rs.getString("date_created")),
                            rs.getInt("item_id")));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alerts;
    }

    @Override
    public List<AlertDTO> findByDateCreatedBetween(Date from, Date to) {
            String sql = "SELECT * FROM alerts WHERE date_created BETWEEN ? AND ?";
            List<AlertDTO> alerts = new ArrayList<>();
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setString(1, from.toString()); // expects format "yyyy-MM-dd"
                ps.setString(2, to.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        alerts.add(new AlertDTO(
                                rs.getInt("id"),
                                rs.getString("message"),
                                rs.getString("type"),
                                Instant.parse(rs.getString("date_created")),
                                rs.getInt("item_id")
                        ));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return alerts;
        }



    @Override
    public List<AlertDTO> findByItemID(int itemId) {
        String sql = "SELECT * FROM alerts WHERE item_id = ?";
        List<AlertDTO> alerts = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    alerts.add(new AlertDTO(
                            rs.getInt("id"),
                            rs.getString("message"),
                            rs.getString("type"),
                            Instant.parse(rs.getString("date_created")),
                            rs.getInt("item_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alerts;
    }

    @Override
    public AlertDTO insert(AlertDTO alert) {

           String sql = """
                    INSERT INTO alerts(id,message,type,date_created,item_id)
                    VALUES(?,?,?,?,?)
                    """;
           try(PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
               ps.setInt(1, alert.id());
               ps.setString(2,alert.message());
               ps.setString(3,alert.type());
               ps.setString(4,alert.dateCreated().toString()); // Assuming dateCreated is an Instant, convert it to String
               ps.setInt(5, alert.item());
               ps.executeUpdate();
               try(ResultSet rs = ps.getGeneratedKeys()){
                   rs.next();
                   AlertDTO saved =new AlertDTO(rs.getInt(1),alert.message(),alert.type(),alert.dateCreated(),alert.item());
                   System.out.println(saved);
                   return saved;

               }
           } catch (SQLException e) {
               System.out.println("Error inserting alert to database: " + e.getMessage());
               throw new RuntimeException(e);
           }

    }

    @Override
    public boolean update(AlertDTO alert) {
        String sql = """
        UPDATE alerts
        SET message = ?, type = ?, date_created = ?, item_id = ?
        WHERE id = ?
    """;
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, alert.id());
            ps.setString(2, alert.message());
            ps.setString(3, alert.type());
            ps.setString(4, alert.dateCreated().toString());
            ps.setInt(5, alert.item());

            int rows = ps.executeUpdate();
            return rows > 0; // True if any row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM alerts WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0; // True if any row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Integer> findMaxId() throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM alerts";
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
