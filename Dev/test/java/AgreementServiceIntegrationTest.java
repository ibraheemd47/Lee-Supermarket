import ObjectDTO.Supplier.*;
import ServiceLayer.Supplier.AgreementService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AgreementServiceIntegrationTest {

    AgreementService agreementService;

    @BeforeAll
    public void setup() {
        agreementService = AgreementService.getInstance();
    }

    @Test
    public void testAddGetDeleteAgreement() {
        
        List<DiscountDTO> quantityDiscounts = new ArrayList<>();
        quantityDiscounts.add(new DiscountDTO(10, 10));

        Map<String, SupItemDTO> items = Map.of("item01", new SupItemDTO(new ProductDTO("1","milk"),new ManufacturerDTO("0","tnova"),"item01", "Item Name", 10, "1", quantityDiscounts));
        
        // Setup items list for AgreementDTO if needed (dummy or real items)
        AgreementDTO agreement = new AgreementDTO("ag01", "sup01", items, false,"cash");

        agreementService.add_agreement(agreement);

        List<AgreementDTO> agreements = agreementService.getAllAgreementOf("sup01").isError()?null:(List<AgreementDTO>) agreementService.getAllAgreementOf("sup01").getMessage();
        assertNotNull(agreements);
        assertTrue(agreements.stream().anyMatch(a -> a.agreementId().equals("ag01")));

        AgreementDTO fetched = agreementService.getAgreement("ag01").isError()?null:(AgreementDTO) agreementService.getAgreement("ag01").getMessage();
        assertNotNull(fetched);
        assertEquals("sup01", fetched.supplierId());

        boolean deleted = agreementService.delete_agreement("ag01");
        assertTrue(deleted);

        AgreementDTO afterDelete = agreementService.getAgreement("ag01").isError()?null:(AgreementDTO) agreementService.getAgreement("ag01").getMessage();
        assertNull(afterDelete);
    }

    // Add more tests for updateDelivery, addItem, removeItems etc. similarly
}