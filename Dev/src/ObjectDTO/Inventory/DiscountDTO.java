package ObjectDTO.Inventory;

import java.time.Instant;
import java.util.Date;

public record DiscountDTO(Integer id, String name, String type , double discountPercentage, Instant startDate, Instant endDate) {
}