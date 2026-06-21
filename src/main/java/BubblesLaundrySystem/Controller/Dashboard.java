
package BubblesLaundrySystem.Controller;

import BubblesLaundrySystem.Model.Transaction;
import BubblesLaundrySystem.Service.InventoryItemsS;
import BubblesLaundrySystem.Service.TransactionsS;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class Dashboard {
    @Autowired
    private InventoryItemsS inventoryService;
    
    @Autowired 
    private TransactionsS transactionService;
    
    @GetMapping("/")
    public String showDashboard(Model model){
        List<Transaction> transactions = transactionService.getAllTransaction();
        List<Transaction> pendingList = transactionService.getTransactionByStatus("Pending");
        List<Transaction> washingList = transactionService.getTransactionByStatus("Washing");
        List<Transaction> doneList = transactionService.getTransactionByStatus("Done");

        long pendingCount = transactions.stream().filter(tx -> "Pending".equalsIgnoreCase(tx.getStatus())).count();
        long washingCount = transactions.stream().filter(tx -> "Washing".equalsIgnoreCase(tx.getStatus())).count();
        long doneCount = transactions.stream().filter(tx -> "Done".equalsIgnoreCase(tx.getStatus())).count();
        
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("pendingTransactions", pendingList);
        model.addAttribute("washingCount", washingCount);
        model.addAttribute("washingTransactions", washingList);
        model.addAttribute("doneCount", doneCount);
        model.addAttribute("doneTransactions", doneList);
        model.addAttribute("transactions", transactions);
        model.addAttribute("inventory", inventoryService.getAllItems());
        return "dashboard";
    }

    @GetMapping("/transaction/start/{id}")
    public String startWash(@PathVariable Integer id){
        Transaction tx = transactionService.getAllTransaction().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
           
        if(tx != null){
            tx.setStatus("Washing");
            transactionService.saveTransaction(tx);
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/transaction/done/{id}")
    public String markAsDone(@PathVariable Integer id){
        Transaction tx = transactionService.getAllTransaction().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
        
        if (tx != null){
            tx.setStatus("Done");
            transactionService.saveTransaction(tx);
        }
        return "redirect:/";
    }
   
    @GetMapping("/transaction/delete/{id}")
    public String deleteTransaction(@PathVariable Integer id) {
        Transaction tx = transactionService.getAllTransaction().stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
                
        if (tx != null) {
            transactionService.deleteTransaction(id) ;
           
        }
        return "redirect:/"; 
    }

    @PostMapping("/transaction/update/{id}")
    public String updateTransaction(@PathVariable Integer id, @ModelAttribute Transaction updatedTx) {
            
      transactionService.updateTransaction_AdjustStock(id, updatedTx);
      System.out.println("updatedTransaction");
        
        return "redirect:/";
        
    }
   
    @PostMapping("/transaction/create")
    public String createNewOrder(@ModelAttribute Transaction transaction) {
        System.out.println("Form Data Received -> " + transaction.getDetergentQty() + transaction.getBleachQty() + transaction.getFabconQty());
        
        transactionService.createTransaction(transaction);
        return "redirect:/"; 
    }
}
