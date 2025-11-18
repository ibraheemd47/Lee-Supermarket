package ObjectDTO.Inventory;

import java.util.List;

public record CategoryDTO(Integer id,Integer parentId ,String name, int quantity, int value ) {
}
