package DataAccessLayer.Supplier;

import ObjectDTO.Supplier.ProductDTO;
import domainLayer.Supplier.Objects.Product;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProductDAO {
    private static ProductDAO instance = null;
    private Connection connection;
    private HashMap<String, ProductDTO> products;

    private ProductDAO() {
        this.connection = ConnectionManager.getConnection();
        this.products = loadData();
    }

    public static ProductDAO getInstance() {
        if (instance == null) {
            instance = new ProductDAO();
        }
        return instance;
    }

    public void insert(ProductDTO product) throws SQLException {
        String sql = "INSERT INTO Products (id, name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.executeUpdate();
            products.put(product.getId(), product);
        }
    }

    public ProductDTO get(String productId) {

        //try to get from the mapper if exist
        ProductDTO product = products.get(productId);
        if (product != null) {
            return product;
        }

        //check if exist in the data base
        if(!exists(productId))
            return null;


        //else not in the mapper check if in the data base

        String sql = "SELECT 1 FROM Products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                return new ProductDTO(
                        rs.getString("id"),
                        rs.getString("name")

                );
            }

        }
        catch (SQLException e){
            System.out.println("[Erorr] get the product from the database:"+e.toString());
        }
        return null;
    }

    private HashMap<String, ProductDTO> loadData() {
        HashMap<String, ProductDTO> data = new HashMap<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Products")) {
            while (rs.next()) {
                ProductDTO dto = new ProductDTO(rs.getString("id"), rs.getString("name"));
                data.put(dto.getId(), dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean exists(String id) {
        String sql = "SELECT 1 FROM Products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void update(ProductDTO product) throws SQLException {
        String sql = "UPDATE Products SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getId());
            stmt.executeUpdate();
            products.put(product.getId(), product);
        }

    }
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM Products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
        }

        //remove from mapper
        products.remove(id);
    }



    public Map<String, Product> getAll() {
        Map<String, Product> result = new HashMap<>();
        String sql = "SELECT * FROM Products";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                result.put(id, new Product(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Product getByName(String name) {
        String sql = "SELECT * FROM Products WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(rs.getString("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
