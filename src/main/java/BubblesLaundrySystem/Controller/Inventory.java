
package BubblesLaundrySystem.Controller;

import BubblesLaundrySystem.Model.InventoryItem;
import BubblesLaundrySystem.Service.InventoryItemsS;
import BubblesLaundrySystem.Service.TransactionsS;
import BubblesLaundrySystem.Model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@Controller
public class Inventory {
    static int bleachLimit;
    static int fabconLimit;
    static int detergentLimit;
    
    @Autowired
    private TransactionsS transactionService;
    @Autowired
    private InventoryItemsS inventoryService;
    
    
    @GetMapping("/inventory")
    public String inventoryTab(Model model){
    List<InventoryItem> items = inventoryService.getAllItems();
    List<Transaction> transactions = transactionService.getAllTransaction();


    long lowStockCount = items.stream().filter(item ->{
        
        
        if (item.getQuantity() == null) return true;
        if (item.getItemName().toLowerCase().contains("detergent") || item.getItemName().toLowerCase().contains("ariel")){
            return item.getQuantity() < detergentLimit;
        
        }else if(item.getItemName().toLowerCase().contains("fabcon") || item.getItemName().toLowerCase().contains("fabric")){
            return item.getQuantity() < fabconLimit;
            
        }else if(item.getItemName().toLowerCase().contains("bleach")){
            return item.getQuantity() < bleachLimit;
        }
        return item.getQuantity() < 10; 
        
        }).count();

        int totalDetergent = 0;
        int totalFabcon = 0;
        int totalBleach = 0;

        for (Transaction t : transactions){
   
        if (t.getDetergentQty() != null) {
        totalDetergent += t.getDetergentQty();
         }else{
        totalDetergent += 0; 
    }

   
        if (t.getFabconQty() != null){
        totalFabcon += t.getFabconQty();
       }else{
        totalFabcon += 0; 
       }

    
       if (t.getBleachQty() != null) {
        totalBleach += t.getBleachQty();
       }else{
        totalBleach += 0; 
       }
     }
    int totalUsageCount = totalDetergent + totalFabcon + totalBleach;
    
    // model attributes
    model.addAttribute("items", items);
    model.addAttribute("transactions", transactions);
    model.addAttribute("totalDetergent", totalDetergent);
    model.addAttribute("totalFabcon", totalFabcon);
    model.addAttribute("totalBleach", totalBleach);
    model.addAttribute("usageTodayCount", totalUsageCount);
    
    // Dynamic alerts added to the model:
    model.addAttribute("lowStockCount", lowStockCount);
    model.addAttribute("detergentLimit", detergentLimit);
    model.addAttribute("fabconLimit", fabconLimit);
    model.addAttribute("bleachLimit", bleachLimit);
   
    return "inventory";
}
    
    
    @GetMapping("/inventory/restock/{id}")
    public String restockItem(@PathVariable("id") Integer id,@RequestParam("amount") int amount){
        InventoryItem item = inventoryService.getItemById(id);
        
        if(item !=null){
          int currentQty; 
          
          if (item.getQuantity()!= null){
              currentQty = item.getQuantity();
          }else{
              currentQty=0;
          }
          item.setQuantity(currentQty + amount);
          item.setLastRestocked(java.time.LocalDateTime.now());
          inventoryService.saveItem(item);
        }
        return "redirect:/inventory";
        }
        
     
        
}
