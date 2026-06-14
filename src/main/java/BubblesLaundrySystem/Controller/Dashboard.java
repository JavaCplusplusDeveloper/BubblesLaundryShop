
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
            transactionService.deleteTransaction(id);
        }
        return "redirect:/"; 
    }

    @PostMapping("/transaction/update/{id}")
    public String updateTransaction(@PathVariable Integer id, @ModelAttribute Transaction updatedTx) {
            
        Transaction tx = transactionService.getAllTransaction().stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
                
        if (tx != null) {
            // CALCULATE DIFFERENCES (New Form Quantity - Existing Database Quantity)
            
            int oldDetergent;
            int newDetergent; 
            int detergentDiff;
            
            int oldFabcon;
            int newFabcon; 
            int fabconDiff; 
            
            int oldBleach;
            int newBleach;
            int bleachDiff;
            
            
            if(tx.getDetergentQty() != null){
                oldDetergent = tx.getDetergentQty();
            }else{
                oldDetergent = 0;
            }
            if(updatedTx.getDetergentQty() != null){
                newDetergent = updatedTx.getDetergentQty();
            }else{
                newDetergent = 0;
            }
            detergentDiff = newDetergent - oldDetergent;

            
            if(tx.getFabconQty() != null){
                oldFabcon = tx.getFabconQty();
            }else{
                oldFabcon= 0;
            }
            
            if(updatedTx.getFabconQty() != null){
                newFabcon = updatedTx.getFabconQty();
            }else{
                newFabcon= 0;
            }
            
            fabconDiff = newFabcon - oldFabcon;

            if(tx.getBleachQty() != null){
                oldBleach= tx.getBleachQty();
            }else{oldBleach= 0;
            }
            
            if(updatedTx.getBleachQty() != null){newBleach= updatedTx.getBleachQty();}else{newBleach=0;}
            
             bleachDiff = newBleach - oldBleach;

            // adjust stock (if the user add or delete)
            inventoryService.adjustAddonStock("Detergent", detergentDiff);
            inventoryService.adjustAddonStock("Fabcon", fabconDiff);
            inventoryService.adjustAddonStock("Bleach", bleachDiff);

            
            tx.setCustomerName(updatedTx.getCustomerName());
            tx.setPhoneNumber(updatedTx.getPhoneNumber());
            tx.setWeight(updatedTx.getWeight());
            tx.setServiceMode(updatedTx.getServiceMode());
            tx.setEstimatedTime(updatedTx.getEstimatedTime()); 
            tx.setBleachQty(updatedTx.getBleachQty());
            tx.setDetergentQty(updatedTx.getDetergentQty());
            tx.setFabconQty(updatedTx.getFabconQty());
            tx.setTotalAmount(updatedTx.getTotalAmount());
            tx.setDeliveryAddress(updatedTx.getDeliveryAddress());
            tx.setAdressDescription(updatedTx.getAdressDescription());
            
            transactionService.saveTransaction(tx);
        }
        return "redirect:/";
    }
   
    @PostMapping("/transaction/create")
    public String createNewOrder(@ModelAttribute Transaction transaction) {
        System.out.println("Form Data Received -> " + transaction.getDetergentQty() + transaction.getBleachQty() + transaction.getFabconQty());
        
        transactionService.createTransaction(transaction);
        return "redirect:/"; 
    }
}
