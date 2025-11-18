package domainLayer.Inventory;


import DataAccessLayer.Inventory.*;
import ObjectDTO.Inventory.*;
import org.hamcrest.core.Is;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class InventoryManager{
    //it's like a warehouse to store all information about the items in the system
    private static InventoryManager instance;
  
    private LocalDate createdDate ; //date when the inventory was created
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    private HashMap<Integer,Item>  items = new HashMap<>(); //list of items in the inventory

    public HashMap<Integer,Item> getItems() {
        return items;
    }

    public void setItems(HashMap<Integer,Item> items) {
        this.items = items;
    }
    private List<Report> reports; //list of reports for the items in the inventory

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
    private Map<Integer, Report> itemReports; //list of item reports for the items in the inventory

    public Map<Integer, Report> getItemReports() {
        return itemReports;
    }

    public void setItemReports(Map<Integer, Report> itemReports) {
        this.itemReports = itemReports;
    }
    private Map<Integer,Discount> discounts; //list of discounts for the items in the inventory

    public Map<Integer,Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Map<Integer,Discount> discounts) {
        this.discounts = discounts;
    }
    
    public List<Alert> AlertsRecord; //list of alerts for the items in the inventory
    private Map<Integer,Category> categories; //list of categories for the items in the inventory
    public Map<Integer,Category> getCategories() {
        return categories;
    }
    public void setCategories(Map<Integer,Category> categories) {
        this.categories = categories;
    }

    private Location location;
    private Map<Integer,CategoryInventoryReport> categoryReports ;
    private Report report ;
    private final ReportDao reportDao ;
    private final AlertDao alertDao;
    private final CategoryDao categoryDao;
    private final DiscountDao discountDao;
    private final ItemDao itemDao;
    private final SupplierItemDao supplierItemDao ;


    //create the constructor to have list of items in the inventory
    public InventoryManager() {
        this.categories = new HashMap<>(); // Initialize the map of categories
//        this.suppliers = new HashMap<>(); // Initialize the list of suppliers
//        this.manufacturers = new HashMap<>();
        this.reports = new ArrayList<>();
        this.itemReports = new HashMap<Integer, Report>();
        this.categoryReports = new HashMap<>();
        this.discounts = new HashMap<>(); // Initialize the map of discounts
        this.AlertsRecord = new ArrayList<>(); // Initialize the list of alerts
        this.createdDate = LocalDate.now(); // Set the created date to the current date
        reportDao = new JdbcReportDAO(); // Data Access Object for reports
        alertDao = new JdbcAlertDAO(); // Data Access Object for alerts
        categoryDao = new JdbcCategoryDAO(); // Data Access Object for categories
        discountDao = new JdbcDiscountDAO(); // Data Access Object for discounts
        itemDao = new JdbcItemDAO(); // Data Access Object for items
        supplierItemDao = new JdbcSupplierItemDao();
    }
// function to load the data if required
//    public void loadData() throws SQLException {
//        // Load items from the database
//        List<ItemDTO> itemDTOs = itemDao.findAll();
//        for (ItemDTO itemDTO : itemDTOs) {
//            Category category = categories.get(itemDTO.getCategoryId());
//            Item item = new Item(itemDTO.getId(), itemDTO.getName(), category, itemDTO.getExpirationDate(),
//                    itemDTO.getQuantityInWarehouse(), itemDTO.getQuantityInStore(), itemDTO.getSupplier(),
//                    itemDTO.getManufacturer(), itemDTO.getBuyingPrice(), itemDTO.getSellingPrice(), itemDTO.getDemand(),
//                    new Location(itemDTO.getLocationAtStoreAisle(), itemDTO.getLocationAtStoreShelf()),
//                    new Location(itemDTO.getLocationAtWarehouseAisle(), itemDTO.getLocationAtWarehouseShelf()));
//            items.put(item.getId(), item);
//        }
//
//        // Load categories from the database
//        List<Category> categoryList = categoryDao.findAll();
//        for (Category category : categoryList) {
//            categories.put(category.getId(), category);
//        }
//
//        // Load discounts from the database
//        List<Discount> discountList = discountDao.findAll();
//        for (Discount discount : discountList) {
//            discounts.put(discount.getId(), discount);
//        }
//
//        // Load alerts from the database
//        List<Alert> alertList = alertDao.findAll();
//        AlertsRecord.addAll(alertList);
//    }
    public static InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }
    public String LoadProgram()  {
        try {
            // Load categories from the database
            List<CategoryDTO> categoryList = categoryDao.findAll();
            for (CategoryDTO categoryDTO : categoryList) {
                if (Objects.equals(categoryDTO.parentId(), categoryDTO.id())) {

                    Category category = new Category(categoryDTO.id(), categoryDTO.name(), categoryDTO.quantity(), categoryDTO.value());
                    categories.put(categoryDTO.id(), category);
                    //System.out.println(category);
                } else {
                    // If the category has a parent, get the parent category from the map
                    Category parentCategory = categories.get(categoryDTO.parentId());

                    // Create a new category with the parent
                    Category category = new Category(categoryDTO.id(), categoryDTO.name(), parentCategory, categoryDTO.quantity(), categoryDTO.value());
                    parentCategory.addSubCategory(category);
                    categories.put(categoryDTO.id(), category);
                }


                List<ItemDTO> itemDTOs = itemDao.findAll();
                List<SupplierItemDTO> supplierItemDTOs = supplierItemDao.findAll();
                int count = 0; // Initialize a counter to track the number of items loaded
                for (ItemDTO itemDTO : itemDTOs) {
                    Category category = categories.get(itemDTO.category_Id());
                    Item item = new Item(itemDTO.id(), itemDTO.name(), category, Date.from(itemDTO.expirationDate()),
                            itemDTO.quantityInWarehouse(), itemDTO.quantityInStore(), itemDTO.supplier(),
                            itemDTO.manufacturer(), itemDTO.buyingPrice(), itemDTO.sellingPrice(), itemDTO.demand(),
                            new Location(itemDTO.aisle_store(), itemDTO.shelf_store()),
                            new Location(itemDTO.aisle_warehouse(), itemDTO.shelf_warehouse()));
                    List<PriceHistoryEntryDTO> priceHistoryEntryDTOs = itemDao.findHistoryById(itemDTO.id());
                    for( PriceHistoryEntryDTO priceHistoryEntryDTO : priceHistoryEntryDTOs) {
                        item.addPriceHistoryEntry(new PriceHistoryEntry(
                                priceHistoryEntryDTO.sellingPrice(), priceHistoryEntryDTO.buyingPrice(),
                                Date.from(priceHistoryEntryDTO.date())));
                    }
                    category.addItemToCategory(item); // Add the item to the category
                    items.put(item.getId(), item);
                    for( SupplierItemDTO supplierItemDTO : supplierItemDTOs) {
                        if (supplierItemDTO.Item_Id() == item.getId()) {
                            item.addSupplier(supplierItemDTO.supplier_id()); // Add the supplier to the item
                        }
                    }

                }


                // Load discounts from the database
                List<DiscountDTO> discountList = discountDao.findAll();
                //System.out.println(discountList);
                if (discountList.isEmpty()) {
                    return "No discounts found in the database"; // Return error message if no discounts are found
                } else {
                    //System.out.println("Discounts loaded from the database: ");
                    //System.out.println(discountList);
                }
                for (DiscountDTO discountDTO : discountList) {
                    Discount discount = new Discount(discountDTO.id(), discountDTO.name(), discountDTO.type(), discountDTO.discountPercentage(),
                            Date.from(discountDTO.startDate()), Date.from(discountDTO.endDate()));
                    discounts.put(discountDTO.id(), discount);
                }
                for( Item item : items.values()) {
                    List<DiscountDTO> itemActiveDiscounts = discountDao.findByItemIdActive(item.getId());
                    Map<Integer,Discount> itemDiscounts = new HashMap<>();
                    for(DiscountDTO d : itemActiveDiscounts) {
                        itemDiscounts.put(d.id(), new Discount(d.id(), d.name(), d.type(), d.discountPercentage(),
                                Date.from(d.startDate()), Date.from(d.endDate())));
                    }
                    item.setActiveDiscounts(itemDiscounts); // Set the discounts for the item
                    List<DiscountDTO> historyDiscounts = discountDao.findByItemId(item.getId());
                    Map<Integer, Discount> itemHistoryDiscounts = new HashMap<>();
                    for (DiscountDTO d : historyDiscounts) {
                        itemHistoryDiscounts.put(d.id(), new Discount(d.id(), d.name(), d.type(), d.discountPercentage(),
                                Date.from(d.startDate()), Date.from(d.endDate())));
                    }
                    item.setDiscountsHistory(itemHistoryDiscounts); // Set the history discounts for the item
                }
                // Load alerts from the database
                List<AlertDTO> alertList = alertDao.findAll();
                for (AlertDTO alertDTO : alertList) {
                    Item item = items.get(alertDTO.item()); // Get the item associated with the alert
                    Alert alert = new Alert(alertDTO.id(), alertDTO.message(), alertDTO.type(), Date.from(alertDTO.dateCreated()), item);
                    AlertsRecord.add(alert); // Add the alert to the list of alerts
                }
                // Load reports from the database for defective items alone and for categories alone
                List<ReportDTO> reportList = reportDao.findAllReports();
                itemReports = new HashMap<Integer, Report>(); // Initialize the map of item reports
                for (ReportDTO reportDTO : reportList) {
                    Optional<ItemReportDTO> itemReportDTO = reportDao.findItemReportById(reportDTO.id());
                    Report report1 = new Report(reportDTO.id(),reportDTO.name(), reportDTO.description(), new Date(reportDTO.startDate().getTime()),new Date(reportDTO.endDate().getTime()));

                    reports.add(report1);
                    itemReports.put(report1.getId(), report1);
                    if (itemReportDTO.isEmpty()) {
                        continue; // Skip if no item report is found for the report
                    }

                    ItemReportDTO itemDTO1 = itemReportDTO.get();
                    Item item = items.get(itemDTO1.item_id()); // Get the item associated with the report
                    DefictiveItemReport report = new DefictiveItemReport(reportDTO.id(), reportDTO.name(), reportDTO.description(),
                            Date.from(reportDTO.startDate().toInstant()), Date.from(reportDTO.endDate().toInstant()), item, itemDTO1.defectiveQuantity());
                    itemReports.put(report.getId(), report); // Add the report to the map of item reports
                }
                for (ReportDTO reportDTO : reportList) {
                    Optional<CategoryReportDTO> categoryReportDTO = reportDao.findCategoryReportById(reportDTO.id());
                    if (categoryReportDTO.isEmpty()) {
                        continue; // Skip if no category report is found for the report
                    }
                    // Create a CategoryInventoryReport object from the reportDTO
                    // and the associated category
                    CategoryReportDTO categoryDTO1 = categoryReportDTO.get();
                    List<Integer> categoriesIDS = categoryDTO1.categories_ids();
                    List<Category> categories = new ArrayList<>();
                    for (Integer categoryId : categoriesIDS) {
                        Category category = this.categories.get(categoryId); // Get the category associated with the report
                        if (category != null) {
                            categories.add(category); // Add the category to the list of categories
                        }
                    }
                    ; // Get the category associated with the report
                    CategoryInventoryReport categoryReport = new CategoryInventoryReport(reportDTO.id(), categories, reportDTO.name(), reportDTO.description(),
                            Date.from(reportDTO.startDate().toInstant()), Date.from(reportDTO.endDate().toInstant()));
                    categoryReports.put(categoryReport.getId(), categoryReport); // Add the report to the map of category reports
                    return "Program loaded successfully"; // Return success message
                }
            }
            } catch(SQLException e){
                e.printStackTrace();
                return "Error loading program: " + e.getMessage(); // Return error message if an exception occurs
            }
            return "Program loaded successfully"; // Return success message if no exceptions occur

    }
    //getters and setters for the class
    //Item(String name,Category category , Date expirationDate, int quantityInWarehouse,int quantityInStore, Supplier supplier, double buyingPrice,double sellingPrice,int demand ,Location locationAtStore, Location locationAtWarehouse)
    public int addItem(String name,int categoryId , Date expirationDate, int quantityInWarehouse,int quantityInStore, String supplier,String manufacturer ,double buyingPrice,double sellingPrice,int demand ,String aisleAtStore,String shelfAtStore, String aisleAtWarehouse, String shelfAtWarehouse) throws SQLException {
        int itemReturn = -1 ;
     //   System.out.println("exp date: "+expirationDate.toString());
        Location locationAtStore = new Location(aisleAtStore,shelfAtStore);
        Location locationAtWarehouse = new Location(aisleAtWarehouse,shelfAtWarehouse);
    //    System.out.println(categories.get(categoryId).toString());
        Category category = categories.get(categoryId);
        Item item = new Item(name,category , expirationDate, quantityInWarehouse,quantityInStore, supplier,manufacturer ,buyingPrice,sellingPrice,demand ,locationAtStore, locationAtWarehouse); // Create a new item
        items.put(item.getId(), item); // Add the item to the inventory
        // Save the item to the database
       // System.out.println("instant exp date: "+expirationDate.toInstant().toString());
       // System.out.println("[InventoyManager addItem] Adding item to the database: " + item);
        itemDao.insert(new ItemDTO(item.getId(),item.getName(),categoryId,expirationDate.toInstant(),item.getQuantityInWarehouse(),item.getQuantityInStore(),item.getSupplier(),item.getManufacturer(),item.getBuyingPrice(),item.getSellingPrice(),item.getDemand(),item.getLocationAtStoreLocationObject().getAisle(),item.getLocationAtStoreLocationObject().getShelf(),item.getLocationAtWarehouseLocationObject().getAisle(),item.getLocationAtWarehouseLocationObject().getShelf()));
        if(!items.containsKey(item.id)){
            return itemReturn;
        }
       Optional<ItemDTO> i =   itemDao.findById(item.getId());
        if(i.isEmpty()){
            return itemReturn;

        }
        itemReturn = item.getId();
        return itemReturn ;
    }
    public void removeUnitFromItem(int id,String location,int quantity) throws SQLException {
        items.get(id).removeUnit(location,quantity); // Remove the item from the inventory
        //update the item in the database
        Item item = items.get(id);
        if (item != null) {

            itemDao.update(new ItemDTO(item.getId(),item.getName(),item.getCategory().getId(),item.getExpirationDate().toInstant(),item.getQuantityInWarehouse(),item.getQuantityInStore(),item.getSupplier(),item.getManufacturer(),item.getBuyingPrice(),item.getSellingPrice(),item.getDemand(),item.getLocationAtStoreLocationObject().getAisle(),item.getLocationAtStoreLocationObject().getShelf(),item.getLocationAtWarehouseLocationObject().getAisle(),item.getLocationAtWarehouseLocationObject().getShelf()));
        }
    }
    public void addUnitToItem(int id,String location,int quantity) {
        items.get(id).addUnit(location,quantity); // Add the item to the inventory
        //update the item in the database
        Item item = items.get(id);
        if (item != null) {
            try {
                itemDao.update(new ItemDTO(item.getId(),item.getName(),item.getCategory().getId(),item.getExpirationDate().toInstant(),item.getQuantityInWarehouse(),item.getQuantityInStore(),item.getSupplier(),item.getManufacturer(),item.getBuyingPrice(),item.getSellingPrice(),item.getDemand(),item.getLocationAtStoreLocationObject().getAisle(),item.getLocationAtStoreLocationObject().getShelf(),item.getLocationAtWarehouseLocationObject().getAisle(),item.getLocationAtWarehouseLocationObject().getShelf()));
            } catch (SQLException e) {
                System.out.println("[ERROR] ADD UNITE INVETORY MANAGER:"+e.getMessage());
                e.printStackTrace();
            }
        }
        else{
            System.out.println("there is no item in map");
        }
        
    }
    public void removeItem(int id) {
        Item item = items.get(id); // Get the item with the specified ID
        items.remove(id); // Remove the item from the inventory
        // Remove the item from the database
            if(isBelowThreshold(id)){
                return ; // addOrder
            }
            try {
                itemDao.deleteById(id); // Delete the item from the database
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }
    public ItemDTO getItemById(int id) throws SQLException {
        ItemDTO itemDTO = null ;
       // System.out.println(items.toString());
      //  System.out.println("checking th id "+id);
        if (!items.containsKey(id)) {
            if (itemDao.findById(id).isEmpty()) {
                return itemDTO; // Return error message if item is not found in the database
            }
            else {
                itemDTO = itemDao.findById(id).get(); // Get the item from the database
                Category category = categories.get(itemDTO.category_Id()); // Get the category associated with the item
                if(category==null){
                    CategoryDTO categoryDTO = categoryDao.findById(itemDTO.category_Id()).get();
                    Category parentCategory = categories.get(categoryDTO.parentId());
                    category=new Category(categoryDTO.id(), categoryDTO.name(), parentCategory, categoryDTO.quantity(), categoryDTO.value());
                    //throw new NullPointerException("Category is null");
                }
              //  System.out.println(category.toString());
              //  System.out.println(itemDTO);
                Item item = new Item(itemDTO.id(), itemDTO.name(), category, Date.from(itemDTO.expirationDate()),
                        itemDTO.quantityInWarehouse(), itemDTO.quantityInStore(), itemDTO.supplier(),
                        itemDTO.manufacturer(), itemDTO.buyingPrice(), itemDTO.sellingPrice(), itemDTO.demand(),
                        new Location(itemDTO.aisle_store(), itemDTO.shelf_store()),
                        new Location(itemDTO.aisle_warehouse(), itemDTO.shelf_warehouse()));
                items.put(id, item); // Add the item to the inventory
            }
        }
        else{
            //System.out.println("get item by id in manager is null ");
            Item item = items.get(id);
            ItemDTO ite = new ItemDTO(item.getId(), item.getName(), item.getCategory().getId(), item.getExpirationDate().toInstant(),
                    item.getQuantityInWarehouse(), item.getQuantityInStore(), item.getSupplier(), item.getManufacturer(),
                    item.getBuyingPrice(), item.getSellingPrice(), item.getDemand(),
                    item.getLocationAtStoreLocationObject().getAisle(), item.getLocationAtStoreLocationObject().getShelf(),
                    item.getLocationAtWarehouseLocationObject().getAisle(), item.getLocationAtWarehouseLocationObject().getShelf());
            return item.toDTO();
        }
        return itemDTO; // Get the item with the specified ID
    }
    public void checkItemsForAlert() throws SQLException {
        List<Item> itemList = new ArrayList<>(items.values());
        for (Item item : itemList) {
            if (item.isBelowThreshold()) {
                Alert alert = new Alert("Item is below threshold you need to order", "Warning", new Date(), item); // Create a new alert for the item
                AlertsRecord.add(alert); // Add the alert to the list of alerts
                AlertDTO alertDTO = new AlertDTO(alert.getId(), "item", alert.getType(), alert.getDateCreated().toInstant(), item.getId()); // Create a new AlertDTO object
                alertDao.insert(alertDTO); // Save the alert to the database
                System.out.println(alert.generateAlert()); // Print the alert details
         }
        }

    }
    public String showPriceHistoryForItem(int id) {
        Item item = items.get(id); // Get the item with the specified ID
        String s = ""; // Initialize an empty string to store the price history details
        if (item != null) {
            
            for (PriceHistoryEntry entry : item.getPriceHistory()) {
               s += entry.toString() + "\n"; // Append the price history entry details to the string
        }
        return s;
    }
        return "Item not found"; // Return error message if item is not found
    }
    public String checkExpiredItems(){
        List<Item> itemList = new ArrayList<>(items.values());
        String s = ""; // Initialize an empty string to store the expired items
    
        for (Item item : itemList) {
            if (item.isExpired()) {
                s += "Expired Item Name: " + item.getName() +"Id: "+item.getId()+ "\n"; // Append the expired item details to the string
         }
        }
        return s;
    }
    public boolean isExpired(int id){
        Item item = items.get(id);
        if (item != null) {
            return item.isExpired(); // Call the isExpired method of the Item class
        }
        return false; // Return false if the item is not found
    }
    public int getQuantity(int id){
        Item item = items.get(id);
        if (item != null) {
            return item.getQuantity(); // Call the getQuantity method of the Item class
        }
        return -1; // Return -1 if the item is not found
    }
    public void setQuantity(int id, int newQuantity) {
        Item item = items.get(id);
        if (item != null) {
            item.setQuantity(newQuantity); // Call the setQuantity method of the Item class
            // Update the item in the database
            try {
                itemDao.update(new ItemDTO(item.getId(),item.getName(),item.getCategory().getId(),item.getExpirationDate().toInstant(),item.getQuantityInWarehouse(),item.getQuantityInStore(),item.getSupplier(),item.getManufacturer(),item.getBuyingPrice(),item.getSellingPrice(),item.getDemand(),item.getLocationAtStoreLocationObject().getAisle(),item.getLocationAtStoreLocationObject().getShelf(),item.getLocationAtWarehouseLocationObject().getAisle(),item.getLocationAtWarehouseLocationObject().getShelf()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isBelowThreshold(int id){
        Item item = items.get(id);
        if (item != null) {
            return item.isBelowThreshold(); // Call the isBelowThreshold method of the Item class
        }
        return false; // Return false if the item is not found
    }
    public double getBuyingPrice(int id){
        Item item = items.get(id);
        if (item != null) {
            return item.getBuyingPrice(); // Call the getPrice method of the Item class
        }
        return -1;
    }
    public double getSellingPrice(int id){
        Item item = items.get(id);
        if (item != null) {
            return item.getSellingPrice(); // Call the getPrice method of the Item class
        }
        return -1; // Return -1 if the item is not found
    }
    public void setBuyingPrice(int id, double newPrice) {
        Item item = items.get(id);
        if (item != null) {
            item.setBuyingPrice(newPrice); // Call the setPrice method of the Item class
            // Update the item in the database
            try {
                itemDao.update(new ItemDTO(item.getId(),item.getName(),item.getCategory().getId(),item.getExpirationDate().toInstant(),item.getQuantityInWarehouse(),item.getQuantityInStore(),item.getSupplier(),item.getManufacturer(),item.getBuyingPrice(),item.getSellingPrice(),item.getDemand(),item.getLocationAtStoreLocationObject().getAisle(),item.getLocationAtStoreLocationObject().getShelf(),item.getLocationAtWarehouseLocationObject().getAisle(),item.getLocationAtWarehouseLocationObject().getShelf()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void setSellingPrice(int id, double newPrice) {
        Item item = items.get(id);
        if (item != null) {
            item.setSellingPrice(newPrice); // Call the setPrice method of the Item class
            // Update the item in the database
            try {
                itemDao.update(new ItemDTO(item.getId(),item.getName(),item.getCategory().getId(),item.getExpirationDate().toInstant(),item.getQuantityInWarehouse(),item.getQuantityInStore(),item.getSupplier(),item.getManufacturer(),item.getBuyingPrice(),item.getSellingPrice(),item.getDemand(),item.getLocationAtStoreLocationObject().getAisle(),item.getLocationAtStoreLocationObject().getShelf(),item.getLocationAtWarehouseLocationObject().getAisle(),item.getLocationAtWarehouseLocationObject().getShelf()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //,double sellingPrice,int demand ,)
    public boolean updateItem(int id,String locationAtStoreAisle,String locationAtStoreShilf,String locationAtWareHouseAisle,String locationAtWareHouseShilf ,String name,int categoryId , Date expirationDate, int quantityInStore,int quantityInWarehouse, String supplier, double buyingPrice,double sellingPrice,int demand) {
        Item item = items.get(id); // Get the item with the specified ID
        Category category = categories.get(id);
        Location locationAtStore = new Location(locationAtStoreAisle,locationAtStoreShilf);
        Location locationAtWareHouse = new Location(locationAtWareHouseAisle,locationAtWareHouseShilf);
        if (item != null) {
            item.updateItem(locationAtStore,locationAtWareHouse , name, category , expirationDate, quantityInStore, quantityInWarehouse, supplier,  buyingPrice,sellingPrice,demand) ;
                // Call the updateItem method of the Item class
            if(isBelowThreshold(id)){
                return false ; // addOrder() TODO
            }
            // Update the item in the database
            try {
                return itemDao.update(new ItemDTO(item.getId(),item.getName(),item.getCategory().getId(),item.getExpirationDate().toInstant(),item.getQuantityInWarehouse(),item.getQuantityInStore(),item.getSupplier(),item.getManufacturer(),item.getBuyingPrice(),item.getSellingPrice(),item.getDemand(),item.getLocationAtStoreLocationObject().getAisle(),item.getLocationAtStoreLocationObject().getShelf(),item.getLocationAtWarehouseLocationObject().getAisle(),item.getLocationAtWarehouseLocationObject().getShelf()));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            //return true; // Return true if the item was updated successfully
        }
        return false; // Return false if the item is not found
    }
    public boolean applyDiscountForItem(int id, int discountId) throws SQLException {
        Item item = items.get(id);
        if (item != null) {
         //   System.out.println(discounts.get(discountId));
            Discount discount = discounts.get(discountId); // Get the discount with the specified ID
            item.applyDiscount(discount); // Call the applyDiscountForItem method of the Item class
            discounts.put(discount.getId(), discount); // Add the discount to the map of discounts
            // insert the item_discounts table to the database
            discountDao.insertItemDiscount(id, discountId,1); // Save the discount to the database
            return true; // Return true if the discount was applied successfully
        }
        return false; // Return false if the item is not found
    }
    public void changeItemSupplierById(int id, String supplierName){
        items.get(id).setSupplier(supplierName);
        // Update the item in the database
        Item item = items.get(id);
        if (item != null) {
            try {
                itemDao.update(new ItemDTO(item.getId(),item.getName(),item.getCategory().getId(),item.getExpirationDate().toInstant(),item.getQuantityInWarehouse(),item.getQuantityInStore(),item.getSupplier(),item.getManufacturer(),item.getBuyingPrice(),item.getSellingPrice(),item.getDemand(),item.getLocationAtStoreLocationObject().getAisle(),item.getLocationAtStoreLocationObject().getShelf(),item.getLocationAtWarehouseLocationObject().getAisle(),item.getLocationAtWarehouseLocationObject().getShelf()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //public DefictiveItemReport(int id, String name, String description, Date startDate, Date endDate,Item item,int defectiveQuantity) {
    public String getDiscountDetails(int id) {
        return discounts.get(id).toString(); // Return the discount with the specified ID
    }
    public boolean isDiscountActive(int id) {
        Discount discount = discounts.get(id); // Get the discount with the specified ID
        if (discount != null) {
            return discount.isActive(); // Call the isActive method of the Discount class
        }
        return false; // Return false if the discount is not found
    }
    public void setDiscountEndDate(int id, Date newEndDate) {
        Discount discount = discounts.get(id); // Get the discount with the specified ID
        if (discount != null) {
            discount.setEndtDate(newEndDate); // Call the setEndtDate method of the Discount class
            // Update the discount in the database
            try {
                discountDao.update(new DiscountDTO(discount.getId(),discount.getName(),discount.getType(),discount.getDiscountPercentage(),discount.getStartDate().toInstant(),newEndDate.toInstant()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void setDiscountPercentage(int id, double newDiscountPercentage) {
        Discount discount = discounts.get(id); // Get the discount with the specified ID
        if (discount != null) {
            discount.setDiscountPercentage(newDiscountPercentage); // Call the setDiscountPercentage method of the Discount class
            try {
                discountDao.update(new DiscountDTO(discount.getId(),discount.getName(),discount.getType(),newDiscountPercentage,discount.getStartDate().toInstant(),discount.getEndDate().toInstant()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void removeDiscount(int discountId,int itemId) {
        Item item = items.get(itemId); // Get the item with the specified ID
        if (item != null) {
            
                item.removeDiscount(discountId); // Call the removeDiscount method of the Item class
                discounts.remove(discountId); // Remove the discount with the specified ID
                try {
                    //do it to delete the discount from the database of the item_discounts table
                    discountDao.deleteById(discountId); // Delete the discount from the database
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            
        }
        
    }
    public String showItems(){
        StringBuilder sb = new StringBuilder();
        for (Item item : items.values()) {
            sb.append(item)           // calls item.toString()
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
    public String showDiscounts(){
        StringBuilder s = new StringBuilder(); // Initialize an empty string to store the discount details
        for (Discount discount : discounts.values()) {
            s.append(discount.toString()).append("\n"); // Append the discount details to the string
        }
        return s.toString(); // Return the string containing the discount details
    }
    public String showActiveDiscounts(int id) throws SQLException {
        Item item = items.get(id); // Get the item with the specified ID
        for(Integer i : discounts.keySet()){
            if(!item.getActiveDiscounts().containsKey(i) && discountDao.getItemDiscountIsActive(id,i)){
                item.activeDiscounts.put(i,discounts.get(i));
            }
        }
        String s = ""; // Initialize an empty string to store the active discount details
        if (item != null) {
            for (Discount discount : new ArrayList<>(item.getActiveDiscounts().values())) {
                s += discount.toString() + "\n"; // Append the active discount details to the string

            }
            return s; // Return the string containing the active discount details
        }
        return "Item not found"; // Return error message if item is not found
    }
    public String showItemDiscounts(int id) throws SQLException {
        Item item = items.get(id); // Get the item with the specified ID
        StringBuilder s = new StringBuilder(); // Initialize an empty string to store the discount details
        if (item != null) {
            for (Discount discount : item.getDiscountsHistory().values()) {
                s.append(discount.toString()).append("\n"); // Append the discount details to the string
            }
            return s.toString(); // Return the string containing the discount details
        }
        return "Item not found"; // Return error message if item is not found
    }
    public void checkDefictiveItems(){
        for (Item item : items.values()) {
            if (item.isExpired()) {
                DefictiveItemReport report = new DefictiveItemReport("Defective Item Report", "Item is expired", new Date(), new Date(), item, item.getQuantity()); // Create a new report for the defective item
                itemReports.put(report.getId(),report); // Add the report to the list of defective item reports
                System.out.println(report.generateReport()); // Print the report details
            }
        }
    }
 //    add constructor without id and with id for item instead of item
    public void reportDefectiveItems(String name, String description, Date startDate, Date endDate, int itemId, int defectiveQuantity) {
        Report report1 ;
        Item item = items.get(itemId); // Get the item with the specified ID
        DefictiveItemReport report = new DefictiveItemReport(name, description, startDate, endDate, item, defectiveQuantity); // Create a new report for the defective item
        itemReports.put(report.getId(), report);
        // Save the report to the database
        try {
            ReportDTO rep = new ReportDTO(report.getId(), report.getName(), report.getDescription(), report.getStartDate(), report.getEndDate());

          //  System.out.println("inserting to database ..." + rep);
          //  System.out.println("reporting: " +rep);
            reportDao.insertReport(rep);
          //  System.out.println("inserted report to database: " + report.getId());
            reportDao.insertItemReport(new ItemReportDTO(report.getId(),report.getName(),report.getDescription(),report.getStartDate().toInstant(),report.getEndDate().toInstant() ,itemId, defectiveQuantity)); // Save the item report to the database
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(report.generateReport()); // Print the report details
        }
    }
    public String ShowItemReports(){
        String ItemReports = "";
      //  System.out.println(itemReports.values());
      //  System.out.println(reports);
        for (Report report : itemReports.values()) {
            ItemReports+=report.generateReport(); // Print the report details
        }
       // System.out.println("Here------->"+ItemReports);
        return ItemReports;
    }
    public String showAlerts(){
        String s = ""; // Initialize an empty string to store the alert details
        for (Alert alert : AlertsRecord) {
           s+= alert.generateAlert(); // Print the alert details
        }
        return s; // Return the string containing the alert details
    }
    //Category methods:
    //Category( String name, Category parentCategory, int quantity,int value,List<Item> items)
    public Category addNewCategory(String name, int parentCategoryId, int quantity,int value,List<Integer> itemsId) throws SQLException {
        Category parentCategory ;
        Category category;
        if(parentCategoryId == 0){
            parentCategory = null; // If the parent category ID is 0, set the parent category to null
            category = new Category(name,quantity, value); // Create a new category with no parent
        }else{

            parentCategory = categories.get(parentCategoryId); // Get the parent category with the specified ID
            if(parentCategory == null){
                System.out.println("Parent category not found"); // Print error message if parent category is not found

            }
            category = new Category(name, parentCategory, quantity, value); // Create a new category with the parent

        }

        List<Item> items = new ArrayList<>(); // Initialize a list to store the items in the category
        for (int itemId : itemsId) {
            Item item = items.get(itemId); // Get the item with the specified ID
            if (item != null) {
                items.add(item); // Add the item to the list
            }
        } 

      //  System.out.println("inserting to database ...");
        categoryDao.insert(new CategoryDTO(category.getId(),category.getParentCategory().getId(),category.getName(),category.getQuantity(),category.getValue())); // Save the category to the database

        categories.put(category.getId(), category); // Add the category to the map of categories
        return category;
    }
    public String setParentCategory(int id,int parentId) throws SQLException {
        Category category = categories.get(id); // Get the category with the specified ID
        Category parentCategory ;
        if(parentId == 0){
            parentCategory = null; // If the parent category ID is 0, set the parent category to null
        }else{
            parentCategory = categories.get(parentId); // Get the parent category with the specified ID
            if(parentCategory == null){
                 
                return "Parent category not found"; // Return error message if parent category is not found
            }
        }
        if (category == null) {
            return "Category not found"; // Return error message if category is not found
        }
        
            category.setParentCategory(parentCategory); // Call the setParentCategory method of the Category class
            int doTheLevelThing = 1;
            categoryDao.update(new CategoryDTO(id,parentId,category.getName(),category.getQuantity(),category.getValue())); // Save the parent-child relationship to the database
            return "Parent category set successfully"; // Return success message
        
         // Return error message if category or parent category is not found
    }
    public void removeSubCategory(int id,int subId) {
        Category category = categories.get(id); // Get the category with the specified ID
        Category subCategory = categories.get(subId); // Get the subcategory with the specified ID
        if (category != null && subCategory != null) {
            category.removeSubCategory(subCategory); // Call the removeSubCategory method of the Category class
            try{
                categoryDao.update(new CategoryDTO(subId,0,subCategory.getName(),subCategory.getQuantity(),subCategory.getValue())); // Remove the subcategory from the database
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void addSubCategory(int id,int subId) {
        Category category = categories.get(id); // Get the category with the specified ID
        Category subCategory = categories.get(subId); // Get the subcategory with the specified ID
        if (category != null && subCategory != null) {
            category.addSubCategory(subCategory); // Call the addSubCategory method of the Category class
            try {
                categoryDao.update(new CategoryDTO(subId,id,subCategory.getName(),subCategory.getQuantity(),subCategory.getValue()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //Important how to do it in database
    public String removeItemFromCategory(int id,int itemId) {
        Category category = categories.get(id); // Get the category with the specified ID
        Item item = items.get(itemId); // Get the item with the specified ID
        if(isBelowThreshold(itemId)){
            return null;// addOrder() TODO:Implemet
        }
        if (category != null && item != null) {
            category.removeItemFromCategory(item); // Call the removeItemFromCategory method of the Category class

        }
        if(items.containsKey(itemId)){  // return and check
          return "Failure in removing item";
        }
        return "Item Was Removed Successfully";
    }
    public String addItemToCategory(int id,int itemId) {
        Category category = categories.get(id); // Get the category with the specified ID
        Item item = items.get(itemId); // Get the item with the specified ID
        if (category != null && item != null) {
            category.addItemToCategory(item); // Call the addItemToCategory method of the Category class
            try{
                categoryDao.insertCategoryItem(id, itemId); // Save the item to the category in the database
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        List<Item> Items = category.getItems();
            if(Items.contains(item)) {
                return "Item was Added To Sub Category Successfully";
            }
            else return "Failure in Adding a Item";

    }
    public String getCategoryPath(int id) {
        Category category = categories.get(id); // Get the category with the specified ID
        if (category != null) {
            return category.getCategoryPath(); // Call the getCategoryPath method of the Category class
        }
        return "Category not found"; // Return error message if category is not found
    }
    public String getMainCategory(int id) {
        Category category = categories.get(id); // Get the category with the specified ID
        if (category != null) {
            return category.getMainCategory(); // Call the getMainCategory method of the Category class
        }
        return "Category not found"; // Return error message if category is not found
    }
    public String changeSubCategories(int mainCatId,List<Integer> subCategoriesIds){
        Category mainCategory = categories.get(mainCatId); // Get the main category with the specified ID
        List<Category> subCategories = new ArrayList<>(); // Initialize a list to store the subcategories
        for (int subCategoryId : subCategoriesIds) {
            Category subCategory = categories.get(subCategoryId); // Get the subcategory with the specified ID
            if (subCategory != null) {
                subCategories.add(subCategory); // Add the subcategory to the list
            }
        }
        if (mainCategory != null) {
            mainCategory.ChangeSubCategories(subCategories); // Call the ChangeSubCategories method of the Category class
            return "Subcategories changed successfully"; // Return success message
        }
        return "Main category not found"; // Return error message if main category is not found
    }
    public String showCategories(){
        String s = ""; // Initialize an empty string to store the category details
        for (Category category : categories.values()) {
            s += category.toString() + "\n"; // Append the category details to the string
        }
        return s; // Return the string containing the category details
    }
    public String removeCategory(int id) {
        Category category = categories.get(id); // Get the category with the specified ID
        if (category != null) {
            categories.remove(id); // Remove the category from the map of categories
            try {
                categoryDao.deleteById(id); // Remove the category from the database
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "Category removed successfully"; // Return success message
        }
        return "Category not found"; // Return error message if category is not found
    }
    public String applyDiscountToCategory(int id, int discountId) {
        Category category = categories.get(id); // Get the category with the specified ID

        Discount discount = discounts.get(discountId); // Get the discount with the specified ID
        if (discount == null) {
            return "Discount not found"; // Return error message if discount is not found
        }
        if (category != null) {
            category.applyDiscountToCategory(discount); // Call the applyDiscountForItem method of the Category class
            discounts.put(discount.getId(), discount); // Add the discount to the map of discounts
            try {
                discountDao.insertCategoryDiscount(id, discountId,1); // Save the discount to the database
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "Discount applied successfully"; // Return success message
        }
        return "Category not found"; // Return error message if category is not found
    }
    public String removeDiscountFromCategory(int id, int discountId) {
        Category category = categories.get(id); // Get the category with the specified ID
        if (category != null) {
           
                category.removeDiscountFromCategory(discountId); // Call the removeDiscount method of the Category class
                discounts.remove(discountId); // Remove the discount with the specified ID
                try {
                    discountDao.updateCategoryDiscountIsActive(id,discountId,0); // Delete the discount from the database
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return "Discount removed successfully"; // Return success message
        }
            
            
        
        return "Category not found"; // Return error message if category is not found
    }
    public String createCategoriesReport(String name, String description, Date startDate, Date endDate, List<Integer> categoriesIds) {
        List<Category> categoriesList = new ArrayList<>(); // Initialize a list to store the categories
        for (int categoryId : categoriesIds) {
            Category category = categories.get(categoryId); // Get the category with the specified ID
            if (category != null) {
                categoriesList.add(category); // Add the category to the list
            }
        }
        CategoryInventoryReport report = new CategoryInventoryReport(categoriesList, name, description, startDate, endDate); // Create a new report for the categories
        reports.add(report); // Add the report to the list of reports
        categoryReports.put(report.getId(), report); // Add the report to the map of category reports
        // Save the report to the database
      //  System.out.println(categoriesList);
        try {
            reportDao.insertReport(new ReportDTO(report.getId(), report.getName(), report.getDescription(), report.getStartDate(), report.getEndDate()));
            for (Category category : categoriesList) {
                reportDao.insertCategoryReport(new CategoryReportDTO(report.getId(), List.of(category.getId()),report.getName(),report.getDescription(),(new Date()).toInstant(),(new Date()).toInstant())); // Save the category report to the database
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report.DoCategotriesReport(); // Return success message
        
    }
    public String showCategory(int id) {
        Category category = categories.get(id); // Get the category with the specified ID
        if (category != null) {
            return category.toString(); // Return the category details as a string
        }
        return "Category not found"; // Return error message if category is not found
    }
    public String removeDiscountFromItem(int id, int discountId) throws SQLException {
        Item item = items.get(id); // Get the item with the specified ID
        if (item != null) {
            item.removeDiscount(discountId); // Call the removeDiscount method of the Item class
            discounts.remove(discountId); // Remove the discount with the specified ID
            discountDao.updateItemDiscountIsActive(id, discountId,0); // Delete the discount from the database
            return "Discount removed successfully"; // Return success message
        }
        return "Item not found"; // Return error message if item is not found
    }
    public String CreateDiscount(String name,String type ,double discountPercentage, Date startDate, Date endDate) {
        Discount discount = new Discount(name,type, discountPercentage, startDate, endDate,this); // Create a new discount
        discounts.put(discount.getId(), discount); // Add the discount to the map of discounts
        try {
            discountDao.insert(new DiscountDTO(discount.getId(), discount.getName(), discount.getType(), discount.getDiscountPercentage(), discount.getStartDate().toInstant(), discount.getEndDate().toInstant())); // Save the discount to the database
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error creating discount"; // Return error message if there is an issue with the database
        }
        return "Discount created successfully"; // Return success message
    }

    public Location createLocation(String aisle, String shelf) {
        return new Location(aisle, shelf); // Create a new location with the specified aisle and shelf
    }
    public String createDiscount(String name,String type ,double discountPercentage, Date startDate, Date endDate) {
        Discount discount = new Discount(name,type, discountPercentage, startDate, endDate,this); // Create a new discount
        discounts.put(discount.getId(), discount); // Add the discount to the map of discounts
        try {
            discountDao.insert(new DiscountDTO(discount.getId(), discount.getName(), discount.getType(), discount.getDiscountPercentage(), discount.getStartDate().toInstant(), discount.getEndDate().toInstant())); // Save the discount to the database
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error creating discount"; // Return error message if there is an issue with the database
        }
        return "Discount has been added to discounts List"; // Return true if the discount was created successfully
    }
    public String removeDiscount(int id) throws SQLException {
        Discount discount = discounts.get(id); // Get the discount with the specified ID
        if (discount != null) {
            discounts.remove(id); // Remove the discount from the map of discounts
            return "Discount removed successfully"; // Return success message
        }
        discountDao.deleteById(id);
        for(Item item : items.values()) {
            if(item.getActiveDiscounts().containsKey(id)){
                item.removeDiscount(id); // Remove the discount from the item

            }
        }
        //Maybe i need to add active discounts to the category as well
        return "Discount not found"; // Return error message if discount is not found
    }
    public boolean createAlert(String message, String type, Date startDate, int itemId) {
        Item item = items.get(itemId); // Get the item with the specified ID
        if (item != null) {
            Alert alert = new Alert(message,type, startDate, item); // Create a new alert for the item
            AlertsRecord.add(alert); // Add the alert to the list of alerts
            AlertDTO alertDTO = new AlertDTO(alert.getId(), alert.getMessage(), alert.getType(), alert.getDateCreated().toInstant(), itemId); // Create a DTO for the alert
            try {
                System.out.println(alertDao.insert(alertDTO));
                return true; // Return true if the alert was created successfully
            } catch (SQLException e) {
                System.out.println("Error creating alert: " + e.getMessage()); // Print error message if there is an issue with the database
                throw new RuntimeException(e);
            }

        }
        return false; // Return false if the item is not found
    }
    public String getDiscountById(int id) {
        Discount discount = discounts.get(id); // Get the discount with the specified ID
        if (discount != null) {
            return discount.toString(); // Return the discount details as a string
        }
        return "Discount not found"; // Return error message if discount is not found
    }
    public String showAllDiscounts() {
        //System.out.println(discounts);
        String s ="";
        for (Discount discount : discounts.values()) {
            s+= discount.toString() + "\n"; // Append the discount details to the string
        }
        return s;
    }
    public String getAllDiscounts() {
        String s = ""; // Initialize an empty string to store the discount details
        if(discounts.size() == 0) {
            try{
                List<DiscountDTO> tempDiscounts = discountDao.findAll();
                if(tempDiscounts.isEmpty()){
                    return "no discounts found"; // Return message if no discounts are found
                }
                for (DiscountDTO discountDTO : tempDiscounts) {
                    Discount discount = new Discount(discountDTO.id(), discountDTO.name(), discountDTO.type(), discountDTO.discountPercentage(), Date.from(discountDTO.startDate()), Date.from(discountDTO.endDate())); // Create a new discount from the DTO
                    discounts.put(discount.getId(), discount); // Add the discount to the map of discounts
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return "Error loading discounts"; // Return error message if there is an issue with the database
            }
        }
        for (Discount discount : discounts.values()) {
            s += discount.toString() + "\n"; // Append the discount details to the string
        }
        return s; // Return the string containing the discount details
    }

    public String deleteDiscount(int id) {
        Discount discount = discounts.get(id); // Get the discount with the specified ID
        if (discount != null) {
            discounts.remove(id); // Remove the discount from the map of discounts
            try {
                discountDao.deleteById(id); // Delete the discount from the database
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error deleting discount"; // Return error message if there is an issue with the database
            }
            for(Item item : items.values()) {
                if(item.getActiveDiscounts().containsKey(id)){
                    item.removeDiscount(id); // Remove the discount from the item

                }
            }
            return "Discount removed successfully"; // Return success message
        }
        return "Discount not found"; // Return error message if discount is not found
    }

    public String updateDiscount(int id, String name, String type, double discountPercentage, Date startDate,
            Date endDate) {
        Discount discount = discounts.get(id); // Get the discount with the specified ID
        if (discount != null) {
            discount.setDiscountPercentage(discountPercentage); // Call the setDiscountPercentage method of the Discount class
            discount.setEndtDate(endDate); // Call the setEndtDate method of the Discount class
            discount.setName(name); // Call the setName method of the Discount class
            discount.setType(type); // Call the setType method of the Discount class
            discount.setStartDate(startDate); // Call the setStartDate method of the Discount class
            try {
                discountDao.update(new DiscountDTO(discount.getId(), discount.getName(), discount.getType(), discount.getDiscountPercentage(), discount.getStartDate().toInstant(), discount.getEndDate().toInstant())); // Update the discount in the database
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error updating discount"; // Return error message if there is an issue with the database
            }
            return "Discount updated successfully"; // Return success message
        }

        return "Discount not found"; // Return error message if discount is not found
    }

    public boolean deleteReport(int id,int type) throws SQLException {
        Boolean IsDeleted = false ;
        //System.out.println("HReferee");
//        for(int i = 0 ; i<reports.size(); i++){
//            System.out.println(reports.get(i).toString());
//            System.out.println(i);
//        }
//        Report repo = reports.get(id);
//        if (repo == null) {
//            System.out.println("Report with ID " + id + " not found in reports map.");
//            return false;
//        }
//        System.out.println(repo.toString());

        if(type ==1){
            CategoryInventoryReport report = categoryReports.get(id);
            if (report != null) {
                categoryReports.remove(id); // Remove the report from the list of reports
                reportDao.deleteCategoryReportById(id);
                IsDeleted=  true; // Return true if the report was deleted successfully
            }
        }
        else if(type ==2){
            Report report = itemReports.get(id); // Get the report with the specified ID
            if (report != null) {
                IsDeleted =  itemReports.remove(id,report); // Remove the report from the list of reports
               // System.out.println("1. "+IsDeleted);
               // System.out.println("Check if the item was Delete in the Hash Map "+IsDeleted);
                if(!IsDeleted){
                    System.out.println("Wasn't Deleted ");
                }
               IsDeleted= reportDao.deleteItemReportById(id);
              //  System.out.println("2."+IsDeleted);  // Return true if the report was deleted successfully
            }
        }
//        if (report != null) {
//            IsDeleted= itemReports.remove(id,report); // Remove the report from the list of reports
//            // Return true if the report was deleted successfully
//        }
        //System.out.println(IsDeleted+ "IN END");
       return IsDeleted; // Return false if the report is not found
    }

    public String getReportById(int id,int type) {
        // TODO Auto-generated method stub
        String Output = "";
        if(type == 1){
            CategoryInventoryReport report = categoryReports.get(id); // Get the report with the specified ID
            if (report != null) {
                Output=report.generateReport(); // Print the report details
            } else {
               Output="Report not found"; // Print error message if report is not found
            }
        }
        else if(type == 2){
            Report report = itemReports.get(id);
            if (report != null) {
                Output=report.generateReport(); // Print the report details
            } else {
                Output="Report not found"; // Print error message if report is not found
            }
        }
        System.out.println(Output);
    return Output;
    }

    public String updateReport(int id, int type,String name, String description, Date startDate, Date endDate, int defectiveQuantity) {
       List<Report> List = getReports();
       Report repo = getReportId(List,id);
        Report report = itemReports.get(id); // Get the report with the specified ID
        if (report != null) {
            repo.setName(name);
            repo.setDescription(description); // Call the setDescription method of the DefictiveItemReport class
            repo.setStartDate(startDate); // Call the setStartDate method of the DefictiveItemReport class
            repo.setEndDate(endDate);
            report.setName(name); // Call the setName method of the DefictiveItemReport class
            report.setDescription(description); // Call the setDescription method of the DefictiveItemReport class
            report.setStartDate(startDate); // Call the setStartDate method of the DefictiveItemReport class
            report.setEndDate(endDate); // Call the setEndDate method of the DefictiveItemReport class
            report.setDefectiveQuantity(defectiveQuantity); // Call the setDefectiveQuantity method of the DefictiveItemReport class
            try {
                reportDao.updateReport(new ReportDTO(report.getId(), report.getName(), report.getDescription(), report.getStartDate(), report.getEndDate())); // Update the report in the database
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return "Report not found"; // Print error message if report is not found
        }
        return "Report Was Updated Successfully";
    }

    private Report getReportId(List<Report> list, int id) {
        Report repo =null ;
        for(Report report1 : list ){ // Get the report with the specified ID
            if(report1.getId() == id){
                repo = report1 ;
            }
        }
        return repo;
    }

    public String updateCategory(int id, String name, int parentId, int quantity, int value) {
        Category category = categories.get(id); // Get the category with the specified ID
        if (category != null) {
            category.setName(name); // Call the setName method of the Category class
            category.setParentCategory(categories.get(parentId)); // Call the setParentCategory method of the Category class
            category.setQuantity(quantity); // Call the setQuantity method of the Category class
            category.setValue(value); // Call the setValue method of the Category class
            try {
                categoryDao.update(new CategoryDTO(category.getId(),category.getParentCategory().getId() ,category.getName(), category.getQuantity(), category.getValue())); // Update the category in the database
//                if(parentId != category.getId()){
//                    categoryDao.deleteParentSon(id, List.of(parentId));
//                    categoryDao.insertParentSon(parentId, List.of(id), 1, 1); // Save the parent-child relationship to the database
//                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
         return   " Category not found"; // Print error message if category is not found
        }
        return "Category Was Updated Successfully";
    }

    public String CreateReport(String name, String description, Date startDate,Date endDate, int itemId, int defectiveQuantity) {
        Item item = items.get(itemId); // Get the item with the specified ID
        DefictiveItemReport report = new DefictiveItemReport(name, description, startDate, endDate, item, defectiveQuantity); // Create a new report for the defective item
       // System.out.println("ReportID------------>"+report.getId());
        itemReports.put(report.getId(), report);
      //  System.out.println("Report -------------->"+ report.generateReport());
        // Save the report to the database
        try {
          //  System.out.println("Entering DTO REPORT");
           // System.out.println(report.getStartDate());
            ReportDTO rep = new ReportDTO(report.getId(), report.getName(), report.getDescription(), report.getStartDate(), report.getEndDate());

           // System.out.println("inserting to database ..." + rep);
           // System.out.println("reporting: " +rep);
            reportDao.insertReport(rep);
          //  System.out.println("inserted report to database: " + report.getId());
            ItemReportDTO temp =new ItemReportDTO(report.getId(),report.getName(),report.getDescription(),(new Date()).toInstant(),(new Date()).toInstant() ,itemId, defectiveQuantity);
           // System.out.println(temp);
            reportDao.insertItemReport(temp); // Save the item report to the database
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(report.generateReport()); // Print the report details
        }
        return report.generateReport();
    }
    public boolean addSupplier(String supplierID,int item_id){
        Item item = items.get(item_id);// Get the item with the specified ID
       // System.out.println("item in [ inventorymanager addSupplier]"+item);
        if (item != null) {
            item.addSupplier(supplierID); // Call the addSupplier method of the Item class
            try {
                supplierItemDao.insertSupplierItem(new SupplierItemDTO(supplierID,item_id)); // Save the supplier to the item in the database
                return true; // Return true if the supplier was added successfully
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Return false if there is an issue with the database
            }
        }
        return false; // Return false if the item is not found
    }
    public boolean removeSupplier(String supplierID,int item_id){
        Item item = items.get(item_id); // Get the item with the specified ID
        if (item != null) {
            item.removeSupplier(supplierID); // Call the removeSupplier method of the Item class
            try {
                supplierItemDao.deleteBySupplierId(supplierID); // Remove the supplier from the item in the database
                return true; // Return true if the supplier was removed successfully
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Return false if there is an issue with the database
            }
        }
        return false; // Return false if the item is not found
    }
}