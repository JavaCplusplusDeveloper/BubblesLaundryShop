
package BubblesLaundrySystem.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Data
@Table(name = "inventory_items")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class InventoryItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    private String itemName;
    private Double price;
    private Integer quantity;
    private LocalDateTime lastRestocked;

    
    
    public void setLastRestocked(LocalDateTime lastRestocked) {
      this.lastRestocked = lastRestocked;
    }
    public int getStockPercentage(){
      if (this.quantity==null|| this.quantity ==0 ){return 0;}
            
      int maxQuantity =100;
      return (int) Math.round(((double)this.quantity/maxQuantity)*100);
}
    
    public LocalDateTime getLastRestocked() {
      return lastRestocked;
    }

 
   
    
    
}
