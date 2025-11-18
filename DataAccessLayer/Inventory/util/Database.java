package DataAccessLayer.Inventory.util;

import java.nio.file.Paths;
import java.sql.*;
public final class Database {
    // point this at your file or JDBC URL
   // private static final String DB_URL = "jdbc:sqlite:C:\\Users\\mohamad\\ADSS_Group_B\\Dev\\inventoryDB.db";
    static String path = Paths.get("").toAbsolutePath().toString();
    static String DB_URL = "jdbc:sqlite:inventoryDB.db";
    private static Connection conn;


    static {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
            //System.out.println(conn == null);
            if (conn == null) {
                throw new SQLException("Connection to database failed (conn is null). Check DB_URL: " + DB_URL);
            }
            try (Statement st = conn.createStatement()) {
                //
                // 1) categories
                //
                // According to the final inventoryDB screenshot, "categories" has:
                //   id          INTEGER (auto increment, primary key)
                //   name        TEXT
                //   quantity    INTEGER
                //   value       INTEGER
                //   parent_id   INTEGER = NULL  (no explicit FK in screenshot, so we treat as a nullable integer)
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS categories (
          id        INTEGER PRIMARY KEY AUTOINCREMENT,
          name      TEXT    NOT NULL,
          quantity  INTEGER NOT NULL,
          value     INTEGER NOT NULL,
          parent_id INTEGER              -- nullable, if you later want a self‐FK: FOREIGN KEY(parent_id) REFERENCES categories(id)
        );
    """);

                //
                // 2) discounts
                //
                // From the "discounts" screenshot:
                //   id                  INTEGER (primary key)
                //   name                TEXT
                //   type                TEXT
                //   discount_percentage REAL
                //   start_date          TEXT
                //   end_date            TEXT
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS discounts (
          id                  INTEGER PRIMARY KEY AUTOINCREMENT,
          name                TEXT    NOT NULL,
          type                TEXT    NOT NULL,
          discount_percentage REAL    NOT NULL,
          start_date          TEXT    NOT NULL,
          end_date            TEXT    NOT NULL
        );
    """);

                //
                // 3) items
                //
                // From the "items" screenshot (id is INTEGER PK, NOT shown with AUTOINCREMENT, but "INTEGER PRIMARY KEY" in SQLite already auto‐increments):
                //   id                  INTEGER PRIMARY KEY
                //   name                TEXT    NOT NULL
                //   category_id         INTEGER
                //   expiration_date     TEXT
                //   quantity_warehouse  INTEGER
                //   quantity_store      INTEGER
                //   supplier            TEXT
                //   manufacturer        TEXT
                //   buying_price        REAL
                //   selling_price       REAL
                //   demand              INTEGER
                //   aisle_store         TEXT
                //   shelf_store         TEXT
                //   aisle_warehouse     TEXT
                //   shelf_warehouse     TEXT
                //   (foreign key: category_id → categories(id))
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS items (
          id                 INTEGER PRIMARY KEY,
          name               TEXT    NOT NULL,
          category_id        INTEGER,
          expiration_date    TEXT,
          quantity_warehouse INTEGER,
          quantity_store     INTEGER,
          supplier           TEXT,
          manufacturer       TEXT,
          buying_price       REAL,
          selling_price      REAL,
          demand             INTEGER,
          aisle_store        TEXT,
          shelf_store        TEXT,
          aisle_warehouse    TEXT,
          shelf_warehouse    TEXT,
          FOREIGN KEY(category_id) REFERENCES categories(id)
        );
    """);

                //
                // 4) price_history
                //
                // From the "price_history" screenshot:
                //   id             INTEGER (auto increment, PK)
                //   item_id        INTEGER NOT NULL
                //   selling_price  REAL    NOT NULL
                //   buying_price   REAL    NOT NULL
                //   date           TEXT    NOT NULL
                //   (foreign key: item_id → items(id))
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS price_history (
          id             INTEGER PRIMARY KEY AUTOINCREMENT,
          item_id        INTEGER NOT NULL,
          selling_price  REAL    NOT NULL,
          buying_price   REAL    NOT NULL,
          date           TEXT    NOT NULL,
          FOREIGN KEY(item_id) REFERENCES items(id)
        );
    """);

                //
                // 5) alerts
                //
                // From the final “inventoryDB → alerts” screenshot:
                //   id           INTEGER (auto increment, PK)
                //   message      TEXT    NOT NULL
                //   type         TEXT    NOT NULL
                //   date_created TEXT    NOT NULL
                //   item_id      INTEGER NOT NULL
                //   (foreign key: item_id → items(id))
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS alerts (
          id           INTEGER PRIMARY KEY AUTOINCREMENT,
          message      TEXT    NOT NULL,
          type         TEXT    NOT NULL,
          date_created TEXT    NOT NULL,
          item_id      INTEGER NOT NULL,
          FOREIGN KEY(item_id) REFERENCES items(id)
        );
    """);

                //
                // 6) item_discounts
                //
                // From the "item_discounts" screenshot:
                //   discount_id INTEGER NOT NULL
                //   item_id     INTEGER NOT NULL
                //   active      INTEGER       -- 0 or 1, indicates whether this discount is currently active
                //   PRIMARY KEY(discount_id, item_id)
                //   (foreign key: discount_id → discounts(id))
                //   (foreign key: item_id     → items(id))
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS item_discounts (
          discount_id INTEGER NOT NULL,
          item_id     INTEGER NOT NULL,
          active      INTEGER NOT NULL DEFAULT 1,
          PRIMARY KEY(discount_id, item_id),
          FOREIGN KEY(discount_id) REFERENCES discounts(id),
          FOREIGN KEY(item_id)     REFERENCES items(id)
        );
    """);

                //
                // 7) category_discounts
                //
                // From the "category_discounts" screenshot:
                //   discount_id INTEGER NOT NULL
                //   category_id INTEGER NOT NULL
                //   active      INTEGER       -- 0 or 1, indicates whether this discount is currently active
                //   PRIMARY KEY(discount_id, category_id)
                //   (foreign key: discount_id → discounts(id))
                //   (foreign key: category_id → categories(id))
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS category_discounts (
          discount_id INTEGER NOT NULL,
          category_id INTEGER NOT NULL,
          active      INTEGER NOT NULL DEFAULT 1,
          PRIMARY KEY(discount_id, category_id),
          FOREIGN KEY(discount_id) REFERENCES discounts(id),
          FOREIGN KEY(category_id) REFERENCES categories(id)
        );
    """);

                //
                // 8) category_item
                //
                // From the "category_item" screenshot:
                //   category_id INTEGER NOT NULL
                //   item_id     INTEGER NOT NULL
                //   PRIMARY KEY(category_id, item_id)
                //   (foreign key: category_id → categories(id))
                //   (foreign key: item_id     → items(id))
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS category_item (
          category_id INTEGER NOT NULL,
          item_id     INTEGER NOT NULL,
          PRIMARY KEY(category_id, item_id),
          FOREIGN KEY(category_id) REFERENCES categories(id),
          FOREIGN KEY(item_id)     REFERENCES items(id)
        );
    """);

                //
                // 9) supplier_item
                //
                // From the "supplier_item" screenshot:
                //   supplier_id TEXT    NOT NULL
                //   item_id     INTEGER NOT NULL
                //   PRIMARY KEY(item_id, supplier_id)
                //   (foreign key: — the screenshot shows a foreign key on supplier_id but the “suppliers” table is not in our images.
                //    If you have a suppliers table, you can add "FOREIGN KEY(supplier_id) REFERENCES suppliers(id)".)
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS supplier_item (
          supplier_id TEXT    NOT NULL,
          item_id     INTEGER NOT NULL,
          PRIMARY KEY(item_id, supplier_id),
          FOREIGN KEY(item_id)     REFERENCES items(id)
          -- If you have a suppliers table, uncomment the next line:
          -- FOREIGN KEY(supplier_id) REFERENCES suppliers(id)
        );
    """);

                //
                // 10) reports
                //
                // From the "price_history & reports" screenshot:
                //   reports:
                //     id          INTEGER (auto increment, PK)
                //     name        TEXT    NOT NULL
                //     description TEXT
                //     start_date  TEXT    NOT NULL
                //     end_date    TEXT    NOT NULL
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS reports (
          id          INTEGER PRIMARY KEY AUTOINCREMENT,
          name        TEXT    NOT NULL,
          description TEXT,
          start_date  TEXT    NOT NULL,
          end_date    TEXT    NOT NULL
        );
    """);

                //
                // 11) category_reports
                //
                // From the "category_reports" screenshot:
                //   report_id   INTEGER NOT NULL
                //   category_id INTEGER NOT NULL
                //   PRIMARY KEY(report_id, category_id)
                //   FOREIGN KEY(report_id)   REFERENCES reports(id)
                //   FOREIGN KEY(category_id) REFERENCES categories(id)
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS category_reports (
          report_id   INTEGER NOT NULL,
          category_id INTEGER NOT NULL,
          PRIMARY KEY(report_id, category_id),
          FOREIGN KEY(report_id)   REFERENCES reports(id),
          FOREIGN KEY(category_id) REFERENCES categories(id)
        );
    """);

                //
                // 12) item_reports
                //
                // From the "item_reports" screenshot:
                //   report_id          INTEGER (PK, and also FK to reports.id)
                //   item_id            INTEGER NOT NULL (FK → items(id))
                //   defective_quantity INTEGER NOT NULL
                //
                // In SQLite, an "INTEGER PRIMARY KEY" column is automatically AUTOINCREMENT unless you explicitly
                // add the AUTOINCREMENT keyword. The screenshot only shows "id" → "report_id" as the key. We’ll
                // leave it as "INTEGER PRIMARY KEY" (no AUTOINCREMENT flag) to match exactly what was shown.
                //
                st.executeUpdate("""
        CREATE TABLE IF NOT EXISTS item_reports (
          report_id          INTEGER PRIMARY KEY,
          item_id            INTEGER NOT NULL,
          defective_quantity INTEGER NOT NULL,
          FOREIGN KEY(report_id) REFERENCES reports(id),
          FOREIGN KEY(item_id)   REFERENCES items(id)
        );
    """);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Database() {}

    /** All DAOs call this to share the same Connection. */
    public static Connection getConnection() {
        return conn;
    }

}
