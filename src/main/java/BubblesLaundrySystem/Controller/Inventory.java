
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

        int lowStockCount = inventoryService.getLowStockCount(items, detergentLimit, fabconLimit, bleachLimit);
        int totalDetergent = inventoryService.getTotalDetergent(transactions);
        int totalFabcon = inventoryService.getTotalFabcon(transactions);
        int totalBleach = inventoryService.getTotalBleach(transactions);

        int totalUsageCount = totalDetergent + totalFabcon + totalBleach;

        model.addAttribute("items", items);
        model.addAttribute("transactions", transactions);
        model.addAttribute("totalDetergent", totalDetergent);
        model.addAttribute("totalFabcon", totalFabcon);
        model.addAttribute("totalBleach", totalBleach);
        
        model.addAttribute("usageTodayCount", totalUsageCount);
        model.addAttribute("lowStockCount", lowStockCount);

        model.addAttribute("detergentLimit", detergentLimit);
        model.addAttribute("fabconLimit", fabconLimit);
        model.addAttribute("bleachLimit", bleachLimit);
    
        return "inventory";
}
    
    
    @GetMapping("/inventory/restock/{id}")
    public String restockItem(@PathVariable("id") Integer id,InventoryItem inventory, @RequestParam("amount") int amount){
        inventoryService.restockInventory(id, inventory,amount);
    
        return "redirect:/inventory";
        }
        
     
        
}
