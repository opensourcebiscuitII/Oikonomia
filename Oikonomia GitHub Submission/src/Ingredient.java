
import java.time.LocalDate;

public class Ingredient{

    String name;

    LocalDate expirationDate;

    public Ingredient(){}

    public Ingredient(String name,LocalDate expirationDate){
        this.name = name;
        this.expirationDate = expirationDate;
    }
    
}