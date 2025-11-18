package domainLayer.Supplier.Objects;
public class Discount {
    private int quantity;
    private double discount_percentage;

    public Discount() {
    }

    public Discount(int quantity , double discount_percentage) {
        this.quantity = quantity;
        this.discount_percentage = discount_percentage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(double discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String toString() {
        return "Discount [quantity=" + quantity + ", discount_percentage=" + discount_percentage + "]";
    }

}