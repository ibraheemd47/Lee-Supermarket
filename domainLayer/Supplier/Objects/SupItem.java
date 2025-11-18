package domainLayer.Supplier.Objects;

import ObjectDTO.Supplier.DiscountDTO;
import ObjectDTO.Supplier.ManufacturerDTO;
import ObjectDTO.Supplier.ProductDTO;
import ObjectDTO.Supplier.SupItemDTO;

import java.util.List;
import java.util.stream.Collectors;

//add the supplier id field
public class SupItem {
    String agreementId;
    private Product item;
    private double price;
    private List<Discount> quantityForDiscount;
    private Manufacturer manufacturer;
    private String supplierCatNum;//serial number
    private String supplier_id;

    public SupItem(Product item, String supplier_id, String agreementId, double price, List<Discount> quantityForDiscount, Manufacturer manufacturer, String supplierCatNum) {
        this.supplier_id = supplier_id;
        this.agreementId = agreementId;
        this.item = item;
        this.price = price;
        this.quantityForDiscount = quantityForDiscount;
        this.manufacturer = manufacturer;
        this.supplierCatNum = supplierCatNum;
    }

    public SupItem(SupItemDTO dto) {
        this.item = new Product(dto.getproduct());
        this.supplier_id = dto.getSupplierId();
        this.price = dto.getPrice();
        this.supplierCatNum = dto.getSupplierCatNum();
        this.agreementId = dto.getAgreementId();

        if (dto.getQuantityDiscounts() != null) {
            this.quantityForDiscount = dto.getQuantityDiscounts().stream()
                    .map(d -> new Discount(d.getQuantity(), d.getDiscountPercentage()))
                    .collect(Collectors.toList());
        } else {
            this.quantityForDiscount = null;
        }

        this.manufacturer = new Manufacturer(dto.getManufacturer().manufacturer_id(), dto.getManufacturer().manufacturer_name());
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product: ").append(item != null ? item.toString() : "N/A").append(", ");
        sb.append("Price: ").append(price).append(", ");
        sb.append("Discounts: [");
        if (quantityForDiscount != null && !quantityForDiscount.isEmpty()) {
            for (int i = 0; i < quantityForDiscount.size(); i++) {
                sb.append(quantityForDiscount.get(i).toString());
                if (i < quantityForDiscount.size() - 1) sb.append(", ");
            }
        } else {
            sb.append("None");
        }
        sb.append("]");
        return sb.toString();
    }

    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Discount> getQuantityForDiscount() {
        return quantityForDiscount;
    }

    public void setQuantityForDiscount(List<Discount> quantityForDiscount) {
        this.quantityForDiscount = quantityForDiscount;
    }

    public String getSupplierCatNum() {
        return supplierCatNum;
    }

    public void setSupplierCatNum(String supplierCatNum) {
        this.supplierCatNum = supplierCatNum;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getPriceAfterDiscount(int quantity) {
       // quantityForDiscount.forEach(v-> System.out.println(v) );
        double discountPercentage = 0.0;
        for (Discount discount : quantityForDiscount) {
            if (discount.getQuantity() <= quantity) {
                discountPercentage = Math.max(discountPercentage, discount.getDiscount_percentage());
            }
        }
        return price * (1 - discountPercentage );
    }

    public SupItemDTO toDTO() {
        List<DiscountDTO> discountDTOs = null;
        if (quantityForDiscount != null) {
            discountDTOs = quantityForDiscount.stream()
                    .map(discount -> new DiscountDTO(discount.getQuantity(), discount.getDiscount_percentage()))
                    .collect(Collectors.toList());
        }


        return new SupItemDTO(
                new ProductDTO(item.getId(), item.getName()),
                new ManufacturerDTO(manufacturer.getManfacturer_id(), manufacturer.getName()),
                supplier_id,
                agreementId,
                price,
                supplierCatNum,
                discountDTOs
        );
    }
}