package DataAccessLayer.Supplier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

    public static void initializeTables(Connection conn) {
        try (Statement statement = conn.createStatement()) {
            String createProudctTable = """
                    CREATE TABLE IF NOT EXISTS Products (
                            id TEXT PRIMARY KEY,
                            name TEXT NOT NULL
                    ) """;
            String createManufacturerTable = """
                    CREATE TABLE IF NOT EXISTS Manufacturers (
                            manfacturer_id TEXT PRIMARY KEY,
                            name TEXT NOT NULL
                    ) """;


            String createSuppliersTable = """
                        CREATE TABLE IF NOT EXISTS Suppliers (
                            id TEXT PRIMARY KEY,
                            name TEXT NOT NULL,
                            bank_account TEXT,
                            fixed_delivery_days BOOLEAN,
                            delivery_days TEXT
                        )
                    """;

            String creteSupItemTable = """
                    CREATE TABLE IF NOT EXISTS SupItems (
                        productId TEXT,
                        productName TEXT,
                        supplierId TEXT,
                        agreement_id TEXT,
                        supplierCatNum TEXT,
                        manufacturer TEXT,
                        price REAL,
                        PRIMARY KEY (productId, supplierId,agreement_id)
                    )
                    """;
            //                        FOREIGN KEY (productId) REFERENCES Products(id),
            //                        FOREIGN KEY (supplierId) REFERENCES Suppliers(id),
            //                        FOREIGN KEY (manufacturer) REFERENCES Manufacturers(manfacturer_id)
            //                        ON DELETE CASCADE ON UPDATE CASCADE
            String createDiscountTable = """
                                    CREATE TABLE IF NOT EXISTS Discounts (
                    product_id TEXT NOT NULL,
                    supplier_id TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    discount_percentage REAL NOT NULL,
                    PRIMARY KEY (product_id, supplier_id, quantity)
                                    )
                    """;
            //                    FOREIGN KEY (product_id) REFERENCES Products(id),
            //                    FOREIGN KEY (supplier_id) REFERENCES Suppliers(id)
            String createContactsTable = """
                        CREATE TABLE IF NOT EXISTS Contacts (
                            id TEXT PRIMARY KEY,
                            SupplierId TEXT,
                            Name TEXT NOT NULL,
                            Email TEXT,
                            phone_number TEXT
                        )
                    """;

            String createAgreementsTable = """
                        CREATE TABLE IF NOT EXISTS Agreements (
                            agreement_id TEXT PRIMARY KEY,
                            supplier_id TEXT,
                            supplier_deliver_it BOOLEAN,
                            payment TEXT
                        )
                    """;
            //order_id;
            //me = supplier_name;
            // = supplier_id;
            //= item_list;
            //ddress;
            //ber = contact_number
            // =order_price ;
            //l=till_arrival;
            String createItem_OrderTable= """
                    CREATE TABLE IF NOT EXISTS Item_Order (
                    order_id TEXT,
                    product_id TEXT,
                    supplier_id TEXT,
                    agreement_id TEXT,
                    quantity INTEGER,
                    price REAL,
                    PRIMARY KEY (order_id, product_id, supplier_id, agreement_id)
                    )
                    """;
            String createOrderTable = """
                        CREATE TABLE IF NOT EXISTS Orders (
                            order_id TEXT PRIMARY KEY,
                            supplier_name TEXT,
                            supplier_id TEXT,
                            address TEXT,
                            contact_number TEXT,
                            is_fixed BOOLEAN,
                            order_price REAL,
                            order_date DATE,
                            STATUS TEXT
                        )
                    """;
            String createFixedOrderTable = """
                        CREATE TABLE IF NOT EXISTS FixedOrder (
                            order_id TEXT PRIMARY KEY,
                            arrival_day TEXT
                        )
                    """;
            //,
            //                            FOREIGN KEY (supplier_id) REFERENCES Suppliers(id)

            //drop all
// Drop all tables first (in correct order)
//            try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
//                stmt.executeUpdate("DELETE FROM Discounts");
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
/*            statement.execute("DROP TABLE IF EXISTS Discounts");
            statement.execute("DROP TABLE IF EXISTS Item_Order");
            statement.execute("DROP TABLE IF EXISTS Orders");
            statement.execute("DROP TABLE IF EXISTS FixedOrder");
            statement.execute("DROP TABLE IF EXISTS SupItems");
            statement.execute("DROP TABLE IF EXISTS Agreements");
            statement.execute("DROP TABLE IF EXISTS Contacts");
            statement.execute("DROP TABLE IF EXISTS Suppliers");
            statement.execute("DROP TABLE IF EXISTS Products");
            statement.execute("DROP TABLE IF EXISTS Manufacturers");
*/
          //  System.out.println("Tables dropped successfully");
            statement.execute(createOrderTable);
            statement.execute(createProudctTable );
            statement.execute(createManufacturerTable);
            statement.execute(createSuppliersTable);
            statement.execute(createAgreementsTable);
            statement.execute(creteSupItemTable);
            statement.execute(createDiscountTable);
            statement.execute(createContactsTable);
            statement.execute(createFixedOrderTable);
            statement.execute(createItem_OrderTable);


            System.out.println("Tables initialized successfully!\n if not exist ");

        } catch (SQLException e) {
            System.err.println("Error initializing tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
