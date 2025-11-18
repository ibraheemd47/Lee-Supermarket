package ObjectDTO.Supplier;

import java.util.List;

//the link between product table and supplier
public class SupItemDTO {
    ManufacturerDTO manufacturer;
    private ProductDTO product;
    //private String productName;
    private String supplierId;
    private double price;
    private String supplierCatNum; // serial number according to supplier
    private List<DiscountDTO> quantityDiscounts;
    String agreementId;
    public SupItemDTO(ProductDTO product, ManufacturerDTO manufacturer, String supplierId, String agreementId,
                      double price, String supplierCatNum, List<DiscountDTO> quantityDiscounts) {
        this.product = product;
        this.agreementId = agreementId;
        this.manufacturer = manufacturer;
        //this.productName = productName;
        this.supplierId = supplierId;
        this.price = price;
        this.supplierCatNum = supplierCatNum;
        this.quantityDiscounts = quantityDiscounts;
    }

    public ManufacturerDTO getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerDTO manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public double getPrice() {
        return price;
    }
    public double getPrice(int quntity) {
        double discountPerc = price;
        for (DiscountDTO discount : quantityDiscounts) {
            if (discount.getQuantity() <= quntity) {
                discountPerc = discount.getDiscountPercentage();
            }
        }
        return (1-discountPerc)*price ;

    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSupplierCatNum() {
        return supplierCatNum;
    }

    public void setSupplierCatNum(String supplierCatNum) {
        this.supplierCatNum = supplierCatNum;
    }

    public List<DiscountDTO> getQuantityDiscounts() {
        return quantityDiscounts;
    }

    public void setQuantityDiscounts(List<DiscountDTO> quantityDiscounts) {
        this.quantityDiscounts = quantityDiscounts;
    }

    @Override
    public String toString() {
        return "SupItemDTO{" +
                "product=" + product +
                ", supplierId='" + supplierId + '\'' +
                ", price=" + price +
                ", supplierCatNum='" + supplierCatNum + '\'' +
                ", quantityDiscounts=" + quantityDiscounts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupItemDTO)) return false;
        SupItemDTO that = (SupItemDTO) o;
        return Double.compare(that.price, price) == 0 &&
                java.util.Objects.equals(product, that.product) &&
                java.util.Objects.equals(supplierId, that.supplierId) &&
                java.util.Objects.equals(supplierCatNum, that.supplierCatNum);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(product, supplierId, price, supplierCatNum);
    }

    public ProductDTO getproduct() {
        return product;
    }

    public String getAgreementId() {
        return agreementId;
    }
}

