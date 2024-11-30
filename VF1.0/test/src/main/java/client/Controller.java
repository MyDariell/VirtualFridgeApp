package client;

import java.util.ArrayList;

/**
 * Controller that connects the ClientSideApplication with the View.
 */
public class Controller {

    /**
     * METHOD:
     * Gets the number of days for an item to expire.
     * Returns -1 if the item is not found.
     * @param itemName
     * @return
     */
    public static int getExpiryDays(String itemName) {
        return ClientSideApplication.getExpiryDate(itemName);
        }

    /**
     * Gets the search result of all food that contains the keyword in its name.
     * Returns an empty list if none found.
     * @param keyword
     * @return
     */
    public static ArrayList<String> getSearchResults(String keyword) {
        return ClientSideApplication.getSearchResult(keyword);
    }

}


