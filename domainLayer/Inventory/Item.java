package domainLayer.Inventory;

import DataAccessLayer.Inventory.DiscountDao;
import DataAccessLayer.Inventory.ItemDao;
import DataAccessLayer.Inventory.JdbcDiscountDAO;
import DataAccessLayer.Inventory.JdbcItemDAO;
import ObjectDTO.Inventory.ItemDTO;
import ObjectDTO.Inventory.PriceHistoryEntryDTO;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class Item {
    static int itemIdCount ; // Static counter to generate unique IDs
    int id;
    String name;
    int MinThreshold;
    Date ExpirationDate;
    int Quantity;
    Location LocationAtStore;
    Location LocationAtWarehouse;
    String supplier;
    String manufacturer;
    double BuyingPrice;
    double SellingPrice;
    int quantityInWarehouse;
    int quantityInStore;
    Category Category;
    List<String> supplierIds = new ArrayList<>(); // List of supplier IDs associated with the item
    Map<Integer,Discount> discountsHistory = new HashMap<>(); // List of discounts associated with the item
    Map<Integer,Discount> activeDiscounts = new HashMap<>(); // List of active discounts associated with the item 
    public Map<Integer,Discount> getDiscountsHistory() {
        return this.discountsHistory;
    }
    public void setActiveDiscounts(Map<Integer, Discount> activeDiscounts) {
        this.activeDiscounts = activeDiscounts;
    }
    public void setDiscountsHistory(Map<Integer, Discount> discountsHistory) {
        this.discountsHistory = discountsHistory;
    }
    List<PriceHistoryEntry> priceHistory = new ArrayList<>(); // List of price history entries
    private final int DEFAULT_DELIVERY_DAYS = 3;
    private final ItemDao dao = new JdbcItemDAO() ;
    private int Demand = 10 ;// the average selling each day for an item, default is 10
    public int getDemand() {
        return Demand;
    }
    DiscountDao disDao = new JdbcDiscountDAO();
    public Item(String name, Category category , Date expirationDate, int quantityInWarehouse, int quantityInStore, String supplier,String manufacturer ,double buyingPrice, double sellingPrice, int demand , Location locationAtStore, Location locationAtWarehouse) {

        int currId = this.current_id_count(); // Increment the item count and assign it as the ID
        this.id = currId++;
        this.name = name;
        this.Demand = demand; // Initialize the demand
        this.MinThreshold = DEFAULT_DELIVERY_DAYS*Demand + Demand;//added one more demand to be on the save side in case if in a day we sell more than expected,delivery day is 3 by default
        this.ExpirationDate = expirationDate;
        this.Quantity = quantityInStore+quantityInWarehouse; // Total quantity in both warehouse and store
        this.LocationAtStore = locationAtStore; // Initialize the location at store
        this.LocationAtWarehouse = locationAtWarehouse; // Initialize the location at warehouse
        this.supplier = supplier; //supplier we are currently working with
        this.BuyingPrice = buyingPrice;
        this.Category = category; // Initialize the category
        this.quantityInWarehouse = quantityInWarehouse; // Initialize to 0 or any other default value
        this.quantityInStore = quantityInStore; // Initialize to 0 or any other default value
        this.SellingPrice = sellingPrice;
        this.manufacturer = manufacturer;
        this.supplierIds.add(supplier);
        priceHistory.add(new PriceHistoryEntry(sellingPrice, buyingPrice, new Date())); // Add initial price history entry
        try {
            dao.insertPriceHistory(new PriceHistoryEntryDTO(sellingPrice, buyingPrice, (new Date()).toInstant()), id); // Insert initial price history entry into the database
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public Item(int id,String name, Category category , Date expirationDate, int quantityInWarehouse, int quantityInStore, String supplier,String manufacturer ,double buyingPrice, double sellingPrice, int demand , Location locationAtStore, Location locationAtWarehouse) {
        this.id = id; // Increment the item count and assign it as the ID
        this.name = name;
        this.Demand = demand; // Initialize the demand
        this.MinThreshold = DEFAULT_DELIVERY_DAYS*Demand + Demand;//added one more demand to be on the save side in case if in a day we sell more than expected,delivery day is 3 by default
        this.ExpirationDate = expirationDate;
        this.Quantity = quantityInStore+quantityInWarehouse; // Total quantity in both warehouse and store
        this.LocationAtStore = locationAtStore; // Initialize the location at store
        this.LocationAtWarehouse = locationAtWarehouse; // Initialize the location at warehouse
        this.supplier = supplier;
        this.BuyingPrice = buyingPrice;
        this.Category = category; // Initialize the category
        this.quantityInWarehouse = quantityInWarehouse; // Initialize to 0 or any other default value
        this.quantityInStore = quantityInStore; // Initialize to 0 or any other default value
        this.SellingPrice = sellingPrice;
        this.manufacturer = manufacturer;
        this.supplierIds.add(supplier);
        priceHistory.add(new PriceHistoryEntry(sellingPrice, buyingPrice, new Date())); // Add initial price history entry
        try {
            dao.insertPriceHistory(new PriceHistoryEntryDTO(sellingPrice, buyingPrice, (new Date()).toInstant()), id); // Insert initial price history entry into the database
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    public void addUnit(String Location,int Quantity){
        // Add item to the inventory
        // Update quantityInWarehouse or quantityInStore as needed
        if (Location.equals("Warehouse")) {
            quantityInWarehouse += Quantity;
        } else if (Location.equals("Store")) {
            quantityInStore += Quantity;
        }
        setQuantity(quantityInStore+quantityInWarehouse); // Update total quantity
    }
    public void removeUnit(String Location,int quantity){
        // Remove item from the inventory
        // Update quantityInWarehouse or quantityInStore as needed
        if (Location.equals("Warehouse")) {
            quantityInWarehouse -= quantity;
        } else if (Location.equals("Store")) {
            quantityInStore -= quantity;
        }
        setQuantity(quantityInStore+quantityInWarehouse); // Update total quantity
        
    }
    // to be implemented
   /*  public void calculateMinThreshold() {
        
    }*/
    //Add to update item the location of the item in the store and warehouse

    public void updateItem(Location locationAtStore,Location locationAtWareHouse ,String name,Category category ,Date expirationDate, int quantityInStore,int quantityInWarehouse,String supplier, double buyingPrice,double sellingPrice,int demand) {

        
        this.name = name;

        this.ExpirationDate = expirationDate;
        this.Demand = demand; // Initialize the demand
        this.MinThreshold = demand*DEFAULT_DELIVERY_DAYS+demand;
        this.Category = category; // Initialize the category
        this.quantityInWarehouse = quantityInWarehouse; // Initialize to 0 or any other default value
        this.quantityInStore = quantityInStore; // Initialize to 0 or any other default value
        this.Quantity = quantityInStore+quantityInWarehouse; // Total quantity in both warehouse and store
        this.LocationAtStore = locationAtStore; 
        this.LocationAtWarehouse = locationAtWareHouse; 
        this.supplier = supplier;
        this.setBuyingPrice(buyingPrice); // Set the buying price
        this.setSellingPrice(sellingPrice); // Set the selling price
        
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    
    public int getMinThreshold() {
        return MinThreshold;
    }
    
    public Date getExpirationDate() {
        return ExpirationDate;
    }
    
    public int getQuantity() {
        return Quantity;
    }
    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }
    public List<PriceHistoryEntry> getPriceHistory() {
        return priceHistory;
    }
    public Category getCategory() {
        return Category;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public double getSellingPrice() {
        return SellingPrice;
    }
    public boolean setSellingPrice(double newSellingPrice) {
        if(newSellingPrice < 0) {
            return false; // Invalid selling price
        }
        this.SellingPrice = newSellingPrice;
        priceHistory.add(new PriceHistoryEntry(newSellingPrice, BuyingPrice, new Date()));
        try {
            dao.insertPriceHistory(new PriceHistoryEntryDTO(newSellingPrice, this.BuyingPrice, (new Date()).toInstant()), id); // Insert initial price history entry into the database
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.SellingPrice == newSellingPrice; // Return true if the selling price was set successfully
    }

    public String getLocationAtStore() {
        return "Location At Store: "+"\n"+"Aisle: " + LocationAtStore.getAisle() + "\n" + "Shelf: "+ LocationAtStore.getShelf();
    }
    public String getLocationAtWarehouse() {
        return "Location At Warehouse: "+"\n"+"Aisle: " + LocationAtWarehouse.getAisle() + "\n" + "Shelf: "+ LocationAtWarehouse.getShelf();
    }
    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public double getBuyingPrice() {
        return BuyingPrice;
    }
    public boolean setBuyingPrice(double newBuyingPrice) {
        if(newBuyingPrice < 0) {
            return false; // Invalid buying price
        }
        this.BuyingPrice = newBuyingPrice;
        priceHistory.add(new PriceHistoryEntry(SellingPrice, newBuyingPrice, new Date()));
        try {
            dao.insertPriceHistory(new PriceHistoryEntryDTO(SellingPrice, newBuyingPrice, (new Date()).toInstant()), id); // Insert price history entry into the database
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.BuyingPrice == newBuyingPrice; // Return true if the buying price was set successfully
    }
    public int getQuantityInWarehouse() {
        return quantityInWarehouse;
    }
    
    public int getQuantityInStore() {
        return quantityInStore;
    }
    public Location getLocationAtWarehouseLocationObject() {return LocationAtWarehouse; }
    public Location getLocationAtStoreLocationObject() {return LocationAtStore; }
    // Removed duplicate method setSellingPrice(double SellingPrice)
    public boolean isBelowThreshold(){
        return Quantity < MinThreshold; 
    } 
    public void applyDiscount(Discount discount) {
        if (discount != null) {
          //  System.out.println(discount!=null);
            discount.setOldPrice(SellingPrice); // Store the old price before applying the discount
            this.discountsHistory.put(discount.getId(), discount); // Add the discount to the list of discounts
            this.activeDiscounts.put(discount.getId(), discount); // Add the discount to the list of active discounts
            this.setSellingPrice(SellingPrice - (SellingPrice * discount.getDiscountPercentage() / 100)); // Update the buying price with the discount
            this.priceHistory.add(new PriceHistoryEntry(SellingPrice, BuyingPrice, new Date())); // Add to the price history
            try {
                disDao.insertItemDiscount(id, discount.getId(), 1); // Insert the discount into the database with status 1 (active)
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void removeDiscounts() {
        List<Discount> newActiveDis =sortDiscountsByEndDate();
        for(int i = 0; i < newActiveDis.size(); i++){
            Discount discount = newActiveDis.get(i);
            if(discount.isExpired()){
                this.SellingPrice = discount.getOldPrice(); // Restore the old price
                this.activeDiscounts.remove(discount.getId()); // Remove the expired discount from the list of active discounts
            }
        }
    }
    //add hashmap id to discount.
    public void removeDiscount(int discountId) {
        Discount discount = activeDiscounts.get(discountId); // Get the discount by ID
        if (discount != null) {
            SellingPrice = discount.getOldPrice(); // Restore the old price
            activeDiscounts.remove(discountId); // Remove the discount from the list of active discounts
            try{
                disDao.updateItemDiscountIsActive(id, discountId, 0); // Update the discount status in the database
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<Discount> sortDiscountsByEndDate() {
        List<Discount> ActiveDis = new ArrayList<>(activeDiscounts.values()); // Return a list of active discounts
        ActiveDis.sort(Comparator.comparing(Discount::getEndDate));
        return ActiveDis; // Sort the discounts by end date and return the sorted list
    }
    
    public boolean isExpired(){
        Date today = new Date(); // Current date
        return ExpirationDate.before(today); // Check if ExpirationDate is before today
    }
    // Returns true if ExpirationDate is before today
    public String toString() {
        return "Item ID: " + id + ", Name: " + name + ", Category: " + Category.getName() + ", Expiration Date: " + ExpirationDate + ", Quantity: " + Quantity + ", Location At Store: " + LocationAtStore.getAisle() + ","+ LocationAtStore.getShelf() + ", Location At Warehouse: " + LocationAtWarehouse.getAisle() + ","+ LocationAtWarehouse.getShelf() + ", Supplier: " + supplier + ", Buying Price: " + BuyingPrice + ", Selling Price: " + SellingPrice;
    }
    public Map<Integer,Discount> getActiveDiscounts() {
        return this.activeDiscounts;
    }
    //get current id count from database
    public int current_id_count() {

        try {
            Optional<Integer> maxIdOpt = dao.findMaxId();
         //   System.out.println("maxIdOpt: " + maxIdOpt);
            this.itemIdCount = maxIdOpt.orElse(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int returnedId = itemIdCount+1;
        return returnedId;
    }



    public ItemDTO toDTO() {
        return new ItemDTO(
                this.id,
                this.name,
                this.Category.getId(), // assuming Category has a getId() method
                this.ExpirationDate.toInstant(),
                this.quantityInWarehouse,
                this.quantityInStore,
                this.supplier,
                this.manufacturer,
                this.BuyingPrice,
                this.SellingPrice,
                this.Demand,
                this.LocationAtStore.getAisle(),
                this.LocationAtStore.getShelf(),
                this.LocationAtWarehouse.getAisle(),
                this.LocationAtWarehouse.getShelf()
        );
    }
    public boolean addSupplier(String supplierId) {
        if (!supplierIds.contains(supplierId)) {
            supplierIds.add(supplierId);
            return true; // Supplier added successfully
        }
        return false; // Supplier already exists
    }
    public boolean removeSupplier(String supplierId) {
        if (supplierIds.contains(supplierId)) {
            supplierIds.remove(supplierId);
            return true; // Supplier removed successfully
        }
        return false; // Supplier not found
    }
    public boolean addPriceHistoryEntry(PriceHistoryEntry entry) {
        if (entry != null) {
            priceHistory.add(entry);
            return true; // Price history entry added successfully
        }
        return false; // Invalid entry
    }



 

}