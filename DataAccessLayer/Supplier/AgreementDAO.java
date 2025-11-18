package DataAccessLayer.Supplier;

//import DataAccessLayer.RepositoryIMP;
import ObjectDTO.Supplier.AgreementDTO;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AgreementDAO {
    private static AgreementDAO instance = null;
    private Connection connection;
    private HashMap<String, HashMap<String, AgreementDTO>> mapper; //supplier_id -> (agreement_id -> agreement)
//get
    //o1==o2
    //{mreturn map}
    private AgreementDAO() {
        this.connection = ConnectionManager.getConnection();
        if (this.connection == null) {
            throw new RuntimeException("Failed to establish database connection");
        }
        mapper = loadData();
    }

    public static AgreementDAO getInstance() {
        if (instance == null) {
            instance = new AgreementDAO();
        }
        return instance;
    }

    private HashMap<String, HashMap<String, AgreementDTO>> loadData() {
        HashMap<String, HashMap<String, AgreementDTO>> agreements = new HashMap<>();
        String sql = "SELECT * FROM Agreements";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String agreementId = rs.getString("agreement_id");
                String supplier_id = rs.getString("supplier_id");
                if (!agreements.containsKey(supplier_id)) {
                    agreements.put(supplier_id, new HashMap<>());
                }
                agreements.get(supplier_id).put(agreementId, new AgreementDTO(
                        agreementId,
                        supplier_id,
                        new HashMap<>(),
                        rs.getBoolean("supplier_deliver_it"),
                        rs.getString("payment")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agreements;
    }

    public void insert(AgreementDTO agreementDTO) throws SQLException {
        String sql = "INSERT INTO Agreements (agreement_id, supplier_id, supplier_deliver_it, payment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, agreementDTO.agreementId());
            pstmt.setString(2, agreementDTO.supplierId());
            pstmt.setBoolean(3, agreementDTO.supplierDeliverIt());
            pstmt.setString(4, agreementDTO.payment());
            pstmt.executeUpdate();
            if (!mapper.containsKey(agreementDTO.supplierId())) {
                mapper.put(agreementDTO.supplierId(), new HashMap<>());
            }
            mapper.get(agreementDTO.supplierId()).put(agreementDTO.agreementId(), agreementDTO);
        }
    }


    public AgreementDTO get(String id) throws SQLException {
        // First try to find in mapper
        for (HashMap<String, AgreementDTO> supplierAgreements : mapper.values()) {
            AgreementDTO agreement = supplierAgreements.get(id);
            if (agreement != null) {
                return agreement;
            }
        }
        // If not found in mapper, try to get from DB
        return get_from_the_DB(id);
    }

    private AgreementDTO get_from_the_DB(String id) {
        String sql = "SELECT * FROM Agreements WHERE agreement_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String supplierId = rs.getString("supplier_id");
                AgreementDTO agreementDTO = new AgreementDTO(
                        rs.getString("agreement_id"),
                        supplierId,
                        new HashMap<>(),
                        rs.getBoolean("supplier_deliver_it"),
                        rs.getString("payment"));
                if (!mapper.containsKey(supplierId)) {
                    mapper.put(supplierId, new HashMap<>());
                }
                mapper.get(supplierId).put(id, agreementDTO);
                return agreementDTO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(AgreementDTO agreementDTO) throws SQLException {
        String sql = "UPDATE Agreements SET supplier_id = ?, supplier_deliver_it = ?, payment = ? WHERE agreement_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, agreementDTO.supplierId());
            pstmt.setBoolean(2, agreementDTO.supplierDeliverIt());
            pstmt.setString(3, agreementDTO.payment());
            pstmt.setString(4, agreementDTO.agreementId());

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("No agreement found with id: " + agreementDTO.agreementId());
            }

            //  update the mapper
            mapper.computeIfAbsent(agreementDTO.supplierId(), k -> new HashMap<>())
                    .put(agreementDTO.agreementId(), agreementDTO);
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM Agreements WHERE agreement_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            mapper.forEach((supId, mapAgreement) -> mapAgreement.remove(id));//iterate all the hash and for supplier that have this id delete it
        }
    }

    public HashMap<String, HashMap<String, AgreementDTO>> getAllInstance() throws SQLException {
        return mapper;
    }


    public HashMap<String, AgreementDTO> getAllAgreement() throws SQLException {
        HashMap<String, AgreementDTO> allAgreement = new HashMap<>();
        String sql = "SELECT * FROM Agreements";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String agreement_id=rs.getString("agreement_id");
                HashMap items=SupItemDAO.getInstance().getByAgreementId(agreement_id);
                AgreementDTO agreementDTO = new AgreementDTO(
                        agreement_id,
                        rs.getString("supplier_id"),
                        items,
                        rs.getBoolean("supplier_deliver_it"),
                        rs.getString("payment")
                );
                allAgreement.put(agreementDTO.agreementId(), agreementDTO);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allAgreement;
    }

    public HashMap<String, AgreementDTO> getBySupplier(String supplierId) {
        HashMap<String, AgreementDTO> agreements = new HashMap<>();

        // Check database first
        String sql = "SELECT * FROM Agreements WHERE supplier_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, supplierId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String agreement_id=rs.getString("agreement_id");
                HashMap items=SupItemDAO.getInstance().getByAgreementId(agreement_id);

                AgreementDTO agreementDTO = new AgreementDTO(
                        agreement_id,
                        supplierId,
                        items,
                        rs.getBoolean("supplier_deliver_it"),
                        rs.getString("payment")
                );
                agreements.put(agreementDTO.agreementId(), agreementDTO);
            }

            // Update mapper with database results
            if (!agreements.isEmpty()) {
                if (mapper.get(supplierId) == null)
                    mapper.put(supplierId, new HashMap<>());

                for (Map.Entry<String, AgreementDTO> entry : agreements.entrySet())
                    mapper.get(supplierId).put(entry.getKey(), entry.getValue());

            }
        }catch(SQLException e) {
            e.printStackTrace();
        }

        // Return from mapper if exists
        return agreements.isEmpty() ? mapper.getOrDefault(supplierId, new HashMap<>()) : agreements;
    }

    public void deleteAllAgreements() {
        String sql = "DELETE FROM Agreements";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
            mapper.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
