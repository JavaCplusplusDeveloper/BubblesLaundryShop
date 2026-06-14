
package BubblesLaundrySystem.Service;

import BubblesLaundrySystem.Model.Transaction;
import BubblesLaundrySystem.Repository.TransactionsR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionsS {
     
   @Autowired
    private TransactionsR transactionRepository;
    
    @Autowired
    private InventoryItemsS inventoryService;
    
    public Transaction createTransaction(Transaction transaction){ transaction.setOrderDate(LocalDateTime.now());
    
    if (transaction.getStatus()==null){
      transaction.setStatus("Pending");
    }
    
    int detergentUsed;
    int bleachUsed;
    int fabconUsed;
    
    if (transaction.getDetergentQty() != null){
        detergentUsed = transaction.getDetergentQty();
    }else{
        detergentUsed = 0;
    }
     if (transaction.getBleachQty() != null){
         bleachUsed = transaction.getBleachQty();
    }else{
         bleachUsed = 0;
     }
     if(transaction.getFabconQty() != null) { fabconUsed  = transaction.getFabconQty();
     }else{ 
         fabconUsed = 0;
     }
     
     inventoryService.deductStock("Detergent", detergentUsed);
     inventoryService.deductStock("Bleach", bleachUsed);
     inventoryService.deductStock("Fabcon", fabconUsed);
     
    return transactionRepository.save(transaction);}
    
    public List<Transaction>getAllTransaction(){
        return transactionRepository.findAll();
    }
    public Transaction getTransactionById(Integer Id){
        return transactionRepository.findById(Id).orElse(null);
    }
    
    
    public Transaction updateStatus(Integer id,String newStatus){
    Transaction transaction = getTransactionById(id);
    if (transaction != null){transaction.setStatus(newStatus); return transactionRepository.save(transaction);}return null;}
    
    
    public void deleteTransaction(Integer id){
        transactionRepository.deleteById(id);
    }

    
    public void saveTransaction(Transaction tx) {
    transactionRepository.save(tx);
    }
    

    public List<Transaction> getTransactionByStatus(String status) {
     return  transactionRepository.findByStatus(status);
    }
}
