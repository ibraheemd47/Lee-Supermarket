package DataAccessLayer.Supplier;

//import DataAccessLayer.RepositoryIMP;
import ObjectDTO.Supplier.ContactDTO;
import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.SupplierDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SupplierDAO {
    private static SupplierDAO instance = null;
    private Connection connection;
    private HashMap<String , SupplierDTO> mapper;
    //private ContactDAO contactDAO;//getInstance()//todo:zaki check if needed
    private SupplierDAO() {
        this.connection = ConnectionManager.getConnection();
        if (this.connection == null) {
            throw new RuntimeException("Failed to establish database connection");
        }

        this.mapper = loadData();

    }

    public static SupplierDAO getInstance() {
        if (instance == null) {
            instance = new SupplierDAO();
        }
        return instance;
    }

    /**
     * Loads supplier data from the database and returns a mapping of supplier IDs to SupplierDTO objects.
     * The data is retrieved from the "Suppliers" table, and each record is converted into a SupplierDTO object.
     *
     * @return A HashMap where the keys are supplier IDs (as strings) and the values are SupplierDTO objects
     *         representing the suppliers retrieved from the "Suppliers" table. If an error occurs while
     *         processing the database query, an empty HashMap is returned.
     */
    private HashMap<String, SupplierDTO> loadData() {
        HashMap<String, SupplierDTO> suppliers = new HashMap<>();
        String sql = "SELECT * FROM Suppliers";  // Note: capital S
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String supplierId = rs.getString("id");  // changed from "Id"
                suppliers.put(supplierId, new SupplierDTO(
                        supplierId,
                        rs.getString("name"),           // changed from "Name"
                        rs.getString("bank_account"),   // changed from "BankAccount"
                        new ArrayList<>(),
                        (rs.getString("delivery_days")!=null)?DaysDTO.valueOf(rs.getString("delivery_days")):null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }


    /**
     * Inserts a new supplier record into the database and updates the local cache.
     *
     * @param supplierdto The SupplierDTO object containing the supplier information to be inserted.
     *                     - supplierdto.getId(): The unique identifier of the supplier.
     *                     - supplierdto.getName(): The name of the supplier.
     *                     - supplierdto.getBankAccount(): The bank account details of the supplier.
     *                     - supplierdto.getFixed_delivery_days(): Indicates if the supplier has fixed delivery days.
     *                     - supplierdto.getDaySup().toString(): The string representation of the supplier's delivery days.
     * @throws SQLException If there is an error executing the SQL insert statement.
     * @implNote verify that this supplier is not in the db and not null
     */
    public void insert(SupplierDTO supplierdto) throws SQLException {
        String sql = "INSERT INTO Suppliers (id, name, bank_account, fixed_delivery_days, delivery_days) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplierdto.getId());
            pstmt.setString(2, supplierdto.getName());
            pstmt.setString(3, supplierdto.getBankAccount());
            pstmt.setBoolean(4, supplierdto.getFixed_delivery_days());
            pstmt.setString(5, (supplierdto.getDaySup()!=null)?supplierdto.getDaySup().toString():null);
            pstmt.executeUpdate();
            mapper.put(supplierdto.getId(), supplierdto);



           //TODO: get the contacts from the supplier and insert in contact dao*****
        }
    }

    public SupplierDTO get(String id) throws SQLException {
        // Check if the supplier is already in the mapper (cache)
        SupplierDTO sup = mapper.get(id);
        if (sup != null)
            return sup;

        // Query the database
        String sql = "SELECT * FROM Suppliers WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    // No such supplier in DB
                    return null;
                }

                // Get contacts
                List<ContactDTO> contacts = new ArrayList<>(
                        ContactDAO.getInstance().getContactsBySupplier(id).values()
                );

                // Build the SupplierDTO
                SupplierDTO sup2 = new SupplierDTO(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("bank_account"),
                        contacts,
                        DaysDTO.fromString(rs.getString("delivery_days"))
                );

                // Cache it in the mapper
                mapper.put(id, sup2);
                return sup2;
            }
        } catch (SQLException e) {
            System.out.println("[Error] Failed to get the supplier from the database: " + e);
            // Optional: rethrow or return null
            return null;
        }
    }

    /**
     * Updates the details of an existing supplier in the database and updates the local cache.
     *
     * @param supplierdto The SupplierDTO object containing updated details of the supplier.
     *                     - supplierdto.getId(): The unique identifier of the supplier to update.
     *                     - supplierdto.getName(): The supplier's updated name.
     *                     - supplierdto.getBankAccount(): The supplier's updated bank account details.
     *                     - supplierdto.getFixed_delivery_days(): A boolean indicating if the supplier has fixed delivery days.
     *                     - supplierdto.getDaySup().toString(): The updated delivery days string representation.
     *
     * @throws SQLException If there is an error executing the SQL update statement.
     * @implNote verify that this supplier is in the db and not null
     */
    public void update(SupplierDTO supplierdto) throws SQLException {
        String sql = "UPDATE Suppliers SET name = ?, bank_account = ?, fixed_delivery_days = ?, delivery_days = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplierdto.getName());
            pstmt.setString(2, supplierdto.getBankAccount());
            pstmt.setBoolean(3, supplierdto.getFixed_delivery_days());
            pstmt.setString(4, supplierdto.getDaySup().toString());
            pstmt.setString(5, supplierdto.getId());
            pstmt.executeUpdate();
            mapper.put(supplierdto.getId(), supplierdto);
        }

    }


    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            mapper.remove(id);
        }
    }

    public HashMap<String, SupplierDTO> getAll() throws SQLException {
        HashMap<String, SupplierDTO> allSuppliers = new HashMap<>();
        String sql = "SELECT * FROM Suppliers";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String supplierId = rs.getString("id");
                String name = rs.getString("name");
                String bankAccount = rs.getString("bank_account");
                boolean fixedDeliveryDays = rs.getBoolean("fixed_delivery_days");
                String deliveryDaysStr = rs.getString("delivery_days");

                List<ContactDTO> contacts = new ArrayList<>( ContactDAO.getInstance().getContactsBySupplier(supplierId).values());
                DaysDTO deliveryDays = (deliveryDaysStr != null) ? DaysDTO.fromString(deliveryDaysStr) : null;

                SupplierDTO supplier = new SupplierDTO(
                        supplierId,
                        name,
                        bankAccount,
                        contacts,
                        deliveryDays
                );
                supplier.setFixed_delivery_days(fixedDeliveryDays);

                allSuppliers.put(supplierId, supplier);

            }
            mapper=allSuppliers;
        } catch (SQLException e) {
            System.err.println("[Error] Failed to fetch all suppliers: " + e.getMessage());
            throw e;
        }

        return allSuppliers;
    }

    public void deleteAll() {
        String sql = "DELETE FROM Suppliers";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
            mapper.clear(); // Clear the local in-memory cache
        } catch (SQLException e) {
            System.err.println("[Error] Failed to delete all suppliers: " + e.getMessage());
        }
    }
    public HashMap<String, SupplierDTO> get_all_supplier_for_today(DaysDTO day) throws Exception {
        HashMap<String, SupplierDTO> result = new HashMap<>();
        String sql = "SELECT * FROM Suppliers WHERE fixed_delivery_days = TRUE AND delivery_days LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + day.name() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String supplierId = rs.getString("id");
                    String name = rs.getString("name");
                    String bankAccount = rs.getString("bank_account");
                    boolean fixedDeliveryDays = rs.getBoolean("fixed_delivery_days");
                    String deliveryDaysStr = rs.getString("delivery_days");

                    List<ContactDTO> contacts = new ArrayList<>(
                            ContactDAO.getInstance().getContactsBySupplier(supplierId).values()
                    );

                    DaysDTO deliveryDays = (deliveryDaysStr != null) ? DaysDTO.fromString(deliveryDaysStr) : null;

                    SupplierDTO supplier = new SupplierDTO(
                            supplierId,
                            name,
                            bankAccount,
                            contacts,
                            deliveryDays
                    );
                    supplier.setFixed_delivery_days(fixedDeliveryDays);
                    result.put(supplierId, supplier);
                }
            }

        } catch (SQLException e) {
            throw new Exception("[Error] Failed to fetch suppliers for day " + day + ": " + e.getMessage());
        }

        return result;
    }
}
