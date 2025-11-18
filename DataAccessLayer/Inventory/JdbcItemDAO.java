package DataAccessLayer.Inventory;

import ObjectDTO.Inventory.ItemDTO;
import ObjectDTO.Inventory.PriceHistoryEntryDTO;
import DataAccessLayer.Inventory.util.Database;
import domainLayer.Inventory.PriceHistoryEntry;

import java.sql.*;
import java.time.Instant;
import java.util.*;

public class JdbcItemDAO implements ItemDao {
    //private Object Instant;

    @Override
    public Optional<ItemDTO> findById(Integer id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ItemDTO item = new ItemDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("category_id"),
                            (Instant.parse(rs.getString("expiration_date"))
                            ), rs.getInt("quantity_store"), rs.getInt("quantity_warehouse"), rs.getString("supplier"), rs.getString("manufacturer"), rs.getDouble("buying_price"), rs.getDouble("selling_price"), rs.getInt("demand"), rs.getString("aisle_store"), rs.getString("shelf_store"), rs.getString("aisle_warehouse"), rs.getString("shelf_warehouse"));
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<ItemDTO> findAll() {
        //System.out.println("Fetching all items from the database...");
        String sql = "SELECT * FROM items";
        List<ItemDTO> items = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
           // System.out.println("Executing query: " + sql);
            ResultSet rs = ps.executeQuery();
            //System.out.println("before while ");
            while (rs.next()) {
                //System.out.println("Processing item with ID: " + rs.getInt("id"));
                ItemDTO Item = new ItemDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("category_id"),
                        (Instant.parse(rs.getString("expiration_date"))
                        ), rs.getInt("quantity_warehouse"), rs.getInt("quantity_store"), rs.getString("supplier"), rs.getString("manufacturer"), rs.getDouble("buying_price"), rs.getDouble("selling_price"), rs.getInt("demand"), rs.getString("aisle_store"), rs.getString("shelf_store"), rs.getString("aisle_warehouse"), rs.getString("shelf_warehouse"));
                items.add(Item);
                //System.out.println(items);
                //System.out.println("in while: ");
            }
            //System.out.println("after while: " + rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching items from the database: " + e.getMessage());
        }
        //System.out.println("Fetched " + items.size() + " items from the database.");
//        if (items.isEmpty()) {
//            System.out.println("No items found in the database.");
//        } else {
//            System.out.println("Items found: " + items);
//        }
        return items;
    }

    @Override
    public Optional<ItemDTO> findByIdWithHistory(Integer id) {
        // TODO : Implement here
return null ;
    }

    @Override
    public Map<Integer,List<PriceHistoryEntryDTO>> findAllWithHistory() {
        List<ItemDTO> items = findAll();
        List<PriceHistoryEntryDTO> ListOfPriceHistory=new ArrayList<>() ;
        Map<Integer,List<PriceHistoryEntryDTO>> itemsWithHistory = new HashMap<>();
        for (ItemDTO item : items) {

          ListOfPriceHistory=findHistoryById(item.id());
            if (ListOfPriceHistory != null && !ListOfPriceHistory.isEmpty()) {
                itemsWithHistory.put(item.id(), ListOfPriceHistory);
            } else {
                itemsWithHistory.put(item.id(), Collections.emptyList());
            }


        }
        return itemsWithHistory;
    }

    @Override
    public PriceHistoryEntryDTO insertPriceHistory(PriceHistoryEntryDTO price,Integer item_id) throws SQLException {
        String sql = "INSERT INTO price_history(item_id, selling_price, buying_price, date) VALUES (?,?,?,?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item_id);
            ps.setDouble(2, price.sellingPrice());
            ps.setDouble(3, price.buyingPrice());
            ps.setString(4, price.date().toString() );
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new PriceHistoryEntryDTO(
                            price.sellingPrice(),
                            price.buyingPrice(),
                            price.date()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting price history entry: " + e.getMessage());
        }
        return price;
    }

    @Override
    public List<PriceHistoryEntryDTO> findHistoryById(Integer id) {
        String sql = "SELECT * FROM price_history WHERE item_id = ?";
        List<PriceHistoryEntryDTO> history = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                history.add(new PriceHistoryEntryDTO(
                        rs.getDouble("selling_price"),
                        rs.getDouble("buying_price"),
                        Instant.parse(rs.getString("date"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public ItemDTO insert(ItemDTO dto) {
        String sql = """
                INSERT INTO items(id,name, category_id, expiration_date, quantity_store, quantity_warehouse, supplier, manufacturer, buying_price, selling_price, demand, aisle_store, shelf_store, aisle_warehouse, shelf_warehouse)
                
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);
                """;
//        System.out.println("Preparing to insert item: " + dto);
//        System.out.println("SQL Statement: " + sql);
//        System.out.println("Database Connection: " + Database.getConnection());
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            System.out.println("Passed the Connection----------");
            ps.setInt(1, dto.id());
            ps.setString(2, dto.name());
            ps.setInt(3, dto.category_Id());
            ps.setString(4, dto.expirationDate().toString());
            ps.setInt(5, dto.quantityInStore());
            ps.setInt(6, dto.quantityInWarehouse());
            ps.setString(7, dto.supplier());
            ps.setString(8, dto.manufacturer());
            ps.setDouble(9, dto.buyingPrice());
            ps.setDouble(10, dto.sellingPrice());
            ps.setInt(11, dto.demand());
            ps.setString(12, dto.aisle_store());
            ps.setString(13, dto.shelf_store());
            ps.setString(14, dto.aisle_warehouse());
            ps.setString(15, dto.shelf_warehouse());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                ItemDTO saved = new ItemDTO(rs.getInt(1), dto.name(), dto.category_Id(), dto.expirationDate(), dto.quantityInWarehouse(), dto.quantityInStore(), dto.supplier(), dto.manufacturer(), dto.buyingPrice(), dto.sellingPrice(), dto.demand(), dto.aisle_store(), dto.shelf_store(), dto.aisle_warehouse(), dto.shelf_warehouse());
                return saved;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error inserting item into the database: " + e.getMessage());
        }

    }

    @Override
    public boolean update(ItemDTO dto) {
        String sql = """
                UPDATE items SET name = ?, category_id = ?, expiration_date = ?, quantity_store = ?, quantity_warehouse = ?,
                supplier = ?, manufacturer = ?, buying_price = ?, selling_price = ?, demand = ? , aisle_store = ?,shelf_store = ?,aisle_warehouse = ?,shelf_warehouse = ? WHERE id = ?""";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)) {
           // System.out.println("Preparing to update item: " + dto);
            ps.setString(1, dto.name());
            ps.setInt(2, dto.category_Id());
            ps.setString(3, dto.expirationDate().toString());
            ps.setInt(4, dto.quantityInStore());
            ps.setInt(5, dto.quantityInWarehouse());
            ps.setString(6, dto.supplier());
            ps.setString(7, dto.manufacturer());
            ps.setDouble(8, dto.buyingPrice());
            ps.setDouble(9, dto.sellingPrice());
            ps.setInt(10, dto.demand());
            ps.setString(11, dto.aisle_store());
            ps.setString(12, dto.shelf_store());
            ps.setString(13, dto.aisle_warehouse());
            ps.setString(14, dto.shelf_warehouse());
            ps.setInt(15, dto.id());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                ItemDTO saved = new ItemDTO(rs.getInt(1), dto.name(), dto.category_Id(), dto.expirationDate(), dto.quantityInWarehouse(), dto.quantityInStore(), dto.supplier(), dto.manufacturer(), dto.buyingPrice(), dto.sellingPrice(), dto.demand(), dto.aisle_store(), dto.shelf_store(), dto.aisle_warehouse(), dto.shelf_warehouse());
                return saved != null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating item in the database: " + e.getMessage());
        }

    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Optional<Integer> findMaxId() {
        String sql = "SELECT MAX(id) AS max_id FROM items";
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