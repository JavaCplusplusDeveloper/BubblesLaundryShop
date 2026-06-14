
package BubblesLaundrySystem.Repository;
import BubblesLaundrySystem.Model.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsR extends JpaRepository<Transaction,Integer> {

    public List<Transaction> findByStatus(String status);
    
}
