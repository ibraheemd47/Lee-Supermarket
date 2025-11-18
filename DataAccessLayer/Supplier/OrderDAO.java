package DataAccessLayer.Supplier;
import ObjectDTO.Supplier.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OrderDAO {
    private static OrderDAO instance = null;
    private Connection connection;
    private HashMap<String, OrderDTO> orders;//<order id:String , order>

    private OrderDAO() {
        this.connection = ConnectionManager.getConnection();
        orders =new HashMap<>();
    }

    public static OrderDAO getInstance() {
        if (instance == null) {
            instance = new OrderDAO();
        }
        return instance;
    }
    public void insert_order(OrderDTO toInsertOrder,boolean isFIxed) throws SQLException {
        //System.out.println("[inserting to db ]"+toInsertOrder);
        String sql = "INSERT INTO Orders (order_id, supplier_name, supplier_id, address, contact_number, is_fixed, order_price, order_date , STATUS ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, (toInsertOrder.getOrder_id()));
            pstmt.setString(2, toInsertOrder.getSupplier_name());
            pstmt.setString(3, toInsertOrder.getSupplier_id());
            pstmt.setString(4, toInsertOrder.getAddress());
            pstmt.setString(5, toInsertOrder.getContact() == null ? null : toInsertOrder.getContact().getPhoneNumber());
            pstmt.setBoolean(6,isFIxed);
            pstmt.setDouble(7, toInsertOrder.getOrder_price());

            pstmt.setDate(8, java.sql.Date.valueOf(toInsertOrder.getDate()));
            pstmt.setString(9, toInsertOrder.getStatus().toString());

            pstmt.executeUpdate();

            // Optionally cache the order locally
            orders.put(toInsertOrder.getOrder_id(), toInsertOrder);
        }
    }

    public OrderDTO getOrderById(String orderId) throws SQLException {
        // Check cache first
        if (orders.containsKey(orderId)) {
            return orders.get(orderId);
        }

        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                //int order_id=rs.getInt("order_id");
                boolean isFixedOrder=rs.getBoolean("is_fixed");
                List<ItemDTO> itemslst=Item_OrderDAO.getInstance().getItems(orderId);
                ContactDTO cont=ContactDAO.getInstance().getByphone(rs.getString("contact_number"));
                OrderDTO order = new OrderDTO(
                        orderId,
                        rs.getString("supplier_name"),
                        rs.getString("supplier_id"),
                        itemslst,
                        cont,
                        rs.getDouble("order_price"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getString("address"),

                         StatusDTO.valueOf(rs.getString("STATUS"))
                );
                if(isFixedOrder){
                    //FixedOrderDTO fixed=new FixedOrderDTO(order_id,)
                }
                //todo : check what to do if fixed
                orders.put(orderId, order); // Cache it
                return order;
            } else {
                return null;
            }
        }
    }


    public boolean deleteOrder(String orderId) throws SQLException {
        String sql = "DELETE FROM Orders WHERE order_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            int rowsAffected = pstmt.executeUpdate();
            orders.remove(orderId); // Remove from cache
            return rowsAffected > 0;
        }
    }

    public boolean updateOrder(OrderDTO order,boolean isFixed) throws SQLException {
        String sql = "UPDATE Orders SET supplier_name = ?, supplier_id = ?, address = ?, contact_number = ?, is_fixed = ?, order_price = ?, order_date = ? , STATUS = ? WHERE order_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, order.getSupplier_name());
            pstmt.setString(2, order.getSupplier_id());
            pstmt.setString(3, order.getAddress());
            pstmt.setString(4, order.getContact()==null?null:order.getContact().getPhoneNumber() );
            pstmt.setBoolean(5, isFixed);
            pstmt.setDouble(6, order.getOrder_price());
            pstmt.setDate(7, order.getDate() == null ? null : java.sql.Date.valueOf(order.getDate()) );
            pstmt.setString(8, order.getStatus().toString());
            pstmt.setString(9, order.getOrder_id());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                orders.put(order.getOrder_id(), order); // Update cache
                return true;
            }
            return false;
        }
    }


    public List<OrderDTO> getAll() throws SQLException {
        List<OrderDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM Orders";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String orderId = rs.getString("order_id");

                // בדיקה אם כבר בקאש
                OrderDTO cachedOrder = orders.get(orderId);
                if (cachedOrder != null) {
                    result.add(cachedOrder);
                    continue;
                }

                boolean isFixedOrder = rs.getBoolean("is_fixed");
                List<ItemDTO> itemslst = Item_OrderDAO.getInstance().getItems(orderId);
                ContactDTO contact = ContactDAO.getInstance().getByphone(rs.getString("contact_number"));

                OrderDTO order = new OrderDTO(
                        orderId,
                        rs.getString("supplier_name"),
                        rs.getString("supplier_id"),
                        itemslst,
                        contact,
                        rs.getDouble("order_price"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getString("address"),
                        StatusDTO.valueOf(rs.getString("STATUS"))
                );

                result.add(order);
                orders.put(orderId, order);
            }

        }

        return result;
    }

}
