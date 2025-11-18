package ObjectDTO.Inventory;

import java.util.List;

public record ParentCategoryDTO(int ParentCategoryId, List<Integer> cayegoriesIds,Integer parentL,Integer sonL) {
}
