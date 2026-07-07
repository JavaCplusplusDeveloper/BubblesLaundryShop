
package BubblesLaundrySystem.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Data
@Table(name = "inventory_items")
@NoArgsConstructor
@AllArgsConstructor (access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
@NonNull // an explicit null check is also generated or making sure theres an object inside of the database
public class InventoryItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    private String itemName;
    private Double price;
    private Integer quantity;
    private LocalDateTime lastRestocked;

    
    

    public int getStockPercentage(){
      
            
      int maxQuantity =100;
      return quantity/maxQuantity;
}
    
  

 
   
    
    
}
