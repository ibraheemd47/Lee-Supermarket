package domainLayer.Supplier.Objects;


class PurchaseItem {
    SupItem supItem;
    int quantity;
    double normalPrice;
     double lastPrice;
     double discount ;
     public PurchaseItem(SupItem supItem, int quantity, double lastPrice, double discount,double normalPrice) {
         this.supItem = supItem;
         this.quantity = quantity;
         this.lastPrice = lastPrice;
         this.discount = discount;
         this.normalPrice = normalPrice;

     }
     public SupItem getSupItem() {
         return supItem;
     }
     public void setSupItem(SupItem supItem) {
         this.supItem = supItem;
     }
     public int getQuantity() {
         return quantity;
     }
     public void setQuantity(int quantity) {
         this.quantity = quantity;
     }
     public double getLastPrice() {
         return lastPrice;
     }
     public void setLastPrice(double lastPrice) {
         this.lastPrice = lastPrice;
     }
     public double getDiscount() {
         return discount;
     }
     public void setDiscount(double discount) {
         this.discount = discount;
     }
     public double getNormalPrice() {
         return normalPrice;
     }
     public void setNormalPrice(double normalPrice) {
         this.normalPrice = normalPrice;
     }
    @Override
    public String toString() {
        return "PurchaseItem{" +
                "supItem=" + (supItem != null ? supItem.toString() : "null") +
                ", quantity=" + quantity +
                ", normalPrice=" + normalPrice +
                ", lastPrice=" + lastPrice +
                ", discount=" + discount +
                '}';
    }


}