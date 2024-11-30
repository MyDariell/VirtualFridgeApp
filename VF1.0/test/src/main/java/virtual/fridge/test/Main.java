package virtual.fridge.test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

//classes
import client.FridgeModel;
import client.Controller;
import client.Food;

/**
 * Main class.
 * This class is the main class for the project.
 * It contains all scenes aka. different visuals for the project.
 * This class impliments the client server to work with the FridgeModel.
 */

public class Main extends Application{

    FridgeModel fridge;
    String name;
    boolean[] clicked1 = {false};
    boolean[] clicked2 = {false};
    boolean[] clicked3 = {false};
    boolean[] clicked4 = {false};
    boolean[] clicked5 = {false};
    boolean[] clicked6 = {false};
    boolean[] clicked7 = {false};
    boolean[] clicked8 = {false};
    Rectangle dim;

    /**
     * Main method.
     * This method simply displays the project.
     * @param args the arguments to be passed to the application
     */

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start method.
     * This method is called when the application is launched.
     * @param stage the stage to display the scene on
     */

    @Override
    public void start(Stage stage) {

        //initialize Fridge
        this.fridge = new FridgeModel();

        Image icon = new Image("Logo.png");
        stage.getIcons().add(icon);
        stage.setTitle("Virtual Fridge");
        stage.setWidth(500);
        stage.setHeight(800);
        stage.setResizable(true);
        stage.setFullScreen(false);
        stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("ESC"));

        //start of log in screen
        OnboardingScene(stage);
        //tagMainScene(stage);
        stage.show();
    }

    /**
     * Onboarding scene.
     * This scene allows the user to enter their name and dietary preferences.
     * @param stage the stage to display the scene on
     */

    //    log in scene to be designed fully later
    public void OnboardingScene(Stage stage) {

        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("vbox-onboarding");
        Scene onboardingScene = new Scene(vbox, 500, 800);
        onboardingScene.getStylesheets().add(getClass().getResource("/OnboardingScene.css").toExternalForm());

        // Logo image
        Image logo = new Image("Logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.getStyleClass().add("logo");
        logoView.setFitWidth(150);
        logoView.setFitHeight(150);
        Rectangle logoClip = new Rectangle(150,150);
        logoClip.setArcHeight(70);
        logoClip.setArcWidth(70);
        logoView.setClip(logoClip);

        // Welcome text
        Label title1 = new Label("Hi, welcome to\nVirtual Fridge!");
        title1.getStyleClass().add("title-onboard");
        title1.setPadding(new Insets(30,0,0,0)); //top,right,bottom,left

        //text
        Label text1 = new Label("What's your name?");
        text1.getStyleClass().add("text-onboard");

        Label text2 = new Label("Any dietary preference?");
        text2.getStyleClass().add("text-onboard");

        // Name field
        Rectangle textClip = new Rectangle(450, 65);
        textClip.setArcWidth(50);
        textClip.setArcHeight(50);
        TextField nameField = new TextField();
        nameField.setPromptText("Type in name...");
        nameField.getStyleClass().add("text-field-name");
        nameField.setPrefWidth(450);
        nameField.setPrefHeight(65);
        nameField.setClip(textClip);

        // dietary buttons
        Button dietaryButton1 = createDietaryButton("Vegan");
        Button dietaryButton2 = createDietaryButton("Vegetarian");
        Button dietaryButton3 = createDietaryButton("Pescetarian");
        Button dietaryButton4 = createDietaryButton("Halal");
        Button dietaryButton5 = createDietaryButton("Kosher");
        Button dietaryButton6 = createDietaryButton("Keto");

        // Error message label
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label-onboard");
        errorLabel.setVisible(false);

        dietaryButton1.setOnMouseClicked(e -> toggleButtonState(dietaryButton1, clicked1));
        dietaryButton2.setOnMouseClicked(e -> toggleButtonState(dietaryButton2, clicked2));
        dietaryButton3.setOnMouseClicked(e -> toggleButtonState(dietaryButton3, clicked3));
        dietaryButton4.setOnMouseClicked(e -> toggleButtonState(dietaryButton4, clicked4));
        dietaryButton5.setOnMouseClicked(e -> toggleButtonState(dietaryButton5, clicked5));
        dietaryButton6.setOnMouseClicked(e -> toggleButtonState(dietaryButton6, clicked6));

        GridPane buttonGrid = new GridPane();
        buttonGrid.getStyleClass().add("button-box-style");
        buttonGrid.setPadding(new Insets(0, 0, 0, 0));
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.add(dietaryButton1, 0, 0);
        buttonGrid.add(dietaryButton2, 1, 0);
        buttonGrid.add(dietaryButton3, 2, 0);
        buttonGrid.add(dietaryButton4, 0, 1);
        buttonGrid.add(dietaryButton5, 1, 1);
        buttonGrid.add(dietaryButton6, 2, 1);

        Rectangle nextButtonClip = new Rectangle(450, 65);
        nextButtonClip.setArcWidth(50);
        nextButtonClip.setArcHeight(50);
        Button nextButton = new Button("Next →");
        nextButton.getStyleClass().add("button-default");
        nextButton.setPrefWidth(450);
        nextButton.setPrefHeight(65);
        nextButton.setClip(nextButtonClip);

        nextButton.setOnMousePressed(e -> toggleButtonState(nextButton, clicked7));
        nextButton.setOnMouseClicked(e -> {
            name = nameField.getText().trim();
            OnboardScene2(stage); // go to next onboarding scene
        });

        HBox textBox1 = new HBox(text1);
        textBox1.setPadding(new Insets(30,0,0,20)); //top,right,bottom,left
        textBox1.setAlignment(Pos.CENTER_LEFT);

        HBox textTypeBox = new HBox(nameField);
        textTypeBox.setPadding(new Insets(0,0,0,0));
        textTypeBox.setAlignment(Pos.CENTER);

        HBox textBox2 = new HBox(text2);
        textBox2.setPadding(new Insets(20,0,0,20));
        textBox2.setAlignment(Pos.CENTER_LEFT);

        vbox.getChildren().addAll(logoView, title1, textBox1, textTypeBox, textBox2, buttonGrid, errorLabel, nextButton);
        //TESTIN--------------------------------------------------
        System.out.println((vbox.getLayoutBounds()));
        System.out.println("VBox Bounds: " + vbox.getLayoutBounds());

        stage.setScene(onboardingScene);
    }

    /**
     * Onboarding scene 2.
     * This scene allows the suer to set the number of days
     * before an item expires that they want to be notified.
     * @param stage the stage to display the scene on
     */

    public void OnboardScene2(Stage stage){
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("vbox-onboarding");
        Scene onboardingScene2 = new Scene(vbox, 500, 800);
        onboardingScene2.getStylesheets().add(getClass().getResource("/OnboardingScene.css").toExternalForm());
//        //TESTING------------------------------------------------------
//        System.out.println((vbox.getLayoutBounds()));
//        System.out.println("VBox Bounds: " + vbox.getLayoutBounds());


        // Logo image
        Image logo = new Image("Logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.getStyleClass().add("logo");
        logoView.setFitWidth(150);
        logoView.setFitHeight(150);
        Rectangle logoClip = new Rectangle(150,150);
        logoClip.setArcHeight(70);
        logoClip.setArcWidth(70);
        logoView.setClip(logoClip);

        // Welcome text
        Label title1 = new Label("Almost there!");
        title1.getStyleClass().add("title-onboard");
        title1.setPadding(new Insets(30,0,0,0)); //top,right,bottom,left

        //text
        Label text1 = new Label("How many days do you want to be\nnotified before something expires?");
        text1.getStyleClass().add("text-onboard");

        //text
        Label text2 = new Label("Hold and drag or click the slider!");
        text2.getStyleClass().add("text-onboard");

        //slider------------------------------------------------------------------------------------
        Slider slider = new Slider(0, 7, 0); // Min = 0, Max = 7, Initial = 0
        slider.setMajorTickUnit(1);
        slider.getStyleClass().add("slider-style");
        slider.setMaxWidth(450);
        Rectangle sliderClip = new Rectangle(450, 65);
        sliderClip.setArcWidth(50);
        sliderClip.setArcHeight(50);
        slider.setClip(sliderClip);

        // Label to display the slider's value
        Label valueLabel = new Label((int)(slider.getValue()) + " days");
        valueLabel.getStyleClass().add("slider-text");
        // Add a listener to update the label when the slider value changes
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            valueLabel.setText(String.format("%.0f", newValue.doubleValue()) + " days");
        });

        //for the colour as you slide
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double percentage = (newValue.intValue() - slider.getMin()) / //intValue so thumb snaps to whole numbers
                (slider.getMax() - slider.getMin());
            slider.setStyle(
                "-fx-background-color: linear-gradient(to right, White " +
                    (percentage * 100) + "%, Gray " + (percentage * 100) + "%);"
            );
        });

        // Name field
        Rectangle textClip = new Rectangle(450, 65);
        textClip.setArcWidth(50);
        textClip.setArcHeight(50);
        TextField nameField = new TextField();
        nameField.setPromptText("Type in name...");
        nameField.getStyleClass().add("text-field-name");
        nameField.setPrefWidth(450);
        nameField.setPrefHeight(65);
        nameField.setClip(textClip);

        // Error message label
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label-onboard");
        errorLabel.setVisible(false);

        Rectangle doneButtonClip = new Rectangle(450, 65);
        doneButtonClip.setArcWidth(50);
        doneButtonClip.setArcHeight(50);
        Button doneButton = new Button("Next →");
        doneButton.getStyleClass().add("button-default");
        doneButton.setPrefWidth(450);
        doneButton.setPrefHeight(65);
        doneButton.setClip(doneButtonClip);

        doneButton.setOnMousePressed(e -> toggleButtonState(doneButton, clicked8));
        doneButton.setOnMouseClicked(e -> {
            MainScene(stage);
        });

        HBox textBox1 = new HBox(text1);
        textBox1.setPadding(new Insets(30,0,0,0)); //top,right,bottom,left
        textBox1.setAlignment(Pos.CENTER);

        HBox textBox2 = new HBox(text2);
        textBox2.setPadding(new Insets(50,0,0,0));
        textBox2.setAlignment(Pos.CENTER);

//CHANGE SLIDER----------------------------------------------------------
        StackPane sliderPane = new StackPane(slider, valueLabel);
        sliderPane.setPadding(new Insets(10,0,120,0));
        sliderPane.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(logoView, title1, textBox1, textBox2, sliderPane, doneButton);
        stage.setScene(onboardingScene2);
    }

    /**
     * Main scene, this the actual fridge for the project.
     * @param stage the stage to set
     */

    public void MainScene(Stage stage) {
        StackPane root = new StackPane();
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("vbox-main");
        Scene mainScene = new Scene(root, 500, 800);
        mainScene.getStylesheets().add(getClass().getResource("/MainStyle.css").toExternalForm());
        root.getChildren().add(vbox);
        SceneDimmer(mainScene);

        Label nameLabel = new Label(name + "'s Fridge");
        nameLabel.getStyleClass().add("label-style");

        Label placeHolder = new Label("Your fridge is empty!");
        placeHolder.getStyleClass().add("list-view-placeholder");

        Comparator<Food> comparator = Comparator.comparing(Food::getExpiryDate);

        ListView<Food> fridgeList = new ListView<>();
        fridgeList.getStyleClass().add("list-view-default");
        fridgeList.setPrefSize(400, 700);
        fridgeList.setPlaceholder(placeHolder);
        fridgeList.setEditable(true);
        fridgeList.setFixedCellSize(65);
        fridgeList.cellFactoryProperty();

        //WRITE TO CSV BEFORE CLOSE-----------------------------------------------------------------------
        stage.setOnCloseRequest(event -> {
            fridge.shutDownFridge();
        });

        // LOADS ITEM FROM MEMORY ------------------------------------------------------------------------------------
        for (String s : fridge.getClientFridge().keySet()) {
            Food item = new Food(s, Duration.ofDays(fridge.getFoodDaysLeft(s)));
            fridgeList.getItems().add(item);
            fridgeList.getItems().sort(comparator);
        }

        fridgeList.setCellFactory(lv -> new ListCell<>() {
                private final HBox content = new HBox();
                private final Label nameLabel = new Label();
                private final Label expiryLabel = new Label();
                private Popup popup;

                {
                    // Configure labels and layout
                    nameLabel.setAlignment(Pos.CENTER_LEFT);
                    expiryLabel.setAlignment(Pos.CENTER_RIGHT);
                    expiryLabel.setMaxWidth(Double.MAX_VALUE); // Ensure it stretches
                    HBox.setHgrow(expiryLabel, Priority.ALWAYS);

                    content.setAlignment(Pos.CENTER_LEFT);
                    content.getChildren().addAll(nameLabel, expiryLabel);
                }

                @Override
                protected void updateItem(Food item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        nameLabel.setText(item.getItemName());
                        nameLabel.setMaxWidth(230);
                        expiryLabel.setText("Expires: " + item.getExpiryDate().toString());

                        applyLabelStyles(item, nameLabel, expiryLabel);

                        content.setStyle("-fx-background-color: Transparent;");
                        setGraphic(content);

                        setOnMouseClicked(event -> {
                            if (!isEmpty()) {
                                if (popup != null && popup.isShowing()) {
                                    popup.hide();
                                }
                                popup = createPopup(item, fridgeList.getItems());
                                showPopup(popup, this, stage);
                            }
                        });
                    }
                }
            });

        // Impact image
        Image impactImage = new Image("Impact.png");
        ImageView impactView = new ImageView(impactImage);
        impactView.setFitWidth(50);
        impactView.setFitHeight(50);
        // Add item image
        Image addItemImage = new Image("AddItem.png");
        ImageView addItemView = new ImageView(addItemImage);
        addItemView.setFitWidth(50);
        addItemView.setFitHeight(50);


        // Settings image
        Image settingsImage = new Image("Settings.png");
        ImageView settingsView = new ImageView(settingsImage);
        settingsView.setFitWidth(50);
        settingsView.setFitHeight(50);

        // Impact button
        Button impactButton = new Button("Impact");
        impactButton.getStyleClass().add("button-default");
        impactButton.setPrefWidth(150);
        impactButton.setPrefHeight(200);
        impactButton.setGraphic(impactView);
        impactButton.setContentDisplay(ContentDisplay.TOP);

        // Add Item button
        Button addItemButton = new Button("Add Item");
        addItemButton.getStyleClass().add("button-default");
        addItemButton.setPrefWidth(150);
        addItemButton.setPrefHeight(200);
        addItemButton.setGraphic(addItemView);
        addItemButton.setContentDisplay(ContentDisplay.TOP);

        // Settings button
        Button settingsButton = new Button("Settings");
        settingsButton.getStyleClass().add("button-default");
        settingsButton.setPrefWidth(150);
        settingsButton.setPrefHeight(200);
        settingsButton.setGraphic(settingsView);
        settingsButton.setContentDisplay(ContentDisplay.TOP);

        // add item popup
        Popup addItemPopup = new Popup();
        addItemPopup.setAutoHide(true);

        Rectangle popupClip = new Rectangle(400, 300);
        popupClip.setArcWidth(30);
        popupClip.setArcHeight(30);
        VBox addItemPopupVBox = new VBox();
        addItemPopupVBox.setPrefWidth(400);
        addItemPopupVBox.setPrefHeight(300);
        addItemPopupVBox.getStyleClass().add("popup-style");
        addItemPopupVBox.setAlignment(Pos.CENTER);
        addItemPopupVBox.setPadding(new Insets(10,10,10,10)); //top,right,bottom,left
        addItemPopupVBox.setSpacing(10);
        addItemPopupVBox.setClip(popupClip);

        addItemPopup.getContent().add(addItemPopupVBox);

        Label addItemPopupLabel = new Label("Adding item to " + name + "'s Fridge");
        addItemPopupLabel.getStyleClass().add("label-style-popup");

        // cancel button for add item popup
        Rectangle addItemButtonClip = new Rectangle(200, 65);
        addItemButtonClip.setArcWidth(50);
        addItemButtonClip.setArcHeight(50);
        Button addItemAddButton = new Button("Add Item");
        addItemAddButton.getStyleClass().add("button-add");
        addItemAddButton.setPrefWidth(200);
        addItemAddButton.setPrefHeight(65);
        addItemAddButton.setClip(addItemButtonClip);

        // Food item field
        Rectangle foodItemClip = new Rectangle(350, 65);
        foodItemClip.setArcWidth(50);
        foodItemClip.setArcHeight(50);
        TextField foodItemField = new TextField();
        foodItemField.setPromptText("Enter food item...");
        foodItemField.getStyleClass().add("text-field-popup");
        foodItemField.setPrefWidth(350);
        foodItemField.setPrefHeight(65);
        foodItemField.setClip(foodItemClip);

        // Expiry date field
        Rectangle expiryDateClip = new Rectangle(350, 65);
        expiryDateClip.setArcWidth(50);
        expiryDateClip.setArcHeight(50);
        TextField expiryDateField = new TextField();
        expiryDateField.setPromptText("Enter expiry date (yyyy-mm-dd)");
        expiryDateField.getStyleClass().add("text-field-popup");
        expiryDateField.setPrefWidth(350);
        expiryDateField.setPrefHeight(65);
        expiryDateField.setClip(expiryDateClip);

        addItemPopupVBox.getChildren().addAll(addItemPopupLabel ,foodItemField,
            expiryDateField, addItemAddButton);

        //show the popup when the button is clicked
        addItemButton.setOnMouseClicked(e -> {
            if (!addItemPopup.isShowing()) {
                double xPos = stage.getX()+ 50;
                double yPos = stage.getY() + 270;
                Dim();
                addItemPopup.show(stage, xPos, yPos);
            }
        });

        // Search popup
        Popup searchPopup = new Popup();

        ListView<String> searchPopupListView = new ListView<>();
        searchPopupListView.setPrefWidth(350);
        searchPopupListView.setPrefHeight(200);

        searchPopup.getContent().add(searchPopupListView);

        // Listen for text changes in the TextField
        foodItemField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                searchPopup.hide(); // Hide the popup if the input is empty
            } else {
                // Filter the items based on the input
                ArrayList<String> searchResults = Controller.getSearchResults(newValue);
                ObservableList<String> filteredItems = FXCollections.observableArrayList(searchResults);
                // update the popup list view
                searchPopupListView.setItems(filteredItems);
                searchPopupListView.setVisible(!filteredItems.isEmpty());

                if (!filteredItems.isEmpty()) {
                    Bounds bounds = foodItemField.localToScreen(foodItemField.getBoundsInLocal());
                    searchPopup.show(foodItemField, bounds.getMinX(), bounds.getMaxY());
                } else {
                    searchPopup.hide();
                }
            }
        });

        // Handle item selection from the popup
        searchPopupListView.setOnMouseClicked(event -> {
            String selectedItem = searchPopupListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                foodItemField.setText(selectedItem);
                searchPopup.hide();
            }
        });

        //should hide list popup but doesn't
//        mainScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
//            if (searchPopup.isShowing()) {
//                // Get the bounds of the foodItemField (anchor node)
//                Bounds fieldBounds = foodItemField.localToScreen(foodItemField.getBoundsInLocal());
//
//                // Check if the click is outside both the TextField and the Popup
//                boolean clickInsideField = fieldBounds.contains(event.getScreenX(), event.getScreenY());
//
//                // If the click is outside the field, hide the popup
//                if (!clickInsideField) {
//                    searchPopup.hide();
//                }
//            }
//        });

        // Hide the popup when focus is lost
        foodItemField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                searchPopup.hide();
            }
        });

        addItemAddButton.setOnAction(e -> {
            String foodName = foodItemField.getText().trim();
            String expiryDateString = expiryDateField.getText().trim();
            Duration expiryDate = null;

            if (foodName.isEmpty()) {
                // Show error message if the food name is empty
                System.out.println("Food name is required!");
            } else {
                try {
                    if (!expiryDateString.isEmpty()) {
                        // Try parsing the user-entered expiry date
                        LocalDate expiryLocalDate = LocalDate.parse(expiryDateString);
                        expiryDate = Duration.between(LocalDate.now().atStartOfDay(), expiryLocalDate.atStartOfDay());

                        if (expiryDate.isNegative()) {
                            throw new IllegalArgumentException("Expiry date cannot be in the past.");
                        }
                    } else {
                        // Fallback to the server-provided expiry date
                        expiryDate = Duration.ofDays(Controller.getExpiryDays(foodName));
                    }
                    // Create a new FoodItem and add it to the fridge
                    Food newFood = new Food(foodName, expiryDate);

                    fridge.addToFridge(newFood);
                    fridgeList.getItems().add(newFood);
                    fridgeList.getItems().sort(comparator);

                    fridgeList.setCellFactory(lv -> new ListCell<>() {
                        private final HBox content = new HBox();
                        private final Label nameLabel = new Label();
                        private final Label expiryLabel = new Label();

                        {
                            nameLabel.setAlignment(Pos.CENTER_LEFT);
                            expiryLabel.setAlignment(Pos.CENTER_RIGHT);
                            expiryLabel.setMaxWidth(Double.MAX_VALUE);
                            HBox.setHgrow(expiryLabel, Priority.ALWAYS);
                            content.setAlignment(Pos.CENTER_LEFT);
                            content.getChildren().addAll(nameLabel, expiryLabel);
                        }

                        @Override
                        protected void updateItem(Food item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                nameLabel.setText(item.getItemName());
                                nameLabel.setMaxWidth(230);
                                expiryLabel.setText("Expires: " + item.getExpiryDate().toString());
                                applyLabelStyles(item, nameLabel, expiryLabel);
                                setGraphic(content);
                            }
                        }
                    });

                    // Clear the input fields after adding
                    foodItemField.clear();
                    expiryDateField.clear();
                } catch (Exception ex) {
                    // Handle invalid expiry date format or other exceptions
                    System.out.println("Invalid expiry date. Use yyyy-MM-dd format. " + ex.getMessage());
                }
            }
        });


        //hide the popup when the cancel button is clicked
//        addItemAddButton.setOnMouseClicked(e -> {*/
            // expiry date
//            Food food = new Food(foodItemField.getText(), expiry);
//            fridge.addToFridge(food);
            //if item succesfully added, messasge label "";
            //clear text fields;
            //add item to list;
//        });

        addItemAddButton.setOnMousePressed(e -> {
            addItemAddButton.getStyleClass().remove("button-add");
            addItemAddButton.getStyleClass().add("button-add-pressed");
        });

        addItemAddButton.setOnMouseReleased(e -> {
            addItemAddButton.getStyleClass().remove("button-add-pressed");
            addItemAddButton.getStyleClass().add("button-add");
        });

        dim.setOnMouseClicked(e -> {
            addItemPopup.hide();
            UnDim();
        });

        HBox bottomButtons = new HBox(impactButton, addItemButton, settingsButton);
        bottomButtons.setAlignment(Pos.CENTER);

        HBox topNameBox = new HBox(10, nameLabel);
        topNameBox.setAlignment(Pos.TOP_LEFT);

        // Add components to the VBox
        vbox.getChildren().addAll(topNameBox, fridgeList, bottomButtons);

        stage.setScene(mainScene);
    }

    /**
     *
     * @param item The item to be added to the popup
     * @param items the items that are being displayed on the list
     * @return a popup to display
     */

    private Popup createPopup(Food item, ObservableList<Food> items) {
        Popup popup = new Popup();
        String itemName = item.getItemName();

        Rectangle popupClip = new Rectangle(440, 65);
        popupClip.setArcWidth(30);
        popupClip.setArcHeight(30);
        Button deleteButton = new Button("Delete " + itemName);
        deleteButton.getStyleClass().add("button-delete");
        deleteButton.setPrefWidth(440);
        deleteButton.setPrefHeight(65);
        deleteButton.setClip(popupClip);
        deleteButton.setOnAction(e -> {
            items.remove(item);// Remove the item from the list
            fridge.removeFromFridge(itemName);
            popup.hide(); // Close the popup
        });

        deleteButton.setOnMousePressed(e -> {
            deleteButton.getStyleClass().remove("button-delete");
            deleteButton.getStyleClass().add("button-delete-pressed");
        });
        deleteButton.setOnMouseReleased(e -> {
            deleteButton.getStyleClass().remove("button-delete-pressed");
            deleteButton.getStyleClass().add("button-delete");
        });

        deleteButton.setOnMouseClicked(Event::consume);

        // Popup content layout
        StackPane popupContent = new StackPane(deleteButton);
        popupContent.getStyleClass().add("popup-style-delete");
        popup.getContent().add(popupContent);

        // Make the popup hide automatically when clicking outside
        popup.setAutoHide(true);

        return popup;
    }

    /**
     * This methods displays the popup at a specific set location
     * @param popup the popup to be displayed
     * @param cell the cell to display the popup
     * @param stage the stage to display the popup on
     */

    private void showPopup(Popup popup, ListCell<Food> cell, Stage stage) {
        // Get the cell's position on the screen
        Bounds cellBounds = cell.localToScreen(cell.getBoundsInLocal());
        double cellX = cellBounds.getMinX() + 10;
        double cellY = cellBounds.getMinY() + 75;

        // Show the popup below the cell
        popup.show(stage, cellX, cellY);
    }

    /**
     * This method applies different styles to the labels based on the item's expiry date
     * @param item the item to be styled
     * @param nameLabel the label to be styled
     * @param expiryLabel the label to be styled
     */

    private void applyLabelStyles(Food item, Label nameLabel, Label expiryLabel) {
        String itemName = item.getItemName();
        nameLabel.getStyleClass().clear();
        expiryLabel.getStyleClass().clear();

        if (fridge.getExpiredFood().get(itemName)) {
            nameLabel.getStyleClass().add("label-cell-expired");
            expiryLabel.getStyleClass().add("label-cell-expired");
        } else if (fridge.getFoodDaysLeft(itemName) <= 2) {
            nameLabel.getStyleClass().add("label-cell-near-expiry");
            expiryLabel.getStyleClass().add("label-cell-near-expiry");
        } else {
            nameLabel.getStyleClass().add("label-cell");
            expiryLabel.getStyleClass().add("label-cell");
        }
    }

    /**
     * This method creates a dimmer for the scene
     * @param scene the scene to be dimmed
     */

    public void SceneDimmer(Scene scene){
        StackPane root = (StackPane) scene.getRoot();

        dim = new Rectangle();
        dim.setFill(new LinearGradient(
            0,1,0,0,true,CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(0,0,0,0.7)),
            new Stop(1, Color.rgb(0,0,0,0)))
        );
        dim.setManaged(false);
        dim.setVisible(false);
        dim.widthProperty().bind(root.widthProperty());
        dim.heightProperty().bind(root.heightProperty());

        root.getChildren().add(dim);
    }

    /**
     * This method dims the scene
     */

    public void Dim(){
        dim.setVisible(true);
        dim.toFront();
    }

    /**
     *  This method undims the scene
     */

    public void UnDim(){
        dim.setVisible(false);
        dim.toBack();
    }

    /**
     * This method toggles the button state, changing between pressed and not pressed
     * @param button the button to be toggled
     * @param clickedState the state of the button
     */

    private void toggleButtonState(Button button, boolean[] clickedState) {
        if (clickedState[0]) {
            button.getStyleClass().remove("button-pressed");
            button.getStyleClass().add("button-default");
        } else {
            button.getStyleClass().remove("button-default");
            button.getStyleClass().add("button-pressed");
        }
        //toggle between clicked and not clicked
        //literally just for changing button colour
        clickedState[0] = !clickedState[0];
    }

    /**
     * This method creates a dietary button
     * @param text the text to be displayed on the button
     * @return the button created
     */

    private Button createDietaryButton(String text) {
        Button button = new Button(text);

        button.getStyleClass().add("button-default");
        button.setPrefWidth(140);
        button.setPrefHeight(65);

        Rectangle clip = new Rectangle(140, 65);
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        button.setClip(clip);

        return button;
    }
}
