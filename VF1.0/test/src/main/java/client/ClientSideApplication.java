package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientSideApplication {
    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;

    // Initialize the connection in a static block or method
    static {
        try {
            initializeConnection("localhost", 1234);
        } catch (IOException e) {
            System.err.println("Error initializing connection: " + e.getMessage());
        }
    }

    private static void initializeConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public static int getExpiryDate(String itemName) {
        try {
            bufferedWriter.write("GET " + itemName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String serverOutput = bufferedReader.readLine();
            if (serverOutput.equalsIgnoreCase("no_expiry_date")) {
                System.out.println("No expiry date found for: " + itemName);
                return -1;
            }
            return Integer.parseInt(serverOutput);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static ArrayList<String> getSearchResult(String searchInput) {
        try {
            String modifiedInput = searchInput.replaceFirst("^\\s+","");
            modifiedInput = modifiedInput.toUpperCase();
            bufferedWriter.write("SEARCH " + modifiedInput);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String serverOutput = bufferedReader.readLine();
            if (serverOutput.equalsIgnoreCase("EMPTY")) {
                System.out.println("No search results for: " + searchInput);
                return new ArrayList<>();
            }

            String delimiter = ";";
            return new ArrayList<>(Arrays.asList(serverOutput.split(delimiter)));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void stopConnection() {
        try {
            bufferedWriter.write("STOP");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String serverResponse = bufferedReader.readLine();
            System.out.println("Server response: " + serverResponse);

            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
