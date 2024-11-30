module com.example.test {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.xml.dom;


    opens virtual.fridge.test to javafx.fxml;
    exports virtual.fridge.test;
    exports client;
    exports server;
}