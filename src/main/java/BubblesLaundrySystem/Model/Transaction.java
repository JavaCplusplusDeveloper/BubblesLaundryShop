
package BubblesLaundrySystem.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Data
@Table(name = "Laundry_Transactions")
@NoArgsConstructor 
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@NonNull
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String customerName;
    private Double totalAmount;
    private String status;
    private String estimatedTime;
    private String phoneNumber;
    
    private LocalDateTime orderDate;
    
    private String serviceMode;
    private Double weight;
    
    private Integer detergentQty = 0;
    private Integer fabconQty = 0;
    private Integer bleachQty = 0;
    
    private String deliveryAddress;
    private String  adressDescription;
    
    
}
