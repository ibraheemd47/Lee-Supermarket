package domainLayer.Supplier;

import ObjectDTO.Supplier.*;

import java.util.*;

public class test2 {
    private static Repository repo;

    public static void main(String[] args) {
        try {
            System.out.println("Starting database test...");
            setupDatabase();

            // Test suppliers
            testSuppliers();
            printAllSuppliers();

            // Test contacts
            testContacts();
            printAllContacts();

            // Test agreements
            testAgreements();
            printAllAgreements();

            // Add edge cases test
            testEdgeCases();

            System.out.println("\nAll tests completed successfully!");

        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private static void setupDatabase() {
        java.io.File dbFile = new java.io.File("supDB.db");
        if (dbFile.exists()) {
            dbFile.delete();
            System.out.println("Existing database file deleted");
        }
        repo = RepositoryIMP.getInstance();
    }

    private static void printAllSuppliers() {
        System.out.println("\n=== Current Suppliers in Database ===");
        HashMap<String, SupplierDTO> suppliers = repo.getAllSuppliers();

        if (suppliers != null && !suppliers.isEmpty()) {
            System.out.println("| ID | Name | Bank Account | Fixed Delivery | Delivery Days |");
            System.out.println("|----|--------------------|--------------|----------------|--------------|");

            for (SupplierDTO supplier : suppliers.values()) {
                System.out.printf("| %s | %s | %s | %b | %s |\n",
                        supplier.getId(),
                        supplier.getName(),
                        supplier.getBankAccount(),
                        supplier.getFixed_delivery_days(),
                        supplier.getDaySup()
                );
            }
        } else {
            System.out.println("No suppliers found in database.");
        }
        System.out.println();
    }

    private static void printAllContacts() {
        System.out.println("\n=== Current Contacts in Database ===");
        // First get all suppliers to iterate through their contacts
        HashMap<String, SupplierDTO> suppliers = repo.getAllSuppliers();

        if (suppliers != null && !suppliers.isEmpty()) {
            System.out.println("| Contact ID | Name | Phone | Email | Supplier ID | Supplier Name |");
            System.out.println("|------------|------|-------|-------|-------------|---------------|");

            for (SupplierDTO supplier : suppliers.values()) {
                HashMap<String, ContactDTO> contacts = repo.getContactsForSupplier(supplier.getId());
                if (contacts != null) {
                    for (ContactDTO contact : contacts.values()) {
                        System.out.printf("| %s | %s | %s | %s | %s | %s |\n",
                                contact.getId(),
                                contact.getName(),
                                contact.getPhoneNumber(),
                                contact.getEmail(),
                                supplier.getId(),
                                supplier.getName()
                        );
                    }
                }
            }
        } else {
            System.out.println("No contacts found in database.");
        }
        System.out.println();
    }

    private static void printAllAgreements() {
        System.out.println("\n=== Current Agreements in Database ===");
        HashMap<String, AgreementDTO> agreements = repo.getAllAgreements();

        if (agreements != null && !agreements.isEmpty()) {
            System.out.println("| Agreement ID | Supplier ID | Payment Terms | Supplier Delivers |");
            System.out.println("|--------------|-------------|---------------|-------------------|");

            for (AgreementDTO agreement : agreements.values()) {
                System.out.printf("| %s | %s | %s | %b |\n",
                        agreement.agreementId(),
                        agreement.supplierId(),
                        agreement.payment(),
                        agreement.supplierDeliverIt()
                );
            }
        } else {
            System.out.println("No agreements found in database.");
        }
        System.out.println();
    }

    private static void testSuppliers() {
        System.out.println("\n=== Testing Suppliers ===");

        // Create multiple test suppliers
        List<SupplierDTO> testSuppliers = Arrays.asList(
                new SupplierDTO("SUP001", "Tech Solutions", "123-456-789", new ArrayList<>(), DaysDTO.SUNDAY),
                new SupplierDTO("SUP002", "Office Supplies Co", "987-654-321", new ArrayList<>(), DaysDTO.MONDAY),
                new SupplierDTO("SUP003", "Global Imports", "456-789-123", new ArrayList<>(), DaysDTO.WEDNESDAY)
        );

        // Insert all suppliers
        for (SupplierDTO supplier : testSuppliers) {
            supplier.setFixed_delivery_days(true);
            repo.insertSupplier(supplier);
            System.out.printf("Added supplier: %s (%s)\n", supplier.getName(), supplier.getId());
        }

        // Test updates
        SupplierDTO updateSupplier = testSuppliers.get(0);
        updateSupplier.setName("Tech Solutions Updated");
        repo.updateSupplier(updateSupplier);
        System.out.printf("Updated supplier: %s\n", updateSupplier.getId());

        // Print current state
        printAllSuppliers();
        // After update, verify the updated supplier
        SupplierDTO retrievedSupplier = repo.getSupplierById("SUP001");
        System.out.println("Verification after update:");
        System.out.printf("Expected name: %s, Actual name: %s\n",
                "Tech Solutions Updated",
                retrievedSupplier.getName());


        // Delete one supplier
        repo.deleteSupplier("SUP003");
        System.out.println("Deleted supplier: SUP003");

        // After delete, verify supplier was removed
        retrievedSupplier = repo.getSupplierById("SUP003");
        System.out.println("Verification after delete:");
        System.out.printf("Supplier SUP003 exists: %s\n", retrievedSupplier != null);


    }

    private static void testContacts() {
        System.out.println("\n=== Testing Contacts ===");

        // Create two contacts for different suppliers
        ContactDTO contact1 = new ContactDTO("CON001", "John Doe", "555-0123", "john@example.com");
        ContactDTO contact2 = new ContactDTO("CON002", "Jane Smith", "555-0124", "jane@example.com");

        // Add contacts to different suppliers
        repo.insertContact(contact1, "SUP001");
        repo.insertContact(contact2, "SUP002");

        // Verify contacts were added correctly
        HashMap<String, ContactDTO> sup1Contacts = repo.getContactsForSupplier("SUP001");
        HashMap<String, ContactDTO> sup2Contacts = repo.getContactsForSupplier("SUP002");

        System.out.println("Contacts for SUP001: " + sup1Contacts.size());
        System.out.println("Contacts for SUP002: " + sup2Contacts.size());


        // Update contact
        contact1.setName("Jane Doe");
        repo.updateContact(contact1);

        // Delete contact
        repo.deleteContact(contact1.getId());
    }

    private static void testAgreements() {
        System.out.println("\n=== Testing Agreements ===");
        // Create agreement with items
        HashMap<String, SupItemDTO> items = new HashMap<>();
        items.put("Item1", null);
        items.put("Item2", null);

        // Create test agreement
        AgreementDTO agreement = new AgreementDTO(
                "AGR001",
                "SUP001",
                items,
                true,
                "NET30"
        );

        // Insert agreement
        repo.insertAgreement(agreement);
        // Verify items were saved
        AgreementDTO retrieved = repo.getAgreementById("AGR001");
        System.out.println("Number of items in agreement: " + retrieved.items().size());

        // Update agreement
        AgreementDTO updatedAgreement = new AgreementDTO(
                agreement.agreementId(),
                agreement.supplierId(),
                agreement.items(),
                agreement.supplierDeliverIt(),
                "NET60"
        );
        repo.updateAgreement(updatedAgreement);

        // Print agreements for supplier
        HashMap<String, AgreementDTO> supplierAgreements = repo.getAgreementsBySupplierId("SUP001");
        if (supplierAgreements != null) {
            System.out.println("Supplier agreements: " + supplierAgreements.size());
        }

        // Delete agreement
        repo.deleteAgreement("AGR001");
    }

    private static void testEdgeCases() {
        System.out.println("\n=== Testing Edge Cases ===");

        // Try to add supplier with duplicate ID
        SupplierDTO duplicateSupplier = new SupplierDTO(
                "SUP0011",
                "Duplicate Supplier",
                "999-999-999",
                new ArrayList<>(),
                DaysDTO.MONDAY
        );
        repo.insertSupplier(duplicateSupplier);

        // Try to add contact to non-existent supplier
        ContactDTO orphanContact = new ContactDTO(
                "CON003",

                "Orphan Contact",
                "555-0125",
                "orphan@example.com"
        );
        repo.insertContact(orphanContact, "SUP0011");

        // Try to delete non-existent agreement
        repo.deleteContact("SUP0011");
    }
    private static void cleanup() {
        if (repo != null) {
            repo.close();
        }
    }
}