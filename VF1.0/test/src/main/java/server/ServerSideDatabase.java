package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ServerSideDatabase {
    private static final HashMap<String, Duration> expiryDate = new HashMap<>();
    private static final ArrayList<String> allFoods = new ArrayList<>();
    private String pathname;
    private ServerSocket serverSocket;

    // Constructor to initialize the server
    public ServerSideDatabase(String pathname, int port) throws IOException {
        this.pathname = pathname;
        this.serverSocket = new ServerSocket(port);

        // READ CSV and populate expiryDate map
        readExpiryDatesFromFile();
    }

    // Read the expiry dates from the CSV file
    private void readExpiryDatesFromFile() {
        try (BufferedReader parseBufferedReader = new BufferedReader(new FileReader(pathname))) {
            String line;
            while ((line = parseBufferedReader.readLine()) != null) {
                // Split the line using comma as the delimiter
                String[] parts = line.split(",");

                // Ensure the line has exactly two parts
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    int days = Integer.parseInt(parts[1].trim());
                    // Add the entry to the HashMap
                    expiryDate.put(key, Duration.ofDays(days));
                    allFoods.add(key);
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Start accepting client connections
    public void startServer() {
        System.out.println("Server started. Waiting for clients...");
        while (true) {
            try {
                Socket socket = serverSocket.accept(); // Accept new client connection
                System.out.println("New client connected.");

                // Create a new thread to handle the client
                ClientSideThread clientHandler = new ClientSideThread(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Static methods to access expiry date information
    protected static Duration getExpiryDate(String foodSearched) {
        return expiryDate.get(foodSearched);
    }

    protected static void addFoodEntry(String food, Integer expiryDays) {
        expiryDate.put(food, Duration.ofDays(expiryDays));
    }

    protected static ArrayList<String> getSearchResult(String searchInput) {
        ArrayList<String> result = new ArrayList<>();
        for (String allFood : allFoods) {
            if (allFood.contains(searchInput)) {
                result.add(allFood);
            }
        }
        return result;
    }

    // Main method to run the server
    public static void main(String[] args) {
        try {
            // Instantiate the Server with the file path and port
            ServerSideDatabase server = new ServerSideDatabase("VF1.0/ItemExpiryDates/Food_ExpiryDates.txt", 1234);
            // Start the server
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientSideThread implements Runnable {
    private final Socket socket;

    ClientSideThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

            String msgFromClient;

            while ((msgFromClient = bufferedReader.readLine()) != null) {
                System.out.println("Client: " + msgFromClient);

                // Handle client commands (example: adding or retrieving expiry dates)
                if (msgFromClient.startsWith("GET")) {
                    String food = msgFromClient.substring(4);
                    Duration expiry = ServerSideDatabase.getExpiryDate(food);
                    if (expiry != null) {
                        System.out.println("Expiry for " + food + ": " + expiry.toDays() + " days.");
                        bufferedWriter.write(String.valueOf(expiry.toDays()));
                    } else {
                        bufferedWriter.write("no_expiry_date");
                    }
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else if (msgFromClient.startsWith("SEARCH")) {
                    String searchValue = msgFromClient.substring(7);
                    ArrayList<String> indexedSearch = new ArrayList<>();
                    if (!searchValue.isEmpty()){
                        indexedSearch = ServerSideDatabase.getSearchResult(searchValue);
                    }
                    String delimiter = ";";
                    try {
                        if (indexedSearch.isEmpty()) {
                            bufferedWriter.write("EMPTY");
                        } else {
                            String listToSend = String.join(delimiter, indexedSearch);
                            bufferedWriter.write(listToSend);
                        }
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        bufferedWriter.write("Error processing SEARCH command.");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                } else if (msgFromClient.equalsIgnoreCase("STOP")) {
                    bufferedWriter.write("Goodbye!");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    break;
                } else {
                    bufferedWriter.write("Unknown command.");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
