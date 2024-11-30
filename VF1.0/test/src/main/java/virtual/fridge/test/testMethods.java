package virtual.fridge.test;

import client.ClientSideApplication;

import java.util.ArrayList;

public class testMethods {

    public static void main(String[] args) {
        System.out.println(ClientSideApplication.getExpiryDate("APPLE"));

        ArrayList<String> searchResults = ClientSideApplication.getSearchResult("SAUSAGE");

        System.out.println(searchResults);
        System.out.println(ClientSideApplication.getSearchResult("PORK"));
        System.out.println("");
        System.out.println(ClientSideApplication.getSearchResult(" pork"));

        System.out.println(ClientSideApplication.getExpiryDate(searchResults.get(0)));


    }
}


