package ObjectDTO.Inventory;

import java.time.Instant;
import java.util.Date;

public record ReportDTO(Integer id, String name, String description, Date startDate, Date endDate){}