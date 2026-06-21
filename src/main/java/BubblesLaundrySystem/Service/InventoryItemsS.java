
package BubblesLaundrySystem.Service;
import BubblesLaundrySystem.Model.InventoryItem;
import BubblesLaundrySystem.Repository.InventoryItemR;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryItemsS {
     @Autowired
      private InventoryItemR inventoryItemRepository;
      
     public List <InventoryItem>getAllItems(){
      return inventoryItemRepository.findAll();
    }
      public InventoryItem saveItem(InventoryItem item){
      return inventoryItemRepository.save(item);
      }
      
       public void  deleteId(Integer id){
        inventoryItemRepository.deleteById(id);
    }
    public void deductStock(String itemName, int amountToDeduct) {
        
        InventoryItem item = getAllItems().stream().filter(i -> itemName.equalsIgnoreCase(i.getItemName())).findFirst().orElse(null);
                
        
        if (item != null) {
            int currentQty;
            int newQty;
            
            if(item.getQuantity() != null){
                currentQty= item.getQuantity();
            }
            else{
                currentQty =0;
            }
            
            newQty = Math.max(0, currentQty - amountToDeduct);
            item.setQuantity(newQty);
            saveItem(item); // Saves the updated quantity to MySQL
        }
    }

    public void adjustAddonStock(String itemName, int difference) {
     
                
      if(difference == 0)return;
      
      InventoryItem item = getAllItems().stream().filter(i -> itemName.equalsIgnoreCase(i.getItemName())).findFirst().orElse(null);
      
      if (item !=null){
      int newQy = item.getQuantity() - difference;
      item.setQuantity(Math.max(0, newQy));
      saveItem(item);
      }
    }

    public InventoryItem getItemById(Integer id) {
     return inventoryItemRepository.findById(id).orElse(null);
    }
    
}


