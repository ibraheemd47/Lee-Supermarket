package DataAccessLayer.Supplier;

import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.FixedOrderDTO;
import ObjectDTO.Supplier.OrderDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FixedOrderDAO {
    private static FixedOrderDAO instance = null;
    private final Connection connection;
    private HashMap<String, FixedOrderDTO> mapper;

    private FixedOrderDAO() {
        this.connection = ConnectionManager.getConnection();
        mapper = new HashMap<>();
    }

    public static FixedOrderDAO getInstance() {
        if (instance == null)
            instance = new FixedOrderDAO();
        return instance;
    }

    /**
     * Inserts a fixed order into both the Orders and FixedOrder tables.
     *
     * @param fixedO the fixed order
     * @param order  the base order
     * @throws SQLException if a database error occurs
     */
    public void insert(FixedOrderDTO fixedO, OrderDTO order) throws SQLException {
        if (OrderDAO.getInstance().getOrderById(order.getOrder_id()) == null)
            OrderDAO.getInstance().insert_order(order, true);//add to the order table
        String sql = "INSERT INTO FixedOrder (order_id, arrival_day) VALUES (?, ?)";
        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fixedO.getOrder_id());
            stmt.setString(2, fixedO.getArrival_day().toString());
            stmt.executeUpdate();
            mapper.put(fixedO.getOrder_id(), fixedO);
        }

    }

    /**
     * Updates the arrival day of a fixed order.
     */
    public boolean updateArrivalDay(String orderId, DaysDTO newDay) throws SQLException {
        String sql = "UPDATE FixedOrder SET arrival_day = ? WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newDay.toString());
            stmt.setString(2, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a fixed order by ID.
     */
    public boolean delete(String orderId) throws SQLException {
        mapper.remove(orderId);
        String sql = "DELETE FROM FixedOrder WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            return stmt.executeUpdate() > 0;
        }

    }

    /**
     * Retrieves a fixed order by its ID.
     */
    public FixedOrderDTO getByOrderId(String orderId) throws SQLException {
        //try to get from the mapper if exist
        if (mapper.containsKey(orderId)) {
            return mapper.get(orderId);
        } else {
            OrderDTO order = OrderDAO.getInstance().getOrderById(orderId);
            //if not exist then get from the database
            String sql = "SELECT * FROM FixedOrder WHERE order_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, orderId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    DaysDTO arriveDay = rs.getString("arrival_day") == null ? null : DaysDTO.valueOf(rs.getString("arrival_day").toUpperCase());
                    FixedOrderDTO dixOrder = new FixedOrderDTO(order, arriveDay);
                    mapper.put(orderId, dixOrder);
                    return dixOrder;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves all fixed orders.
     */
    public List<FixedOrderDTO> getAll() throws SQLException {
        String sql = "SELECT * FROM FixedOrder";
        List<FixedOrderDTO> list = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {


            while (rs.next()) {
                String orderId = rs.getString("order_id");
                OrderDTO order = OrderDAO.getInstance().getOrderById(orderId);
                list.add(new FixedOrderDTO(
                        order,
                        DaysDTO.valueOf(rs.getString("arrival_day").toUpperCase())

                ));
            }
        }
        //insert all the list to mapper
        for (var x : list) {
            mapper.put(x.getOrder_id(), x);
        }

        return list;
    }


    /**
     * return all the order that today the day should delivered
     * @param daysDTO today
     * @return list of order that should be delivered today
     * @throws SQLException
     */
    public List<OrderDTO> get_all_order_for_today(DaysDTO daysDTO) throws SQLException {
        List<OrderDTO> orderForToday = new ArrayList<>();
        String sql = "SELECT * FROM FixedOrder WHERE arrival_day = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, daysDTO.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                //DaysDTO arriveDay=rs.getString("arrival_day")==null?null:DaysDTO.valueOf(rs.getString("arrival_day").toUpperCase());
                OrderDTO order = OrderDAO.getInstance().getOrderById(rs.getString("order_id"));
                //System.out.println("[get all fixed dao]get the order "+order);

                FixedOrderDTO dixOrder = new FixedOrderDTO(order, daysDTO);
                orderForToday.add(order);
                mapper.put(order.getOrder_id(), dixOrder);

            }
        }
        return orderForToday;
    }
}
