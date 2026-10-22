import java.util.ArrayList;
import java.util.List;

public class FoodStorage {

    public static final int DEFAULT_CAPACITY = 8;

    private final FoodItem[] trays;
    private int back;
    private int size;
    private boolean fifoMode;

    public FoodStorage(boolean fifoMode) {
        this(fifoMode, DEFAULT_CAPACITY);
    }

    public FoodStorage(boolean fifoMode, int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        trays = new FoodItem[capacity];
        this.fifoMode = fifoMode;
    }

    private int frontIndex() {
        return (back + size - 1) % trays.length;
    }

    private FoodItem at(int i) {
        return trays[(back + i) % trays.length];
    }

    private void requireNotEmpty(String message) {
        if (isEmpty()) {
            throw new StorageEmptyException(message);
        }
    }

    public void add(FoodItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Food item cannot be null.");
        }
        if (isFull()) {
            throw new StorageFullException("Storage is full (" + trays.length + " trays). Remove a tray before adding another.");
        }
        trays[(back + size) % trays.length] = item;
        size++;
    }

    public FoodItem remove() {
        return fifoMode ? removeFromBack() : removeFromFront();
    }

    public FoodItem removeFromFront() {
        requireNotEmpty("Cannot remove a tray: the storage is empty.");
        int position = frontIndex();
        FoodItem item = trays[position];
        trays[position] = null;
        size--;
        return item;
    }

    public FoodItem removeFromBack() {
        requireNotEmpty("Cannot remove a tray: the storage is empty.");
        FoodItem item = trays[back];
        trays[back] = null;
        back = (back + 1) % trays.length;
        size--;
        return item;
    }

    public FoodItem peekTop() {
        requireNotEmpty("Cannot view the top tray: the storage is empty.");
        return trays[frontIndex()];
    }

    public FoodItem peekNextToRemove() {
        requireNotEmpty("Cannot view the next tray: the storage is empty.");
        return fifoMode ? trays[back] : trays[frontIndex()];
    }

    public List<FoodItem> listFromTop() {
        List<FoodItem> list = new ArrayList<>();
        for (int i = size - 1; i >= 0; i--) {
            list.add(at(i));
        }
        return list;
    }

    public List<Integer> search(FoodType type) {
        requireNotEmpty("Cannot search: the storage is empty.");
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (at(size - 1 - i).getType() == type) {
                positions.add(i + 1);
            }
        }
        return positions;
    }

    public int totalWeightGrams() {
        int total = 0;
        for (int i = 0; i < size; i++) {
            total += at(i).getWeightGrams();
        }
        return total;
    }

    public void setFifoMode(boolean newFifoMode) {
        if (!isEmpty()) {
            throw new IllegalStateException("Empty the storage before changing the mode.");
        }
        fifoMode = newFifoMode;
    }

    public boolean isFifoMode() {
        return fifoMode;
    }

    public String getModeDescription() {
        return fifoMode ? "Front side for adding, opposite side for removing (Queue / FIFO)"
                : "Front side for adding and removing (Stack / LIFO)";
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return trays.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == trays.length;
    }
}
