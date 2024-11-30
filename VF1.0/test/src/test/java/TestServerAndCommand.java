import client.ClientSideApplication;
import client.Food;
import client.FridgeModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ServerSideDatabase;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*

This is the items in the test file

APPLE, 5
PORK RAW, 35
PORK COOKED, 50
TINY SAUSAGE, 5
MEDIUM SAUSAGE, 10
LARGE SAUSAGE, 23
BOSNIAN SAUSAGE, 20
ASIAN SAUSAGE, 1
FETA CHEESE, 40

*/
public class TestServerAndCommand {

    private Thread serverThread;

    // Create and start the server before each test
    @BeforeEach
    public void setUp() {
        serverThread = new Thread(() -> {
            try {
                createServer();
            } catch (IOException e) {
                System.err.println("Failed to initialize the server: " + e.getMessage());
            }
        });
        serverThread.start();
    }

    // Stop the server after each test
    @AfterEach
    public void tearDown() {
        stopServer();
    }

    public void createServer() throws IOException {
        ServerSideDatabase testingServer = new ServerSideDatabase("src/test/testExpiryDates/TestFood_ExpiryDates.txt", 1234);
        testingServer.startServer();
        System.out.println("Server started successfully!");
    }

    public void stopServer() {
        if (serverThread != null && serverThread.isAlive()) {
            try {
                serverThread.interrupt();
            } catch (Exception e) {
                System.err.println("Failed to stop the server: " + e.getMessage());
            }
        }
    }

    @Test
    public void BlankInput(){
        ArrayList<String> Expected = new ArrayList<>();
        ArrayList<String> blank = ClientSideApplication.getSearchResult("");
        assertEquals(Expected, blank);
    }

    @Test
    public void OneItemSearch () {
        ArrayList<String> Expected = new ArrayList<>();
        Expected.add("APPLE");

        ArrayList<String> uppercase = ClientSideApplication.getSearchResult("APPLE");
        ArrayList<String> lowercase = ClientSideApplication.getSearchResult("apple");
        ArrayList<String> space = ClientSideApplication.getSearchResult("   APPLE");
        ArrayList<String> mixed = ClientSideApplication.getSearchResult("    ApPlE");

        assertEquals(Expected, uppercase);
        assertEquals(Expected, lowercase);
        assertEquals(Expected, space);
        assertEquals(Expected, mixed);
    }

    @Test
    public void OneItemSearchWithSpace () {
        ArrayList<String> Expected = new ArrayList<>();
        Expected.add("PORK RAW");

        ArrayList<String> uppercase = ClientSideApplication.getSearchResult("PORK RAW");
        ArrayList<String> lowercase = ClientSideApplication.getSearchResult("pork raw");
        ArrayList<String> space = ClientSideApplication.getSearchResult("   PORK RAW");
        ArrayList<String> mixed = ClientSideApplication.getSearchResult("    pork raW");

        assertEquals(Expected, uppercase);
        assertEquals(Expected, lowercase);
        assertEquals(Expected, space);
        assertEquals(Expected, mixed);
    }

    @Test
    public void TwoItemSearch () {
        ArrayList<String> Expected = new ArrayList<>();
        Expected.add("PORK RAW");
        Expected.add("PORK COOKED");

        ArrayList<String> uppercase = ClientSideApplication.getSearchResult("PORK");
        ArrayList<String> lowercase = ClientSideApplication.getSearchResult("pork");
        ArrayList<String> space = ClientSideApplication.getSearchResult("   PORK");
        ArrayList<String> mixed = ClientSideApplication.getSearchResult("   poRk");

        assertEquals(Expected, uppercase);
        assertEquals(Expected, lowercase);
        assertEquals(Expected, space);
        assertEquals(Expected, mixed);
    }

    @Test
    public void ManyItemSearch () {
        ArrayList<String> Expected = new ArrayList<>();
        Expected.add("TINY SAUSAGE");
        Expected.add("MEDIUM SAUSAGE");
        Expected.add("LARGE SAUSAGE");
        Expected.add("BOSNIAN SAUSAGE");
        Expected.add("ASIAN SAUSAGE");

        ArrayList<String> uppercase = ClientSideApplication.getSearchResult("SAUSAGE");
        ArrayList<String> lowercase = ClientSideApplication.getSearchResult("sausage");
        ArrayList<String> space = ClientSideApplication.getSearchResult("  SAUSAGE");
        ArrayList<String> mixed = ClientSideApplication.getSearchResult("  SAusagE");

        assertEquals(Expected, uppercase);
        assertEquals(Expected, lowercase);
        assertEquals(Expected, space);
        assertEquals(Expected, mixed);
    }

    @Test
    public void noMatches(){
        ArrayList<String> Expected = new ArrayList<>();
        ArrayList<String> invalidInput1 = ClientSideApplication.getSearchResult("CPEN221");
        ArrayList<String> invalidInput2 = ClientSideApplication.getSearchResult("WHAT DO YOU THINK MAKES SENSE");
        ArrayList<String> invalidInput3 = ClientSideApplication.getSearchResult("!   =");

        assertEquals(Expected, invalidInput1);
        assertEquals(Expected, invalidInput2);
        assertEquals(Expected, invalidInput3);
    }

    @Test
    public void PorkRaw() {
        ArrayList<String> Expected = new ArrayList<>();
        Expected.add("PORK RAW");

        ArrayList<String> uppercase = ClientSideApplication.getSearchResult("PORK RAW");

        assertEquals(Expected, uppercase);
    }
    /*

    These are the tests for GET command

     */

    @Test
    public void InvalidInput1() {

        int expiryDate = ClientSideApplication.getExpiryDate("CPEN211");

        assertEquals(-1, expiryDate);
    }

    @Test
    public void InvalidInput2() {

        int expiryDate = ClientSideApplication.getExpiryDate("PORKRAW");

        assertEquals(-1, expiryDate);
    }

    @Test
    public void PorkRawDate() {

        int expiryDate = ClientSideApplication.getExpiryDate("PORK RAW");

        assertEquals(35, expiryDate);
    }

    @Test
    public void AppleDate() {

        int expiryDate = ClientSideApplication.getExpiryDate("APPLE");

        assertEquals(5, expiryDate);
    }

    @Test
    public void createFridge () {

    }

}







