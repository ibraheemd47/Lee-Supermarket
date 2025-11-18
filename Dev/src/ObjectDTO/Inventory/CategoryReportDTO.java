package ObjectDTO.Inventory;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public record CategoryReportDTO(int reportId, List<Integer> categories_ids, String name, String description, Instant startDate, Instant endDate){}