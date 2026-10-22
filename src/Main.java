import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("   FAST FOOD PARTY STORAGE");
        System.out.println("==============================");

        static Scanner sc = new Scanner(System.in);
        static FoodStorage storage;
        storage = new FoodStorage(chooseMode());

        boolean exit = false;
        while (exit == false) {
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

    static String readLine(String message) {
        System.out.print(message);
        // if there is no more input (for example Ctrl+D) we close the program
        if (sc.hasNextLine() == false) {
            System.out.println();
            System.out.println("No more input. Program finished.");
            System.exit(0);
        }
        return sc.nextLine();
    }
}

