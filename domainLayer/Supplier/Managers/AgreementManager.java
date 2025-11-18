package domainLayer.Supplier.Managers;

import ObjectDTO.Supplier.*;
import domainLayer.Supplier.Factory;
import domainLayer.Supplier.Objects.Agreement;
import domainLayer.Supplier.Objects.Discount;
import domainLayer.Supplier.Objects.SupItem;
import domainLayer.Supplier.Repository;
import domainLayer.Supplier.RepositoryIMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgreementManager {
    private final Map<String, Agreement> agreements;//<agreement id , agreement >
    Repository dataBase = RepositoryIMP.getInstance();

    public AgreementManager() {
        this.agreements = new HashMap<>();
    }

    public String addAgreement(AgreementDTO agreement) {
        if (agreement == null || agreement.agreementId() == null) {
            throw new IllegalArgumentException("Agreement or ID cannot be null.");
        }
        agreements.put(agreement.agreementId(), new Agreement(agreement));
        HashMap<String, SupItemDTO> items = new HashMap<>();
        for (Map.Entry<String, SupItemDTO> s : agreement.items().entrySet()) {
            SupItem sup = new SupItem(s.getValue());
            List<DiscountDTO> lstDis = new ArrayList<>();
            for (Discount d : sup.getQuantityForDiscount()) {
                lstDis.add(new DiscountDTO(d.getQuantity(), d.getDiscount_percentage()));
            }
            items.put(s.getKey(), new SupItemDTO(new ProductDTO(sup.getItem().getId(), sup.getItem().getName()), new ManufacturerDTO(sup.getManufacturer().getManfacturer_id(), sup.getManufacturer().getName()), sup.getSupplier_id(), agreement.agreementId(), sup.getPrice(), sup.getSupplierCatNum(), lstDis));
        }
        dataBase.insertAgreement(new AgreementDTO(agreement.agreementId(), agreement.supplierId(), items, agreement.supplierDeliverIt(), agreement.payment()));
        return "added agreement with id " + agreement.agreementId();
    }

    public boolean removeAgreement(String agreementId) {
        if(agreementId == null )
        {
            throw new IllegalArgumentException();
        }
        try {
            agreements.remove(agreementId);
            boolean before = dataBase.getAgreementById(agreementId) != null;
            dataBase.deleteAgreement(agreementId);
            boolean after = dataBase.getAgreementById(agreementId) == null;
            return before & after;
        } catch (Exception e) {
            return false;
        }
    }

    public AgreementDTO getAgreementDTOById(String agreementId) {
        return (getAgreementById(agreementId) != null) ? getAgreementById(agreementId).toDTO() : null;
    }

    public Agreement getAgreementById(String agreementId) {
        if (agreements.get(agreementId) != null)
            return agreements.get(agreementId);
        else {
            AgreementDTO agreementDTO = dataBase.getAgreementById(agreementId);
            if (agreementDTO != null)
                return new Agreement(agreementDTO);
            else
                return null;
        }
    }

    public List<Agreement> getAgreementsBySupplier(String supplierId) {
        List<Agreement> result = new ArrayList<>();
        for (Agreement a : agreements.values()) {
            if (a.getSupplierId().equals(supplierId)) {
                result.add(a);
            }
        }
        return result;
    }

    public List<AgreementDTO> getAgreementsDTOBySupplier(String supplierId) {
        HashMap<String, AgreementDTO> agreement = dataBase.getAgreementsBySupplierId(supplierId);
        List<AgreementDTO> result = new ArrayList<>();
        for (var x : agreement.entrySet()) {
            result.add(x.getValue());
            agreement.put(x.getKey(), x.getValue());
        }

        return result;
    }


    public List<Agreement> getAllAgreements() {
        ArrayList list = new ArrayList<>();
        for (AgreementDTO a :dataBase.getAllAgreements().values()) {
            list.add(new Agreement(a));
            agreements.put(a.agreementId(), new Agreement(a));
        }
        return list;
        //return new ArrayList<>(dataBase.getAllAgreements().values());
    }

    public boolean containsAgreement(String agreementId) {
        ////System.out.printlnlnln(agreements.toString());
        if( agreements.containsKey(agreementId))
        {
            return true;
        }
        else {
            AgreementDTO agreementDTO = dataBase.getAgreementById(agreementId);
            if (agreementDTO != null){
                return true;}
            else
                return false;
        }
    }

    public void clearAll() {
        agreements.clear();
        dataBase.clearAllAgreementes();
    }

    /**
     * checking if the product is in the agreement
     *
     * @param productId  the product serial number
     * @param supplierId the supplier id
     * @return null if not Product is in
     */
    public SupItemDTO checkProductBySerialNumber(String productId, String supplierId) {
        Map<String, SupItemDTO> supItemMap = dataBase.getSupItemsBySupplierId(supplierId);
        for (SupItemDTO supItem : supItemMap.values()) {
            if (supItem.getproduct().getId().equals(productId))
                return supItem;
        }
        return null;
    }

    /**
     * checking if the product is in the agreement
     *
     * @param productname the product name
     * @param supplierId  the supplier id
     * @return null if not Product is in
     */
    public SupItemDTO checkProductByname(String productname, String supplierId) {
        Map<String, SupItemDTO> supItemMap = dataBase.getSupItemsBySupplierId(supplierId);
        for (SupItemDTO supItem : supItemMap.values()) {
            if (supItem.getproduct().getName().equals(productname))
                return supItem;
        }
        return null;
    }

    public String updateDeliveryOfSupplier(String agID, boolean supplierDeliverIt) {
        getAgreementById(agID).setSupplierDeliverIt(supplierDeliverIt);

        //update in the database
        AgreementDTO agreementDTO = getAgreementById(agID).toDTO();
        AgreementDTO updatedAgreementDTO = new AgreementDTO(agID, agreementDTO.supplierId(), agreementDTO.items(), supplierDeliverIt, agreementDTO.payment());
        ////System.out.printlnln("insert to update the data base " + updatedAgreementDTO.toString());
        dataBase.updateAgreement(updatedAgreementDTO);
        return "updated delivery of supplier with id " + agID;
    }

    public String removeItemsOF(String agreementId) {
        getAgreementById(agreementId).clearItems();

        //remove from the database
//        AgreementDTO agreementDTO=getAgreementById(agreementId).toDTO();
//        AgreementDTO new_agreementDTO=new AgreementDTO(agreementDTO.agreementId(),agreementDTO.supplierId(),new HashMap<>(),agreementDTO.supplierDeliverIt(),agreementDTO.payment());
        dataBase.removeAllAgreementItems(agreementId);
        return "removed all items of agreement with id " + agreementId;
    }

    public String addItemToAgreement(String agID, String itemID, SupItemDTO toAdd) {
        //System.out.printlnln("the item that i get "+toAdd);
        if (!containsAgreement(agID)) {
            throw new IllegalArgumentException("Agreement does not exist.");
        }
        Agreement a = getAgreementById(agID);
        a.addItem(itemID, new SupItem(toAdd));

        //add to the database
        AgreementDTO agreementDTO = a.toDTO();
//        //System.out.printlnln("adding the item to the database " + agreementDTO.toString());
        dataBase.updateAgreement(agreementDTO);
        return "added item with id " + itemID + " to agreement with id " + agID;
    }

    public String updatePaymentType(String aglID, String payment) {
        getAgreementById(aglID).setPayment(payment);

        //update in the database
        AgreementDTO agreementDTO = getAgreementById(aglID).toDTO();
        AgreementDTO updatedAgreementDTO = new AgreementDTO(aglID, agreementDTO.supplierId(), agreementDTO.items(), agreementDTO.supplierDeliverIt(), payment);
//        //System.out.printlnln("insert to update the data base " + updatedAgreementDTO.toString());
        dataBase.updateAgreement(updatedAgreementDTO);
        return "updated payment type of agreement with id " + aglID;
    }

    /**
     * @return the best agreement (lowest total price) that covers all the products in the list.
     */
    public AgreementDTO get_best_deal(List<Pair<Integer, ProductDTO>> ls) {
        double best_price = Double.MAX_VALUE;
        Agreement best_agreement = null;
        List<Agreement> agreements = Factory.getInstance().getAgreementManager().getAllAgreements();

        //System.out.printlnln("get best deal " + agreements.toString() + "  sup item list" );

       // ls.forEach(x->//System.out.printlnln("product  "+x.second+" quantity "+x.first));
        for (Agreement agreement : agreements) {
            double newPrice = 0;
            boolean allFound = true;
            //System.out.printlnln("checking the agreement " + agreement.toString() + "  sup item list" );
            //System.out.printlnln("the item is "+agreement.getItems());

            for (Pair<Integer, ProductDTO> p : ls) {
                ProductDTO product = p.second;
                int quantity = p.first;

                if (agreement.containItem(product.getId())) {
                    //System.out.printlnln("find one");
                    double add = agreement.getItemPrice(quantity, product.getId());
                    newPrice += add;
                } else {
                    allFound = false;
                    break;
                }
            }

            if (allFound && newPrice < best_price) {
                best_price = newPrice;
                best_agreement = agreement;
            }
            else{

            }
        }

        if (best_agreement == null) {
            return null;
        }

        return best_agreement.toDTO();
    }

    public SupItemDTO remove_item_from_agreement(String agreementId, String productID) {
        //remove from the map
        if(agreements.get(agreementId)!=null)
            agreements.get(agreementId).getItems().remove(productID);

        //remove from the data base
        return dataBase.remove_item_from_agreement(agreementId,productID);
    }

    public boolean check_item_include_with_supplier_agreement(SupplierDTO supplierDTO, ProductDTO productDTO) {
       /// check first in the map
        for (var agreement: agreements.entrySet()) {
            if(agreement.getValue().getSupplierId().equals(supplierDTO.getId()) && agreement.getValue().containItem(productDTO.getId()) )
                return true;

        }
        //return false;

        //else not exist in the map check in the database
        Map<String, SupItemDTO> supItemMap = dataBase.getSupItemsBySupplierId(supplierDTO.getId());
        for (SupItemDTO supItem : supItemMap.values()) {
            if (supItem.getproduct().getName().equals(productDTO.getId()))
                return true;
        }
        return false;
    }

    public boolean isProudctExist(String aglID, String productID) {
        if(agreements.get(aglID)!=null)
            return true;
        return dataBase.getAgreementById(aglID).items().containsKey(productID);
    }

//    private OrderDTO do_order_with_sup(Agreement bestAgreement, List<Pair<Integer, ProductDTO>> ls) {
////        Supplier supplier = Factory.getInstance().getSupplierManager().getSupplierById(bestAgreement.getSupplierId());
////        OrderDTO order = new OrderDTO("make it autmatic",supplier.getName(),supplier.getId(),listItem,supplier.getContact().get(0),
////                LocalDate.now(),)
////    }
//    }
}

