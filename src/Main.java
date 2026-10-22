import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static FoodStorage storage;

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   FAST FOOD PARTY STORAGE");
        System.out.println("   (max " + FoodStorage.DEFAULT_CAPACITY + " trays)");
        System.out.println("=================================");

        storage = new FoodStorage(chooseMode());

        boolean exit = false;
        while (exit == false) {
            showMenu();
            String option = readLine("Choose an option: ").trim();

            try {
                switch (option) {
                    case "1":
                        addFood();
                        break;
                    case "2":
                        FoodItem removed = storage.remove();
                        if (storage.isFifoMode()) {
                            System.out.println("Removed from the opposite side: " + removed);
                        } else {
                            System.out.println("Removed from the front side: " + removed);
                        }
                        break;
                    case "3":
                        System.out.println("Food at the top: " + storage.peekTop().getName());
                        System.out.println("Details: " + storage.peekTop());
                        if (storage.isFifoMode()) {
                            System.out.println("Next to be removed (opposite side): " + storage.peekNextToRemove().getName());
                        }
                        break;
                    case "4":
                        showAll();
                        break;
                    case "5":
                        searchFood();
                        break;
                    case "6":
                        storage.setFifoMode(chooseMode());
                        System.out.println("Mode changed to: " + storage.getModeDescription());
                        break;
                    case "0":
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option! Type a number from 0 to 6.");
                }
            } catch (StorageFullException | IllegalStateException | StorageEmptyException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Program finished. Bye!");
    }

    static void showMenu() {
        System.out.println();
        System.out.println("Mode: " + storage.getModeDescription());
        System.out.println("Trays used: " + storage.size() + "/" + storage.capacity());
        System.out.println("1 - Add food");
        System.out.println("2 - Remove food");
        System.out.println("3 - Show food at the top");
        System.out.println("4 - Show all food");
        System.out.println("5 - Search food");
        System.out.println("6 - Change mode (only when empty)");
        System.out.println("0 - Exit");
    }

    static boolean chooseMode() {
        while (true) {
            System.out.println("How will the chef use the storage?");
            System.out.println("1 - Front side for adding and removing (Stack / LIFO)");
            System.out.println("2 - Front side for adding, opposite side for removing (Queue / FIFO)");
            String answer = readLine("Mode (1 or 2): ").trim();
            if (answer.equals("1")) {
                return false;
            } else if (answer.equals("2")) {
                return true;
            } else {
                System.out.println("Please type 1 or 2.");
            }
        }
    }

    static void addFood() {
        if (storage.isFull()) {
            throw new StorageFullException("Storage is full (" + storage.capacity() + " trays). Remove a tray first.");
        }

        FoodType type = readFoodType();

        int weight = 0;
        boolean ok = false;
        while (ok == false) {
            String text = readLine("Weight in grams (" + FoodItem.MIN_WEIGHT_GRAMS + "-" + FoodItem.MAX_WEIGHT_GRAMS + "): ");
            try {
                weight = Integer.parseInt(text.trim());
                FoodItem.validateWeight(weight);
                ok = true;
            } catch (NumberFormatException e) {
                System.out.println("Error: the weight must be a whole number, for example 250.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // read the best-before date until it is valid
        LocalDate bestBefore = null;
        ok = false;
        while (ok == false) {
            String text = readLine("Best-before date (dd/MM/yyyy, max 14 days): ");
            try {
                bestBefore = LocalDate.parse(text.trim(), FoodItem.DATE_FORMAT);
                FoodItem.validateBestBefore(bestBefore, LocalDate.now());
                ok = true;
            } catch (DateTimeParseException e) {
                System.out.println("Error: wrong date. Use dd/MM/yyyy, for example "
                        + LocalDate.now().plusDays(3).format(FoodItem.DATE_FORMAT));
            } catch (InvalidBestBeforeDateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        FoodItem item = new FoodItem(type, weight, bestBefore);
        storage.add(item);
        System.out.println("Added: " + item);
    }

    static void showAll() {
        if (storage.isEmpty()) {
            throw new StorageEmptyException("Nothing to show, the storage is empty.");
        }
        List<FoodItem> items = storage.listFromTop();
        System.out.println("---- Food in the storage (top first) ----");
        for (int i = 0; i < items.size(); i++) {
            String text = (i + 1) + ". " + items.get(i);
            if (i == 0) {
                text = text + "  <- TOP / front";
            } else if (i == items.size() - 1) {
                text = text + "  <- opposite side";
            }
            System.out.println(text);
        }
        System.out.println("Total weight: " + storage.totalWeightGrams() + " g");
    }

    static void searchFood() {
        if (storage.isEmpty()) {
            throw new StorageEmptyException("Cannot search, the storage is empty.");
        }
        FoodType type = readFoodType();
        List<Integer> positions = storage.search(type);
        if (positions.size() == 0) {
            System.out.println("There is no " + FoodItem.foodName(type) + " in the storage.");
        } else {
            System.out.println(FoodItem.foodName(type) + " found at position(s) " + positions + " (1 = top)");
        }
    }

    static FoodType readFoodType() {
        while (true) {
            String text = readLine("Food (1 Burger, 2 Pizza, 3 Fries, 4 Sandwich, 5 Hotdog): ");
            try {
                return parseFoodType(text);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static FoodType parseFoodType(String text) {
        String t = text.trim().toLowerCase();

        if (t.isEmpty()) {
            throw new IllegalArgumentException("You must type a food.");
        }
        if (t.equals("1") || t.equals("burger")) {
            return FoodType.BURGER;
        }
        if (t.equals("2") || t.equals("pizza")) {
            return FoodType.PIZZA;
        }
        if (t.equals("3") || t.equals("fries")) {
            return FoodType.FRIES;
        }
        if (t.equals("4") || t.equals("sandwich")) {
            return FoodType.SANDWICH;
        }
        if (t.equals("5") || t.equals("hotdog") || t.equals("hot dog") || t.equals("hot-dog")) {
            return FoodType.HOTDOG;
        }
        throw new IllegalArgumentException("'" + text.trim() + "' is not on the menu. Choose Burger, Pizza, Fries, Sandwich or Hotdog.");
    }

    static String readLine(String message) {
        System.out.print(message);
        if (sc.hasNextLine() == false) {
            System.out.println();
            System.out.println("No more input. Program finished.");
            System.exit(0);
        }
        return sc.nextLine();
    }
}
