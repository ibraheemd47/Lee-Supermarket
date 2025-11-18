package domainLayer.Inventory;
import java.util.Date;


public class PriceHistoryEntry {
    private double sellingPrice; // Selling price of the item
    private double buyingPrice; // Buying price of the item
    private Date date; // Date when the price was recorded

    public PriceHistoryEntry(double sellingPrice, double buyingPrice, Date date) {
        this.sellingPrice = sellingPrice;
        this.buyingPrice = buyingPrice;
        this.date = date;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public Date getDate() {
        return date;
    }
    public String toString() {
        return "PriceHistoryEntry{" +
                "sellingPrice=" + sellingPrice +
                ", buyingPrice=" + buyingPrice +
                ", date=" + date +
                '}';
    }
}
