package client;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Rep Invariant:
 *  (1) itemName cannot be null
 *
 * Abstract Function:
 *      An item in the fridge that has a set expiry date.
 */
public class Food {

    private String itemName;
    private LocalDate expiryDate;
    private LocalDate currentDate = LocalDate.now();

    /**
     * Food Constructor
     * @param itemName
     * @param timeToExpire
     * @param currentDate
     */
    public Food(String itemName, Duration timeToExpire, LocalDate currentDate) {
        this.itemName = itemName;
        this.currentDate = currentDate;
        this.expiryDate = currentDate.plusDays(timeToExpire.toDays());
    }

    /**
     * Food Constructor: Intialized by the itemName and the Duration to its expiry
     * @param itemName
     * @param timeToExpire
     */
    public Food(String itemName, Duration timeToExpire) {
        this.itemName = itemName;
        this.expiryDate = currentDate.plusDays(timeToExpire.toDays());
    }

    /**
     * Getter for itemName
     * @return
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Getter for expiryDate
     * @return
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Food)) {
            return false;
        }
        Food thatFood = (Food) thatObject;
        return (this.getExpiryDate() == thatFood.getExpiryDate())
            && (this.itemName.equals(thatFood.getItemName()));
    }

    @Override
    public int hashCode() {
        int asciiSum = 0;
        for (char c : itemName.toCharArray()) {
            asciiSum += (int) c;
        }
        return currentDate.getDayOfMonth() + expiryDate.getDayOfMonth() + asciiSum;
    }
}
