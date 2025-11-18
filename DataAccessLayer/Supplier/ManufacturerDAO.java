package DataAccessLayer.Supplier;

import ObjectDTO.Supplier.ManufacturerDTO;

import java.sql.*;
import java.util.HashMap;

public class ManufacturerDAO {
    private static ManufacturerDAO instance = null;
    private final Connection connection;
    HashMap<String , ManufacturerDTO> manufacturers;//<manufacturer-id:String, ManfuctrerDTO>

    private ManufacturerDAO() {
        manufacturers=new HashMap<>();
        this.connection = ConnectionManager.getConnection();
    }

    public static ManufacturerDAO getInstance() {
        if (instance == null)
            instance = new ManufacturerDAO();
        return instance;
    }

    public void insert(String id, String name) throws SQLException {
        String sql = "INSERT INTO Manufacturers (manfacturer_id, name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.executeUpdate();
        }
    }

    public void update(String id, String newName) throws SQLException {
        String sql = "UPDATE Manufacturers SET name = ? WHERE manfacturer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, id);
            stmt.executeUpdate();
            manufacturers.put(id, new ManufacturerDTO(id,newName));
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM Manufacturers WHERE manfacturer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            manufacturers.remove(id);
        }
    }

    public String getManuNameById(String id) throws SQLException {
        //try to get from the mapper if exist
        if (manufacturers.containsKey(id)) {
            return manufacturers.get(id).manufacturer_name();
        }

        //else get it from the data base
        String sql = "SELECT name FROM Manufacturers WHERE manfacturer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }

        //or null if not exist
        return null;
    }

    public ManufacturerDTO getManuDTOById(String id) throws SQLException {
        //try to get from the mapper if exist
        if (manufacturers.containsKey(id)) {
            return manufacturers.get(id);
        }

        //else get it from the data base
        String sql = "SELECT name FROM Manufacturers WHERE manfacturer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name= rs.getString("name");
                ManufacturerDTO manuToReturn=new ManufacturerDTO(id,name);

                manufacturers.put(id,manuToReturn);//added to mapper

                return manuToReturn;
            }
        }
        //or null if not exist
        return null;
    }

    public HashMap<String, String> getAll() throws SQLException {
        String sql = "SELECT * FROM Manufacturers";
        HashMap<String, String> manufacturers = new HashMap<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("manfacturer_id");
                String name = rs.getString("name");
                manufacturers.put(id, name);
            }
        }
        return manufacturers;
    }
    public boolean exists(String id) throws SQLException {
        //try to return from the mapper
        if(manufacturers.containsKey(id))
            return true;

        String sql = "SELECT 1 FROM Manufacturers WHERE manfacturer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public void deleteAllMans() {
        String sql = "DELETE FROM Manufacturers";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            manufacturers.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
