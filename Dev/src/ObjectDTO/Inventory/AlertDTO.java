package ObjectDTO.Inventory;

import java.time.Instant;
import java.util.Date;

public record AlertDTO(Integer id, String message, String type, Instant dateCreated, int item) {
}
