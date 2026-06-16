
package BubblesLaundrySystem.Service;

import BubblesLaundrySystem.Model.Transaction;
import BubblesLaundrySystem.Repository.TransactionsR;
import jakarta.transaction.Transactional;
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
    
     @Transactional
    public void updateTransaction_AdjustStock(Integer id, Transaction updatedTx) {
        Transaction tx = getTransactionById(id);
                
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

            // 2. Adjust stock using your custom inventory rule handler
            inventoryService.adjustAddonStock("Detergent", detergentDiff);
            inventoryService.adjustAddonStock("Fabcon", fabconDiff);
            inventoryService.adjustAddonStock("Bleach", bleachDiff);

            // 3. Map the updated details back to the managed entity row
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
            
            transactionRepository.save(tx);
        }
    }
}

