package ServiceLayer;

import ObjectDTO.Supplier.*;
import ServiceLayer.Inventory.*;
import ServiceLayer.Supplier.*;
import domainLayer.Inventory.InventoryManager;
import domainLayer.Inventory.Item;
import domainLayer.Supplier.Factory;
import domainLayer.Supplier.Managers.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceFactory {
    private static ServiceFactory Instance;

    public AlertService alertService;
    public CategoryService categoryService;
    public ItemService itemService;
    public ReportService reportService;
    public DiscountService discountService;

    int orderId = 0;
    private InventoryManager manager;
    private Response response;
    private AgreementService AgreementService;
    private SupplierService SupplierService;
    private OrderService OrderService;
    private ProductService ProductService;
    private ManufacturerService ManufacturerService;
    private Factory factory;

    public ServiceFactory() {
        //****inventory
        this.manager = InventoryManager.getInstance();
        this.alertService = new AlertService(manager);
        this.categoryService = new CategoryService(manager);
        this.discountService = new DiscountService(manager);
        this.itemService = new ItemService(manager);
        this.reportService = new ReportService(manager);
        this.response = new Response();


        //****supplier
        this.AgreementService = AgreementService.getInstance();
        this.SupplierService = SupplierService.getInstance();
        this.OrderService = OrderService.getInstance();
        this.ProductService = ProductService.getInstance();
        this.ManufacturerService = ManufacturerService.getInstance();
        this.factory = Factory.getInstance();
    }


    public static ServiceFactory getInstance() {
        if (Instance == null) {
            Instance = new ServiceFactory();
        }
        return Instance;
    }
    // ItemService methods
    public String showItems() {
        return itemService.showItems();
    }

    public ObjectDTO.Inventory.ItemDTO getItemById(int id) {
        return itemService.getItemById(id);
    }
    private boolean isExist(int id ){
        return getItemById(id)!=null;
    }

    public String addItem(String name, int categoryId, Date expirationDate, int quantityInWarehouse, int quantityInStore, String supplier, String manufacturer, double buyingPrice, double sellingPrice, int demand, String aisleAtStore, String shelfAtStore, String aisleAtWarehouse, String shelfAtWarehouse) {
        String Output = "";
        Output= itemService.addItem(name, categoryId, expirationDate, quantityInWarehouse, quantityInStore, supplier, manufacturer, buyingPrice, sellingPrice, demand, aisleAtStore, shelfAtStore, aisleAtWarehouse, shelfAtWarehouse);
        ProductDTO productDTO = new ProductDTO(Output,name);
        ProductService.add_product(productDTO);
        return Output;
    }

    public String removeItem(int id) {
        return itemService.removeItem(id);
    }

    public String updateItem(int id, String locationAtStoreAisle, String locationAtStoreShelf, String locationAtWarehouseAisle, String locationAtWarehouseShelf, String name, int categoryId, Date expirationDate, int quantityInStore, int quantityInWarehouse, String supplier, double buyingPrice, double sellingPrice, int demand) {
         itemService.updateItem(id, locationAtStoreAisle, locationAtStoreShelf, locationAtWarehouseAisle, locationAtWarehouseShelf, name, categoryId, expirationDate, quantityInStore, quantityInWarehouse, supplier, buyingPrice, sellingPrice, demand);
        if (itemService.isBelowThresholdById(id)){
            manager.createAlert("Item is Below Threshold", "quantity",new Date(),id);
            ObjectDTO.Inventory.ItemDTO item = itemService.getItemById(id);
            List<Pair<Integer, ProductDTO>> ls = new ArrayList<>();
            ls.add(new Pair<>(item.demand()*4,new ProductDTO(item.id()+"", item.name()))); // Assuming demand is the quantity needed,
            this.addOrder(ls);
        }
        return "Item updated successfully.";
    }

    public String changeItemSupplierById(int id, String supplier) {
        return itemService.changeItemSupplierById(id, supplier);
    }

    public String removeUnitFromItem(int id, String location, int quantity) {
        itemService.removeUnitFromItem(id, location, quantity);//todo : get item  > function is below > tue? generate alert call addOrder(List<OrderDTO>) function
        //ObjectDTO.Inventory.ItemDTO item = itemService.getItemById(id);

        if (itemService.isBelowThresholdById(id)){
//            System.out.println("true");
            manager.createAlert("Item is Below Threshold", "quantity",new Date(),id);
            ObjectDTO.Inventory.ItemDTO item = itemService.getItemById(id);
            List<Pair<Integer, ProductDTO>> ls = new ArrayList<>();
            ls.add(new Pair<>(item.demand()*4,new ProductDTO(item.id()+"", item.name()))); // Assuming demand is the quantity needed,
            this.addOrder(ls);
        }
        return "" ;
    }

    public String addUnitToItem(int id, String location, int quantity) {
        return itemService.addUnitToItem(id, location, quantity);
    }

    public String isExpired(int id) {
        return itemService.isExpired(id);
    }

    public String getQuantity(int id) {
        return itemService.getQuantity(id);
    }

    public String setQuantity(int id, int qty) {
        return itemService.setQuantity(id, qty);
    }

    public String isBelowThreshold(int id) {
        return itemService.isBelowThreshold(id);
    }

    public String getBuyingPrice(int id) {
        return itemService.getBuyingPrice(id);
    }

    public String setBuyingPrice(int id, double price) {
        return itemService.setBuyingPrice(id, price);
    }

    public String getSellingPrice(int id) {
        return itemService.getSellingPrice(id);
    }

    public String setSellingPrice(int id, double price) {
        return itemService.setSellingPrice(id, price);
    }

    public String checkExpiredItems() {
        return itemService.checkExpiredItems();
    }

    // ReportService methods
    public String checkDefectiveItems() {
        return reportService.checkDefectiveItems();
    }

    public String reportDefectiveItems(String name, String description, Date startDate, Date endDate, int itemId, int defectiveQuantity) {
        return reportService.reportDefectiveItems(name, description, startDate, endDate, itemId, defectiveQuantity);
    }

    public String ShowItemReports() {
        return reportService.ShowItemReports();
    }

    public String deleteReport(int id, int type) {
        return reportService.deleteReport(id, type);
    }

    public String getReportById(int id, int type) {
        return reportService.getReportById(id, type);
    }

    public String updateReport(int id, int type, String name, String description, Date startDate, Date endDate, int defectiveQuantity) {
        return reportService.updateReport(id, type, name, description, startDate, endDate, defectiveQuantity);
    }

    public String createCategoriesReport(String name, String description, Date startDate, Date endDate, List<Integer> categoryIds) {
        return reportService.createCategoriesReport(name, description, startDate, endDate, categoryIds);
    }

    // CategoryService methods
    public String showCategories() {
        return categoryService.showCategories();
    }

    public String addNewCategory(String name, int parentId, int quantity, int value, List<Integer> itemsId) {
        return categoryService.addNewCategory(name, parentId, quantity, value, itemsId);
    }

    public String removeCategory(int id) {
        return categoryService.removeCategory(id);
    }

    public String showCategory(int id) {
        return categoryService.showCategory(id);
    }

    public String changeSubCategories(int mainCatId, List<Integer> subCategoryIds) {
        return categoryService.changeSubCategories(mainCatId, subCategoryIds);
    }

    public String setParentCategory(int id, int parentId) {
        return categoryService.setParentCategory(id, parentId);
    }

    public String addSubCategory(int id, int subId) {
        return categoryService.addSubCategory(id, subId);
    }

    public String removeSubCategory(int id, int subId) {
        return categoryService.removeSubCategory(id, subId);
    }

    public String addItemToCategory(int id, int itemId) {
        return categoryService.addItemToCategory(id, itemId);
    }

    public String removeItemFromCategory(int id, int itemId) {
        return categoryService.removeItemFromCategory(id, itemId);
    }

    public String getCategoryPath(int id) {
        return categoryService.getCategoryPath(id);
    }

    public String getMainCategory(int id) {
        return categoryService.getMainCategory(id);
    }

    public String updateCategory(int id, String name, int parentId, int quantity, int value) {
        return categoryService.updateCategory(id, name, parentId, quantity, value);
    }

    public String applyDiscountToCategory(int id, int discountId) {
        return categoryService.applyDiscountToCategory(id, discountId);
    }

    public String removeDiscountFromCategory(int id, int discountId) {
        return categoryService.removeDiscountFromCategory(id, discountId);
    }

    // AlertService methods
    public String showAlerts() {
        return alertService.showAlerts();
    }

    public String checkItemsForAlert() {
        return alertService.checkItemsForAlert();
    }

    // DiscountService methods
    public String createDiscount(String name, String type, double discountPercentage, Date startDate, Date endDate) {
        return discountService.createDiscount(name, type, discountPercentage, startDate, endDate);
    }

    public String showDiscounts() {
        return discountService.showDiscounts();
    }

    public String showActiveDiscounts(int id) {
        return discountService.showActiveDiscounts(id);
    }
    public String showItemDiscounts(int itemId) {
        return discountService.showItemDiscounts(itemId);
    }
    public String applyDiscountForItem(int itemId, int discountId) {
        return discountService.applyDiscountForItem(itemId, discountId);
    }

    public String getDiscountDetails(int id) {
        return discountService.getDiscountDetails(id);
    }

    public String isDiscountActive(int id) {
        return discountService.isDiscountActive(id);
    }

    public String setDiscountEndDate(int id, Date newEndDate) {
        return discountService.setDiscountEndDate(id, newEndDate);
    }

    public String setDiscountPercentage(int id, double newDiscountPercentage) {
        return discountService.setDiscountPercentage(id, newDiscountPercentage);
    }

    public String removeDiscountFromItem(int id, int discountId) {
        return discountService.removeDiscountFromItem(id, discountId);
    }

    public String deleteDiscount(int id) {
        return discountService.deleteDiscount(id);
    }

    public String updateDiscount(int id, String name, String type, double discountPercentage, Date startDate, Date endDate) {
        return discountService.updateDiscount(id, name, type, discountPercentage, startDate, endDate);
    }

    public String showAllDiscounts() {
        return discountService.showAllDiscounts();
    }

    public String getDiscountById(int id) {
        return discountService.getDiscountById(id);
    }

    public String LoadProgram() {
        try {
            String s = manager.LoadProgram();
            response = Response.success(s);

        } catch (Exception e) {
            response = Response.failure(e.getMessage());
        }
        return response.toString();
    }

    public String showPriceHistoryForItem(int itemId) {
        return manager.showPriceHistoryForItem(itemId);
    }

    public AgreementService getAgreementService() {
        return AgreementService;
    }

    public SupplierService getSupplierService() {
        return SupplierService;
    }

    public OrderService getOrderService() {
        return OrderService;
    }

    public ProductService getProductService() {
        return ProductService;
    }

    public ManufacturerService getManufacturerService() {
        return ManufacturerService;
    }

    public Factory getFactory() {
        return factory;
    }

    private OrderDTO getOrder(List<Pair<Integer, ProductDTO>> ls) {
        //


        AgreementDTO agreement = AgreementService.get_best_deal(ls).isError() ? null : (AgreementDTO) AgreementService.get_best_deal(ls).getMessage();
        //System.out.println(" get messege"+(SupplierService.findSupplierById(agreement.supplierId())));
        String supplierName = ((SupplierDTO) SupplierService.findSupplierById(agreement.supplierId()).getMessage()).getName();
        ContactDTO supplierContact = ((SupplierDTO) SupplierService.findSupplierById(agreement.supplierId()).getMessage()).getContact().get(0);
        double total_price = 0;
        if (agreement != null) {
            List<ItemDTO> items = new ArrayList<>();

            for (Pair<Integer, ProductDTO> p : ls) {
                ProductDTO toAdd=ProductService.get_product_by_Id(p.second.getId()).isError()?null:(ProductDTO) ProductService.get_product_by_Id(p.second.getId()).getMessage();
                ManufacturerDTO manu = agreement.items().get(p.second.getId()).getManufacturer();
                double price = agreement.items().get(p.second.getId()).getPrice();
                String suppliercatNum = agreement.items().get(p.second.getId()).getSupplierCatNum();
                List<DiscountDTO> dis = agreement.items().get(p.second.getId()).getQuantityDiscounts();
                SupItemDTO supItemDTO = new SupItemDTO(toAdd, manu, agreement.supplierId(), agreement.agreementId(), price, suppliercatNum, dis);
                ItemDTO itemToAdd = new ItemDTO(supItemDTO, orderId + "", p.first, supItemDTO.getPrice(p.first));

                items.add(itemToAdd);
                total_price += itemToAdd.price();
            }

            int autoMaticOrderId=orderId;
            while(!OrderService.is_uniq_id(autoMaticOrderId+"")){
                orderId++;
                autoMaticOrderId= orderId;
            }
            OrderDTO order = new OrderDTO(autoMaticOrderId+"", supplierName, agreement.supplierId(), items, supplierContact, total_price, LocalDate.now(), "store", StatusDTO.PURCHASED);
            orderId++;
            return order;

        } else
            return null;
    }

    public boolean addFixedOrder(List<Pair<Integer, ProductDTO>> ls, DaysDTO day) {

        OrderDTO orderToAdd = getOrder(ls);
        if (orderToAdd == null)
            return false;
        getOrderService().add_fixed_order(new FixedOrderDTO(orderToAdd, day), day);
        return true;
    }

    public boolean addOrder(List<Pair<Integer, ProductDTO>> ls) {//Pair <quantity : integer , productDTO>
        OrderDTO orderTOAdd = getOrder(ls);
       // System.out.println("[sf]orderTOAdd "+orderTOAdd);
        if (orderTOAdd == null)
            return false;

        getOrderService().add_needed_order(orderTOAdd);
        return true;
    }
    public void next_day(){
        List<OrderDTO> orderDTOList=getOrderService().next_day(); //? null : (List<OrderDTO>) getOrderService().next_day().getMessage();
        ////System.out.println("{next day} recived the list "+recived);

        //System.out.println("{next day function}");
        boolean recived=false;
        if( orderDTOList!=null){
            //System.out.println("next day recived the list not null");
            //todo:implement
            recived=this.Receive(orderDTOList);//Recive(orderlst:List<OrderDTO>):Boolean(ok i get the order )
           // recived=true;

        }
        //System.out.println("{next day} recived the list "+recived);
        if(recived){
            orderDTOList.forEach(orderDTO -> {this.delivered(orderDTO.getOrder_id());});//confirm that the order is delivered
        }
    }

    public String get_day() {
        return getOrderService().get_day();
    }

    public String delivered(String orderId) {
        return OrderService.delivered( orderId);
    }

    public boolean Receive(List<OrderDTO> orderList){
       try {
 //          System.out.println("order list:"+orderList);
//           orderList.forEach(orderDTO -> {
//               orderDTO.getItem_list().forEach(itemDTO -> {
//                   System.out.println(itemDTO);
//               });
//           });
           boolean Output = false;
           for (OrderDTO Order : orderList) {
               for (ItemDTO Item : Order.getItem_list()) {
                   //System.out.println("recive order:"+Order.getOrder_id());
                   String itemId = Item.supItem().getproduct().getId(); // TODO :  insert the itemId in Integer form
                   if (itemId == null) {
                     //  System.out.println("null here 1");
                       Output =Output|| false;
                       break;
                       //return false;
                   }
                   int Id = Integer.parseInt(itemId);
                   ObjectDTO.Inventory.ItemDTO ItemDTO = itemService.getItemById(Id);
                   //System.out.println("i am here");
                   if(ItemDTO==null)
                   {
                       System.out.println("null here 2");
                       Output=Output|| false;
                       break;
                       //System.out.println("i am here 1");
                      // return false;
                   }
                   int OldQuantity = ItemDTO.quantityInWarehouse();
                   //System.out.println("qntity now"+ItemDTO.quantityInWarehouse() );
               //    System.out.println(this.addUnitToItem(Id, "Warehouse", Item.quantity()));
                   if (itemService.getItemById(Id).quantityInWarehouse() != (OldQuantity + Item.quantity())) {
                       //System.out.println("i am here 2");
                       //System.out.println("the quntity now"+itemService.getItemById(Id).quantityInWarehouse() +"!="+ "OldQuantity "+OldQuantity + "Item.quantity()))"+Item.quantity());
                       Output = false;
                   }
                   else{
                       Output=true;
                   }
               }
           }
           return Output;
       } catch (Exception e) {
           System.out.println("error in receive function"+e.getMessage());
           return false;
       }

    }
    private boolean is_valid_supplier_item(String supplier_id, String ItemId){
        if (supplier_id == null || ItemId == null)
        {
            //System.out.println("[ERORR] is_valid_supplier_item : inserting null parmeters");
            return false;
        }
        SupplierDTO supplierDTO =(SupplierDTO) SupplierService.findSupplierById(supplier_id).getMessage();
        if(supplierDTO==null){// there is no supplier with this id in the db
            //System.out.println("thier is no supplier with this id in the db"+supplier_id);
            return false ;
        }
        //there is a supplier like that
        //check if their item like that in the db
        ProductDTO productDTO =ProductService.get_product_by_Id(ItemId).isError() ? null : (ProductDTO) ProductService.get_product_by_Id(ItemId).getMessage();
        if(productDTO==null){
            //System.out.println("thier is no item with this id in the db"+ItemId+" supplier id is "+supplier_id);
            return false ;//if there is no item with this id in the db

        }
                    //check if the item include in the agreement with the supplier
        return AgreementService.check_item_include_with_supplier_agreement(supplierDTO,productDTO);
    }

    public String CreateReport(String name, String description, java.sql.Date startDate, java.sql.Date endDate, int itemId, int defectiveQuantity) {
        return reportService.CreateReport(name,description,startDate,endDate,itemId,defectiveQuantity);
    }
    public boolean addSupplier(String supplierId, int id){
        return itemService.addSupplier(supplierId,id);
    }
    public boolean removeSupplier(String supplierId, int id){
        return itemService.removeSupplier(supplierId,id);
    }
    public boolean checkItemAgreement(String agid,String itemId){
        return AgreementService.checkItemAgreement(agid,itemId);
    }
    ///////////////////////////////////////////////////////////////////  Function to Add : Get_NexDay(...) , When there is alert we need to call upon AddOrder
    ///////


}
