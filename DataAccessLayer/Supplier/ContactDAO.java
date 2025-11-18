package DataAccessLayer.Supplier;

//import DataAccessLayer.RepositoryIMP;
import ObjectDTO.Supplier.ContactDTO;

import java.sql.*;
import java.util.HashMap;

public class ContactDAO {
    private static ContactDAO instance = null;
    private Connection connection;
    private HashMap<String, HashMap<String,ContactDTO>> mapper;//<supplier_id :string , contact:HashMap< contact_id :String ,ContactDTO>>

    private ContactDAO() {
        this.connection = ConnectionManager.getConnection();
        if (this.connection == null) {
            throw new RuntimeException("Failed to establish database connection");
        }
        this.mapper = loadData();
    }

    public static ContactDAO getInstance() {
        if (instance == null) {
            instance = new ContactDAO();
        }
        return instance;
    }

    public void insert(ContactDTO contactDTO, String supplierId) throws SQLException {
        String sql = "INSERT INTO Contacts (id, supplierId,name, email,phone_number) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, contactDTO.getId());
            pstmt.setString(3, contactDTO.getName());
            pstmt.setString(5, contactDTO.getPhoneNumber());
            pstmt.setString(4, contactDTO.getEmail());
            pstmt.setString(2, supplierId);
            pstmt.executeUpdate();
            // Initialize the inner HashMap if it doesn't exist
            mapper.computeIfAbsent(supplierId, k -> new HashMap<>())
                    .put(contactDTO.getId(), contactDTO);
//get a supplier contact list  =>  add to list
        }
    }

    public HashMap<String,ContactDTO> get(String supplierId) throws SQLException {
        return mapper.getOrDefault(supplierId, new HashMap<>());
    }

    public void update(ContactDTO contactDTO) throws SQLException {
        String sql = "UPDATE contacts SET name = ?, phone_number = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, contactDTO.getName());
            pstmt.setString(2, contactDTO.getPhoneNumber());
            pstmt.setString(3, contactDTO.getEmail());
            pstmt.setString(4, contactDTO.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(String contact_id_to_delete) throws SQLException {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, contact_id_to_delete);
            pstmt.executeUpdate();
            mapper.forEach((supplier_id, contactDTO_map) ->contactDTO_map.remove(contact_id_to_delete) );
        }
    }

    private HashMap<String,ContactDTO> get_contact_For_Supplier_db(String supplierId) throws SQLException {
        HashMap<String,ContactDTO> contacts = new HashMap<>();
        String sql = "SELECT * FROM contacts WHERE supplierId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplierId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ContactDTO conDTO=mapToContact(rs);
                contacts.put(conDTO.getId(),conDTO);
            }
        }
        return contacts;
    }

    private ContactDTO mapToContact(ResultSet rs) throws SQLException {
        return new ContactDTO(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("phone_number"),
                rs.getString("email")
        );
    }

    /**
     * Retrieves contacts associated with a specific supplier.
     *
     * @param supplierId The unique identifier of the supplier
     * @return Optional containing list of contacts for the supplier, empty Optional if supplier not found
     * @throws IllegalArgumentException if supplierId is null or empty
     */
    public HashMap<String,ContactDTO> getContactsBySupplier(final String supplierId) throws SQLException {
        HashMap<String,ContactDTO> toReturnMap=null;
         toReturnMap=mapper.getOrDefault(supplierId,null);//return the lst of contact dto of supplier that id = id or null if the supp,lier not exist
        if(toReturnMap==null){

                toReturnMap= get_contact_For_Supplier_db(supplierId);
                if(!toReturnMap.isEmpty()){
                    mapper.put(supplierId,toReturnMap);
                }


        }
        return toReturnMap;
    }

    private HashMap<String, HashMap<String,ContactDTO>> loadData() {
        HashMap<String, HashMap<String,ContactDTO>> contacts = new HashMap<>();
        String sql = "SELECT * FROM Contacts";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String supplierId = rs.getString("SupplierId");
                ContactDTO contact =mapToContact(rs);

//                        new ContactDTO(
//                        rs.getString("Id"),
//                        rs.getString("Name"),
//                        rs.getString("Phone"),
//                        rs.getString("Email")
//                );
                String contact_id_to_add=contact.getId()+"";
                contacts.computeIfAbsent(supplierId, k -> new HashMap<>()).put(contact_id_to_add,contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public ContactDTO getByphone(String contactNumber) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE phone_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, contactNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ContactDTO conDTO=mapToContact(rs);
                return conDTO;
            }
        }
        return null;
    }
}
