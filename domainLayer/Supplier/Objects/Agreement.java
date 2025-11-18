package domainLayer.Supplier.Objects;

import ObjectDTO.Supplier.AgreementDTO;
import ObjectDTO.Supplier.SupItemDTO;

import java.util.Map;
import java.util.stream.Collectors;

public class Agreement {
    private String agreementId;
    private String supplierId;
    private Map<String, SupItem> items;//<id , supitem>
    private boolean supplierDeliverIt;
    private String payment;

    public Agreement(String agreementId , String supplierId, Map<String,SupItem> items, boolean supplierDeliverIt , String payment) {
        this.agreementId = agreementId;
        this.supplierId = supplierId;
        this.items = items;
        this.supplierDeliverIt = supplierDeliverIt;
        this.payment = payment;
    }

    public Agreement(AgreementDTO agreement) {
        this.agreementId=agreement.agreementId();
        this.supplierId=agreement.supplierId();
        this.supplierDeliverIt=agreement.supplierDeliverIt();
        this.payment=agreement.payment();
        this.items=agreement.items().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,entry->new SupItem(entry.getValue())));
    }
    public AgreementDTO toDTO() {
        Map<String, SupItemDTO> itemDTOs = items.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().toDTO()
                ));

        return new AgreementDTO(
                agreementId,
                supplierId,
                itemDTOs,
                supplierDeliverIt,
                payment
        );
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
    public String getPayment() {
        return payment;
    }

    public String getAgreementId() {
        return agreementId;
    }
    public String getSupplierId() {
        return supplierId;
    }
    public Map<String,SupItem> getItems() {
        return items;
    }
    public boolean isSupplierDeliverIt() {
        return supplierDeliverIt;
    }
    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    public void setItems(Map<String,SupItem> items) {
        this.items = items;
    }
    public void setSupplierDeliverIt(boolean supplierDeliverIt) {
        this.supplierDeliverIt = supplierDeliverIt;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Agreement ID: ").append(agreementId).append("\n");
        sb.append("Supplier ID: ").append(supplierId).append("\n");
        sb.append("Supplier Delivers: ").append(supplierDeliverIt ? "Yes" : "No").append("\n");
        sb.append("Payment: ").append(payment).append("\n");
        sb.append("Items:\n");
//        for (SupItem item : items) {
//            sb.append("  - ").append(item.toString()).append("\n");
//        }
        for(Map.Entry<String,SupItem> entry : items.entrySet()){
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }


    public void clearItems() {
        items.clear();
    }



    public void addItem(String itemID, SupItem toAdd) {
        items.put(itemID , toAdd);
    }

    public boolean containItem(String itemID) {
        return items.containsKey(itemID);
    }

    public double getItemPrice(int quntity , String productId) {
        return items.get(productId).getPriceAfterDiscount(quntity);

    }
}