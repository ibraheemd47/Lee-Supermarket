package ObjectDTO.Inventory;

import java.time.Instant;
import java.util.Date;
public record ItemReportDTO(Integer report_id, String name, String description, Instant startDate, Instant endDate, Integer item_id, Integer defectiveQuantity) {}