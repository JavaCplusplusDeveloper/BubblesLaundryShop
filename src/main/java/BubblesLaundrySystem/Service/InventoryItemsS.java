
package BubblesLaundrySystem.Service;
import BubblesLaundrySystem.Model.InventoryItem;
import BubblesLaundrySystem.Model.Transaction;
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
        
        InventoryItem item;
        
        for(int i = 0 ; i < getAllItems().size();i++){
            InventoryItem index = getAllItems().get(i);
            if(itemName.equalsIgnoreCase(index.getItemName())){

                item = index;
                
                int currentQty = 0;
                int newQty = 0;
            
                currentQty= item.getQuantity();
                newQty = Math.max(0, currentQty - amountToDeduct);
                item.setQuantity(newQty);
                saveItem(item); // Saves the updated quantity to MySQL
                break;
            }
        }
                
            
        
    }
    
    
    public void adjustAddonStock(String itemName, int difference) {
     
      InventoryItem item;

      for(int i=0;i<getAllItems().size(); i++){
        InventoryItem index = getAllItems().get(i);
        if(itemName.equalsIgnoreCase(index.getItemName())){
            item = index;
            int newQy = item.getQuantity() - difference;
            item.setQuantity(Math.max(0, newQy));
            saveItem(item);
            break;
        }
      }
      
    }

    public InventoryItem getItemById(Integer id) {
         
        return inventoryItemRepository.findById(id).orElse(null);

    }

    //counter of "low stock"
    public int getLowStockCount(List<InventoryItem> items, int detergentLimit, int fabconLimit, int bleachLimit) {
    int count = 0;
    
    for (int i = 0;i<items.size();i++) {
        InventoryItem item = items.get(i);
        
        int qty = item.getQuantity();

            if (qty < detergentLimit){count++;}
        
            if (qty < fabconLimit) count++;

            if (qty < bleachLimit) count++;

            if (qty < 10)count++;
        }
        return count;
    }
    
    

    public int getTotalDetergent(List<Transaction> transactions) {
    int total = 0;
    for(int i = 0; i < transactions.size(); i++){
        Transaction T = transactions.get(i);
        T.getDetergentQty();
    }
    
    return total;
    }   
    
    public int getTotalBleach(List<Transaction> transactions) {
    int total = 0;
    
    for(int i = 0; i< transactions.size();i++){
        Transaction T =transactions.get(i);
        T.getBleachQty();
    }
    return total;
    }

    public int getTotalFabcon(List<Transaction> transactions) {
    int total = 0;
    for(int i = 0; i<transactions.size();i++){
      Transaction t = transactions.get(i);
      t.getFabconQty();
    }
    System.out.println("the current total of fabcon -> "+total);
    return total;
    
    }


    public void restockInventory(Integer id,InventoryItem item,int amount){
        item = getItemById(id);

          int currentQty = 0;
          currentQty = item.getQuantity();
          
          item.setQuantity(currentQty + amount);
          
          item.setLastRestocked(java.time.LocalDateTime.now());
          saveItem(item);
        }
    

    }
    



