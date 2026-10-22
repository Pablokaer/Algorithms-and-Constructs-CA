import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FoodStorage {
    public static final int MIN_WEIGHT_GRAMS = 1;
    public static final int MAX_WEIGHT_GRAMS = 5000;
    public static final int MAX_SHELF_LIFE_DAYS = 14;

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private FoodType type;
    private int weightGrams;
    private LocalDate bestBefore;
    private LocalDateTime placedAt;



}
