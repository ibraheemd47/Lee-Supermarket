package domainLayer.Supplier;

import ObjectDTO.Supplier.AgreementDTO;
import ObjectDTO.Supplier.ContactDTO;
import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.SupplierDTO;

import java.util.*;

public class DatabaseTest {
    private static Repository repo;

    public static void main(String[] args) {
        try {
            System.out.println("Starting database test...");
            setupDatabase();

            // Test suppliers
            testSuppliers();

            // Test contacts
            testContacts();

            // Test agreements
            testAgreements();

            System.out.println("\nAll tests completed successfully!");

        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private static void setupDatabase() {
        // Delete the database file if it exists
        java.io.File dbFile = new java.io.File("supDB.db");
        if (dbFile.exists()) {
            dbFile.delete();
            System.out.println("Existing database file deleted");
        }

        // Initialize the repository
        repo = RepositoryIMP.getInstance();
        System.out.println("Repository initialized");
    }

    private static void testSuppliers() {
        System.out.println("\n=== Testing Suppliers ===");

        // Create test supplier
        SupplierDTO supplier = new SupplierDTO(
                "SUP001",
                "Test Supplier",
                "123-456-789",
                new ArrayList<>(),
                DaysDTO.SUNDAY
        );
        supplier.setFixed_delivery_days(true);

        // Test insert
        repo.insertSupplier(supplier);

        // Test get
        SupplierDTO retrieved = repo.getSupplierById("SUP001");
        verifySupplier(supplier, retrieved);

        // Test update
        supplier.setName("Updated Supplier");
        repo.updateSupplier(supplier);
        retrieved = repo.getSupplierById("SUP001");
        verifySupplier(supplier, retrieved);

        // Test get all
        HashMap<String, SupplierDTO> allSuppliers = repo.getAllSuppliers();
        System.out.println("Total suppliers: " + allSuppliers.size());

        // Test delete
        repo.deleteSupplier("SUP001");
        retrieved = repo.getSupplierById("SUP001");
        if (retrieved == null) {
            System.out.println("Supplier deleted successfully");
        }
    }

    private static void testContacts() {
        System.out.println("\n=== Testing Contacts ===");

        // Create test supplier and contact
        SupplierDTO supplier = new SupplierDTO(
                "SUP002",
                "Contact Test Supplier",
                "987-654-321",
                new ArrayList<>(),
                DaysDTO.MONDAY
        );
        repo.insertSupplier(supplier);

        ContactDTO contact = new ContactDTO(
                "CON001",
                "John Doe",
                "555-0123",
                "john@example.com"
        );

        // Test insert
        repo.insertContact(contact, "SUP002");

        // Test get contacts by supplier
        HashMap<String, ContactDTO> contacts = repo.getContactsForSupplier("SUP002");
        if (contacts != null && contacts.containsKey("CON001")) {
            System.out.println("Contact retrieved successfully");
        }

        // Test update
        contact.setName("Jane Doe");
        repo.updateContact(contact);

        // Test delete
        repo.deleteContact(contact.getId());
    }

    private static void testAgreements() {
        System.out.println("\n=== Testing Agreements ===");

        // Create test agreement
        AgreementDTO agreement = new AgreementDTO(
                "AGR001",
                "SUP002",
                new HashMap<>(),  // Empty items JSON
                true,
                "NET30"
        );

        // Test insert
        repo.insertAgreement(agreement);

        // Test get
        AgreementDTO retrieved = repo.getAgreementById("AGR001");
        verifyAgreement(agreement, retrieved);

        // Test update
        AgreementDTO updatedAgreement = new AgreementDTO(
                agreement.agreementId(),
                agreement.supplierId(),
                agreement.items(),
                agreement.supplierDeliverIt(),
                "NET60"
        );
        repo.updateAgreement(updatedAgreement);

        // Test get by supplier
        HashMap<String, AgreementDTO> supplierAgreements = repo.getAgreementsBySupplierId("SUP002");
        System.out.println("Supplier agreements: " + supplierAgreements.size());

        // Test delete
        repo.deleteAgreement("AGR001");
    }

    private static void verifySupplier(SupplierDTO expected, SupplierDTO actual) {
        if (actual != null &&
                expected.getId().equals(actual.getId()) &&
                expected.getName().equals(actual.getName()) &&
                expected.getBankAccount().equals(actual.getBankAccount())) {
            System.out.println("Supplier verification successful");
        } else {
            System.out.println("Supplier verification failed");
        }
    }

    private static void verifyAgreement(AgreementDTO expected, AgreementDTO actual) {
        if (actual != null &&
                expected.agreementId().equals(actual.agreementId()) &&
                expected.supplierId().equals(actual.supplierId()) &&
                expected.payment().equals(actual.payment())) {
            System.out.println("Agreement verification successful");
        } else {
            System.out.println("Agreement verification failed");
        }
    }

    private static void cleanup() {
        if (repo != null) {
            repo.close();
            System.out.println("\nDatabase connection closed");
        }
    }
}