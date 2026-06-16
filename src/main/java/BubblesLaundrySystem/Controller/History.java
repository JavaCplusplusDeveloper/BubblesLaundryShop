
package BubblesLaundrySystem.Controller;

import BubblesLaundrySystem.Model.InventoryItem;
import BubblesLaundrySystem.Model.Transaction;
import BubblesLaundrySystem.Service.InventoryItemsS;
import BubblesLaundrySystem.Service.TransactionsS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class History {
    @Autowired
    private TransactionsS transactionService;
    @Autowired
    private InventoryItemsS inventoryService;
    
    @GetMapping("/history")
    public String historyTab(Model model){
    List<InventoryItem>item = inventoryService.getAllItems();
    List<Transaction>transactions = transactionService.getAllTransaction();
    model.addAttribute("items", item);
    model.addAttribute("transaction", transactions);
    
    return "history";
    }
    
    
    @GetMapping("/historyB")
    public String historyTabB (Model model){
    List<InventoryItem>item = inventoryService.getAllItems();
    List<Transaction>transactions = transactionService.getAllTransaction();
    
    model.addAttribute("items", item);
    model.addAttribute("transaction", transactions);
    
    return "historyB";
    }
    
    
    @PostMapping("/history/update/{id}")
    public String ViewDeductionsA(Model model){
    List<InventoryItem>item = inventoryService.getAllItems();
    List<Transaction>transactions = transactionService.getAllTransaction();
    
    model.addAttribute("items", item);
    model.addAttribute("transaction", transactions);
    return "redirect:/history";    }
    
}
