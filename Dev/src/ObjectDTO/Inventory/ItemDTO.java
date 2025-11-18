package ObjectDTO.Inventory;


import java.time.Instant;
import java.util.Date;

public record ItemDTO(Integer id, String name, Integer category_Id , Instant expirationDate, Integer quantityInWarehouse, Integer quantityInStore, String supplier, String manufacturer , double buyingPrice, double sellingPrice, Integer demand, String aisle_store, String shelf_store, String aisle_warehouse, String shelf_warehouse) {}
