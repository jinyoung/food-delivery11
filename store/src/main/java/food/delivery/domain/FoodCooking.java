package food.delivery.domain;

import food.delivery.StoreApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="FoodCooking_table")
@Data

public class FoodCooking  {

    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private String foodId;
    
    
    
    
    
    private String orderId;
    
    
    @ElementCollection
    private List<String> options;    
    
    private String storeId;
    
    
    
    
    
    private String customerId;

    @PostPersist
    public void onPostPersist(){
    }

    public static FoodCookingRepository repository(){
        FoodCookingRepository foodCookingRepository = StoreApplication.applicationContext.getBean(FoodCookingRepository.class);
        return foodCookingRepository;
    }



    public void accept(AcceptCommand acceptCommand){

        if(acceptCommand.getAccept()){
            OrderAccepted orderAccepted = new OrderAccepted(this);
            orderAccepted.publishAfterCommit();

            setStatus("접수됨");
        }else{
            OrderRejected orderRejected = new OrderRejected(this);
            orderRejected.publishAfterCommit();

            setStatus("거절됨");

        }
    }
    
    public void start(){
        CookStarted cookStarted = new CookStarted(this);
        cookStarted.publishAfterCommit();

    }
    public void finish(){
        CookFinished cookFinished = new CookFinished(this);
        cookFinished.publishAfterCommit();

    }

    public static void 주문정보복제(OrderPlaced orderPlaced){

        /** Example 1:  new item  */
        FoodCooking foodCooking = new FoodCooking();
        foodCooking.setCustomerId(orderPlaced.getCustomerId());
        foodCooking.setFoodId(orderPlaced.getFoodId());
        foodCooking.setOrderId(String.valueOf(orderPlaced.getId()));
        foodCooking.setStatus("미결재");
        repository().save(foodCooking);

       

        /** Example 2:  finding and process
        
        repository().findById(orderPlaced.get???()).ifPresent(foodCooking->{
            
            foodCooking // do something
            repository().save(foodCooking);


         });
        */

        
    }
    public static void updateStatus(Paid paid){

        /** Example 1:  new item 
        FoodCooking foodCooking = new FoodCooking();
        repository().save(foodCooking);

        */

        /** Example 2:  finding and process */
        
        repository().findByOrderId(paid.getOrderId()).ifPresent(foodCooking->{
            
            foodCooking.setStatus("결재됨"); // do something
            repository().save(foodCooking);


         });
       

        
    }


}
