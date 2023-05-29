package com.example.gravitysimulator;

import com.example.gravitysimulator.objects.PVector;
import com.example.gravitysimulator.objects.PlanetSprite;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*

IDEE

- implementa una finestra per la creazione di oggetti colorati, con nome e informazioni da poter visualizzare all'occorrenza
- crea una finestra di visualizzazione statistiche, in cui si mostra magari info come la forza risultante di ogni particella, la sua velocità e altro
- implementa funzionalità di particelle "di prova", cioè che sono soggette al campo ma non lo producono a loro volta
- implementa slider per modificare G
- tracciante colorato per i pianeti

- controllo maggiore sugli oggetti a disposizione: settare nome, massa, velocità, colore, poterle cambiare durante l'esecuzione, eliminare

FUNZIONALITA':
- Slider per agire su G
- Pianeti per ottenere la lista degli oggetti e modificarli
- funzione SETTINGS per modificare in un colpo solo G, vedere la lista dei pianeti con tableView e modificarli, attivare o disattivare il tracciante colore
- funzione STATUS per visualizzare qualche informazione fisica indipendente sulla simulazione attuale, come il centro di massa, i secondi dall'inizio simulazione
 */

public class SimulatorWindowController {

    /* ------------------ CENTER VARIABLES --------------- */
    @FXML private AnchorPane simulationPane;
    @FXML private Canvas drawCanvas;



    /* -------------- RIGHT SIDE VARIABLES ---------------- */
    @FXML private Canvas planetCanvas;
    @FXML private Label planetNameLabel;
    @FXML private Label planetPositionLabel;
    @FXML private Label planetVelocityLabel;
    @FXML private Slider radiusSlider;
    @FXML private Slider massSlider;



    /* -------------- LEFT SIDE VARIABLES -------------- */
    @FXML private Button simulationButton;


    List<PlanetSprite> physicalObjects;
    List<PlanetSprite> initialObjectConfiguration;
    int selectedPlanetIndex;
    AnimationTimer timer;
    boolean isSimulating;


    /**
     * First method of the simulation program. It is used to
     * set up all the objects.
     */
    void onStart() {
        this.isSimulating = false;
        simulationButton.setText("Simulate");

        initializeTimer();
        initializeSimulation();
    }


    /**
     * Method for create a new timer and linking it
     * to the mainLoop() function.
     */
    void initializeTimer() {

        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                mainLoop();
            }
        };
    }

    /**
     * Method specifically written to set up all the stuff
     * regarding the simulation. It's been called in the onStart() method.
     */
    @FXML
    public void initializeSimulation() {

        double h = simulationPane.getHeight();
        double w = simulationPane.getWidth();

        physicalObjects = new ArrayList<>();
        initialObjectConfiguration = new ArrayList<>();

        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);


        //physicalObjects.add(new PlanetSprite("ef", Color.CYAN, 10, new PVector(400, 300), new PVector(0, 1), 100));
        //physicalObjects.add(new PlanetSprite("gnof", Color.ORANGE, 10, new PVector(600, 300), new PVector(0, -1), 100));
        //physicalObjects.add(new PlanetSprite(Color.GREEN, new PVector(300, 200), new PVector(0, 0), 200));
        //physicalObjects.add(new PlanetSprite(Color.YELLOW, new PVector(500, 200), new PVector(-1, 0), 100));
        //physicalObjects.add(new PlanetSprite(Color.GREEN, new PVector(800, 400), new PVector(-1, 0.33333), 5));
        //selectedPlanet = physicalObjects.get(0);

        // Generating and adding a default object
        PlanetSprite newObject = new PlanetSprite("AlphaCentauri", Color.GOLDENROD, 30, new PVector(300, 300), new PVector(0, 0), 200);
        newObject.setOnMouseClicked(event -> {
            selectedPlanetIndex = physicalObjects.indexOf((PlanetSprite) event.getSource());
            System.out.println(selectedPlanetIndex);
            updatePlanetInfo();

        });
        selectedPlanetIndex = 0;

        generateDefaultPhysicalObject(newObject);

        newObject = new PlanetSprite(newObject);
        initialObjectConfiguration.add(newObject);


        // Initialising the sliders
        radiusSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(physicalObjects.size() != 0) {
                physicalObjects.get(selectedPlanetIndex).setRadius(newValue.doubleValue());
            }
        }));
        massSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (physicalObjects.size() != 0) {
                physicalObjects.get(selectedPlanetIndex).setMass(newValue.doubleValue());
            }
        }));

    }


    private void mainLoop() {
        // Do simulation stuff
        //for(Iterator<BouncingSprite> iterator = physicalObjects.iterator(); iterator.hasNext(); ) {

        physicalObjects.forEach(s -> {
            s.update();
            s.display();
            updatePlanetInfo();
        });
        applyGravitationalForce(physicalObjects);
        //initialObjectConfiguration.forEach(s -> System.out.println(s));
    }


    /**
     * Function called when the user clicks on a planet
     */
    public void updatePlanetInfo() {

        this.planetNameLabel.setText(physicalObjects.get(selectedPlanetIndex).getName());
        this.planetCanvas.getGraphicsContext2D().setFill(((Shape)physicalObjects.get(selectedPlanetIndex).getView()).getFill());
        this.planetCanvas.getGraphicsContext2D().fillOval(50, 50, 100, 100);
        this.planetPositionLabel.setText(String.format("Position: (x: %.2f, y: %.2f)", physicalObjects.get(selectedPlanetIndex).getLocation().x, drawCanvas.getHeight() - physicalObjects.get(selectedPlanetIndex).getLocation().y));
        this.planetVelocityLabel.setText(String.format("Velocity: (x: %.2f, y: %.2f)", physicalObjects.get(selectedPlanetIndex).getVelocity().x, -physicalObjects.get(selectedPlanetIndex).getVelocity().y));
        this.radiusSlider.setValue(physicalObjects.get(selectedPlanetIndex).getRadius());
        this.massSlider.setValue(physicalObjects.get(selectedPlanetIndex).getMass());

    }

    private void applyGravitationalForce(List<PlanetSprite> sprites) {

        PVector totalForce = new PVector();
        for(PlanetSprite first : sprites) {

            totalForce.set(0, 0, 0);
            for(PlanetSprite second : sprites) {
                totalForce.set(totalForce.add(first.computeGravitationalForce(second)));
            }

            first.applyImpulseForce(totalForce);
        }
    }

    void stopSimulation() {
        this.isSimulating = false;
        simulationButton.setText("Simulate");
        timer.stop();
    }

    void resumeSimulation() {
        this.isSimulating = true;
        simulationButton.setText("Stop");
        timer.start();
    }

    @FXML
    void restartSimulation() {

        stopSimulation();
        restoreObjects();

    }




    /* ---------------------------------- LEFT SIDE PANEL ---------------------------- */



    @FXML
    void onSimulate() {

        if (this.isSimulating) {
            stopSimulation();
        } else {
            resumeSimulation();
        }

    }


    public void restoreObjects() {

        List<PlanetSprite> newLink = new ArrayList<>();

        for(PlanetSprite b : initialObjectConfiguration) {
            PlanetSprite newObject = new PlanetSprite(b);
            newObject.setOnMouseClicked(event -> {
                selectedPlanetIndex = physicalObjects.indexOf((PlanetSprite) event.getSource());
                System.out.println(selectedPlanetIndex);
                updatePlanetInfo();
            });
            newLink.add(newObject);
        }

        physicalObjects.clear();
        simulationPane.getChildren().clear();
        simulationPane.getChildren().add(drawCanvas);

        physicalObjects.addAll(newLink);
        simulationPane.getChildren().addAll(newLink);

        physicalObjects.forEach(s -> s.display());
    }


    @FXML
    public void onAddNewObject() {

        try {
            stopSimulation();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add-object-view.fxml"));
            DialogPane view = loader.load();
            AddObjectController controller = loader.getController();

            // Initialize the object inside the dialog pane
            controller.setPlanet(new PlanetSprite());
            controller.initialize(drawCanvas);


            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("New Object");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);



            // Show the dialog and wait until the user closes it
            Optional<ButtonType> clickedButton = dialog.showAndWait();

            // Add new object to the Lists
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Adding the new object to the arrayLists and setting the mouse listener
                PlanetSprite newObject = controller.getNewObject();
                newObject.setOnMouseClicked(event -> {
                    selectedPlanetIndex = physicalObjects.indexOf((PlanetSprite) event.getSource());
                    System.out.println(selectedPlanetIndex);
                    updatePlanetInfo();
                });

                generateDefaultPhysicalObject(newObject);

                // Adding a new reference of the object to the initial configuration set
                newObject = controller.getNewObject();
                initialObjectConfiguration.add(newObject);
                //}

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void generateDefaultPhysicalObject(PlanetSprite sprite) {
        physicalObjects.add(sprite);
        simulationPane.getChildren().add(sprite);
        sprite.display();
    }




    /* --------------------------------------- RIGHT SIDE PANEL ----------------------------------------- */



    @FXML
    public void onDeletePlanet() {

        if(physicalObjects.size() > 0) {
            physicalObjects.remove(selectedPlanetIndex);
            initialObjectConfiguration.remove(selectedPlanetIndex);
            simulationPane.getChildren().remove(selectedPlanetIndex + 1);

            // By default, we select back the first one
            selectedPlanetIndex = 0;

            if (physicalObjects.size() == 0) {
                // If it's empty, we must set the right panel to its initial value
                this.planetNameLabel.setText("Name");
                this.planetCanvas.getGraphicsContext2D().clearRect(0, 0, this.planetCanvas.getWidth(), this.planetCanvas.getHeight());
                this.planetPositionLabel.setText("Position: ");
                this.planetVelocityLabel.setText("Velocity: ");
                this.radiusSlider.setValue(this.radiusSlider.minProperty().getValue());
                this.massSlider.setValue(this.massSlider.minProperty().getValue());
            }
            else {
                updatePlanetInfo();
            }

        }

    }


    public void tracePlanets() {
        physicalObjects.forEach(s -> {
            if(s.isTracing()) {

            }
        });
    }

}