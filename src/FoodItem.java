import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// One tray of food in the storage
public class FoodItem {

    public static final int MIN_WEIGHT_GRAMS = 1;
    public static final int MAX_WEIGHT_GRAMS = 5000;
    public static final int MAX_SHELF_LIFE_DAYS = 14;

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final FoodType type;
    private final int weightGrams;
    private final LocalDate bestBefore;
    private final LocalDateTime placedAt;

    // the time placed is the current time
    public FoodItem(FoodType type, int weightGrams, LocalDate bestBefore) {
        LocalDateTime placedAt = LocalDateTime.now();
        if (type == null) throw new IllegalArgumentException("Food type is required.");
        validateWeight(weightGrams);
        validateBestBefore(bestBefore, placedAt.toLocalDate());
        this.type = type;
        this.weightGrams = weightGrams;
        this.bestBefore = bestBefore;
        this.placedAt = placedAt;
    }

    public static void validateWeight(int weightGrams) {
        if (weightGrams < MIN_WEIGHT_GRAMS || weightGrams > MAX_WEIGHT_GRAMS) {
            throw new IllegalArgumentException("Weight must be between " + MIN_WEIGHT_GRAMS + " and " + MAX_WEIGHT_GRAMS + " grams.");
        }
    }

    public static void validateBestBefore(LocalDate bestBefore, LocalDate today) {
        if (bestBefore == null) throw new InvalidBestBeforeDateException("Best-before date is required.");
        if (bestBefore.isBefore(today)) throw new InvalidBestBeforeDateException("Best-before date cannot be in the past.");
        LocalDate maxDate = today.plusDays(MAX_SHELF_LIFE_DAYS);
        if (bestBefore.isAfter(maxDate)) {
            throw new InvalidBestBeforeDateException("Best-before date can be at most " + MAX_SHELF_LIFE_DAYS
                    + " days from today (latest: " + maxDate.format(DATE_FORMAT) + ").");
        }
    }

    public FoodType getType() {
        return type;
    }

    public String getName() {
        return foodName(type);
    }

    // PIZZA -> Pizza
    public static String foodName(FoodType type) {
        return type.name().charAt(0) + type.name().substring(1).toLowerCase();
    }

    public int getWeightGrams() {
        return weightGrams;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    @Override
    public String toString() {
        return getName() + " - " + weightGrams + "g - best before " + bestBefore.format(DATE_FORMAT)
                + " - placed at " + placedAt.format(TIME_FORMAT);
    }
}
