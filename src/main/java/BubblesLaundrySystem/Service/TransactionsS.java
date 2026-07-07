
package BubblesLaundrySystem.Service;

import BubblesLaundrySystem.Model.Transaction;
import BubblesLaundrySystem.Repository.TransactionsR;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@NonNull
public class TransactionsS {
     
   @Autowired
    private TransactionsR transactionRepository;
    
    @Autowired
    private InventoryItemsS inventoryService;
    
    public Transaction createTransaction(Transaction transaction){ transaction.setOrderDate(LocalDateTime.now());
    
    transaction.setStatus("Pending");
    
    int detergentUsed = 0;
    int bleachUsed = 0;
    int fabconUsed =0;
    
    detergentUsed = transaction.getDetergentQty();
    bleachUsed = transaction.getBleachQty();
    fabconUsed  = transaction.getFabconQty();
     
    inventoryService.deductStock("Detergent", detergentUsed);
    inventoryService.deductStock("Bleach", bleachUsed);
    inventoryService.deductStock("Fabcon", fabconUsed);
     
    return transactionRepository.save(transaction);}
    
    public List<Transaction>getAllTransaction(){
        return transactionRepository.findAll();
    }
    public Transaction getTransactionById(Integer Id){
        return transactionRepository.findById(Id).get();
    }
    
    
    public Transaction updateStatus(Integer id,String newStatus){
    
    Transaction transaction = getTransactionById(id);
    
    transaction.setStatus(newStatus); return transactionRepository.save(transaction);

    }
    
    
    public void deleteTransaction(Integer id){
        transactionRepository.deleteById(id);
    }

    
    public void saveTransaction(Transaction tx) {
    transactionRepository.save(tx);
    }
    

    public List<Transaction> getTransactionByStatus(String status) {
     return  transactionRepository.findByStatus(status);
    }
    

    //All Transactions and Addons will be adjusted
    public void updateTransaction_AdjustStock(Integer id, Transaction updatedTx) {
        Transaction tx = getTransactionById(id);
                
         
            // CALCULATE DIFFERENCES (New Form Quantity - Existing Database Quantity)
            
            int oldDetergent = 0;
            int newDetergent = 0; 
            int detergentDiff = 0;
            
            int oldFabcon = 0;
            int newFabcon = 0; 
            int fabconDiff = 0; 
            
            int oldBleach = 0;
            int newBleach = 0;
            int bleachDiff = 0;
            
            
            //addons comparing old and new
            oldDetergent = tx.getDetergentQty();
            newDetergent = updatedTx.getDetergentQty();
            detergentDiff = newDetergent - oldDetergent;

            oldFabcon = tx.getFabconQty();
            newFabcon = updatedTx.getFabconQty();
            fabconDiff = newFabcon - oldFabcon;
            
            oldBleach= tx.getBleachQty();
            newBleach= updatedTx.getBleachQty();
            bleachDiff = newBleach - oldBleach;


            // 2. Adjust stocks in theinventory
            inventoryService.adjustAddonStock("Detergent", detergentDiff);
            inventoryService.adjustAddonStock("Fabcon", fabconDiff);
            inventoryService.adjustAddonStock("Bleach", bleachDiff);


            // 3. updates all the variable inside of the reedit button
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


