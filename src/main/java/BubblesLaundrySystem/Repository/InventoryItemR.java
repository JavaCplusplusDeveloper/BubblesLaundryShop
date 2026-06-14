
package BubblesLaundrySystem.Repository;
import BubblesLaundrySystem.Model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemR extends JpaRepository<InventoryItem,Integer>{


    
}
