package ObjectDTO.Supplier;

public class DiscountDTO {
    private int quantity;
    private double discountPercentage;

    public DiscountDTO(int quantity, double discountPercentage) {
        this.quantity = quantity;
        this.discountPercentage = discountPercentage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public String toString() {
        return "DiscountDTO{" +
                "quantity=" + quantity +
                ", discountPercentage=" + discountPercentage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountDTO)) return false;
        DiscountDTO that = (DiscountDTO) o;
        return quantity == that.quantity &&
                Double.compare(that.discountPercentage, discountPercentage) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(quantity, discountPercentage);
    }

}

