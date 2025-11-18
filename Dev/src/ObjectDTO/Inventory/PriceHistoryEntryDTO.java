package ObjectDTO.Inventory;

import java.time.Instant;
import java.time.LocalDate;

public record PriceHistoryEntryDTO (double sellingPrice, double buyingPrice, Instant date) {
}
