package ObjectDTO.Supplier;

import java.util.Map;

public record AgreementDTO(
        String agreementId,
        String supplierId,
        Map<String, SupItemDTO> items,
        boolean supplierDeliverIt,
        String payment) { }


