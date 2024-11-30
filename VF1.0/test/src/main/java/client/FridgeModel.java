package client;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Representation Invariant
 *      (1) clientFridge cannot be null
 *      (2) foodExpired cannot be null
 *      (3) foodDaysLeft cannot be null
 *      (4) today cannot be null
 *      (5) clientFridge.size() == foodExpired.size() == foodDaysLeft.size()
 *      (6) clockThread should continue to run until program is killed or shutDownFridge() is ran
 *
 * Abstract Function:
 *      The FridgeModel represents a virtual storage system for food items that:
 *       - Tracks expiry dates for stored items.
 *       - Updates their status and remaining days left automatically.
 *       - Supports loading and saving its data to and from a CSV file for persistence.
 */

public class FridgeModel {
    private static final long THREAD_SLEEP_MILLIS = 1000;
    private HashMap<String, LocalDate> clientFridge;
    private HashMap<String, Boolean> foodExpired;
    private HashMap<String, Integer> foodDaysLeft;
    private LocalDate today = LocalDate.now();
    private String clientFridgePath = "ClientFridge.txt";
    private ClockThread clockThread;

    /**
     * Constructor to create a Fridge
     */
    public FridgeModel() {
        this.clientFridge = new HashMap<>();
        this.foodExpired = new HashMap<>();
        this.foodDaysLeft = new HashMap<>();
        loadCSV();
        checkExpiry();
        updateDaysLeft();
        ClockThread clockThread = new ClockThread();
        clockThread.start();
    }
    /**
     * Constructor to create a Fridge
     */
    public FridgeModel(LocalDate startingDate, String path) {
        this.today = startingDate;
        this.clientFridge = new HashMap<>();
        this.clientFridgePath = path;
        this.foodExpired = new HashMap<>();
        this.foodDaysLeft = new HashMap<>();
        ClockThread clockThread = new ClockThread();
        loadCSV();
    }

    /**
     * Method: Adds a food item to the fridge
     * Throws IllegalArgumentException if food is null
     * @param food
     */
    public void addToFridge(Food food) {
        if (food == null) {
            throw new IllegalArgumentException();
        }
        clientFridge.put(food.getItemName(), food.getExpiryDate());
        foodExpired.put(food.getItemName(), false);
        foodDaysLeft.put(food.getItemName(), (int) ChronoUnit.DAYS.between(food.getExpiryDate(),today));
    }

    /**
     * Method: Checks if a food item exists in the fridge
     * @param food cannot be null
     * @return
     */
    public boolean hasItem(Food food) {
        return clientFridge.entrySet().stream()
            .anyMatch(entry -> entry.getKey().equals(food.getItemName()));
    }

    /**
     * Method: Checks if a food item exists in the fridge
     * @param foodName cannot be null
     * @return
     */
    public boolean hasItem(String foodName) {
        return clientFridge.keySet().stream()
            .anyMatch(name -> name.equals(foodName));
    }

    /**
     * Method: Removes a food item from the fridge.
     * Returns true if food is successfully removed from the fridge. False otherwise.
     * itemName cannot be null
     * @param itemName
     * @return
     */
    public boolean removeFromFridge(String itemName) {
        if (hasItem(itemName)){
            clientFridge.remove(itemName);
            foodExpired.remove(itemName);
            foodDaysLeft.remove(itemName);
            return true;
        }
        return false;
    }

    /**
     * Method: Checks all items in the fridge, whether they have expired.
     * returns true if at least more than one item has expired and been set to expired.
     * @return
     */
    public void checkExpiry() {
        for (String food : clientFridge.keySet()) {
            if (today.isAfter(clientFridge.get(food))) {
                foodExpired.put(food, true);
            }
            else {
                foodExpired.put(food,false);
            }
        }
    }

    /**
     * Method: Updates the days left of each item in the fridge. (today - expiryDate)
     * If an item is pass its expiry date, it will stay at 0.
     *
     */
    public void updateDaysLeft () {
        for (String food : clientFridge.keySet()) {
            int daysLeft = (int) ChronoUnit.DAYS.between(today,clientFridge.get(food));
            if (daysLeft < 0) {
                foodDaysLeft.put(food, 0);
            }
            else {
                foodDaysLeft.put(food, daysLeft);
            }
        }
    }


    //GETTERS------------------------------------------------------------------------------------------------

    /**
     * Method: Gets a Map of all the food in the Fridge mapped to whether they are expired or not.
     * @return
     */
    public HashMap<String, Boolean> getExpiredFood() {
        return new HashMap<>(foodExpired);
    }

    /**
     * Method: Returns a Map of the client's fridge and the item's expiry date
     * @return
     */
    public HashMap<String, LocalDate> getClientFridge() {
        return new HashMap<>(clientFridge);
    }

    /**
     * Method: Returns the days left of each item in the fridge.
     * returns -1 if food does not exist in the fridge.
     * @return
     */
    public int getFoodDaysLeft(String itemName) {
        if (hasItem(itemName)) {
            return foodDaysLeft.get(itemName);
        }
        else {
            return -1;
        }
    }

    /**
     * Helper Method: Updates the current Date of the fridge to the System Date
     */
    private void updateCurrentDate() {
        this.today = LocalDate.now();
    }

    /**
     * Manual override of current date
     * Used for testing.
     *
     * @param date
     */
    public void setCurrentDate(LocalDate date) {
        this.today = date;
    }

    /**
     * Method: Stops clockThread and writes the Hashmap back into csv for storage. This should be done before shutting
     * down the application.
     */
    public void shutDownFridge() {
        if (clockThread != null) {
            clockThread.stopThread();
            try {
                clockThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        writeCSV();
    }



    //WRITE AND LOADING CSV------------------------------------------------------------------------------------------

    /**
     * Utility Method: Writes to csv file the HashMap of clientFridge.
     */
    private void writeCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(clientFridgePath))) {
            for (Map.Entry<String, LocalDate> entry : clientFridge.entrySet()) {
                String line = entry.getKey() + "," + entry.getValue().toString();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility Method: Loads csv from the file to the clientFridge HashMap.
     */
    private void loadCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(clientFridgePath
        ))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Split the line by the comma delimiter
                String[] parts = line.split(",");
                String itemName = parts[0].trim();
                LocalDate expiryDate = LocalDate.parse(parts[1].trim());
                clientFridge.put(itemName, expiryDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Helper Class:
     * Thread class that continuosly updates the currentDate, checkExpiry and updateDaysLeft whenever the client
     * application is up and the FridgeModel instance is created
     */
    class ClockThread extends Thread {
        private boolean running = true;

        public void run() {

            while (running) {
                synchronized (FridgeModel.this) {
                    updateCurrentDate();
                    checkExpiry();
                    updateDaysLeft();
                    System.out.println("Thread Alive");
                }
                try {
                    Thread.sleep(THREAD_SLEEP_MILLIS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        /**
         * Method: Stops the thread
         */
        public void stopThread() {
            running = false;
        }
    }
}






