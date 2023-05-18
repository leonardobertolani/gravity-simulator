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
import javafx.scene.shape.Circle;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/*

TO DO

- nella finestra di generazione di una pallina aggiungi il tasto annulla e apply
- scrivi la funzione per la generazione sfasata e usala anche per add series
 */

public class SimulatorWindowController {
    @FXML private AnchorPane simulationPane;
    @FXML private Canvas drawCanvas;
    @FXML private Button simulationButton;




    List<PlanetSprite> physicalObjects;
    List<PlanetSprite> initialObjectConfiguration;
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


        physicalObjects.add(new PlanetSprite(Color.CYAN, new PVector(200, 150), new PVector(0, 0), 10));
        physicalObjects.add(new PlanetSprite(Color.ORANGE, new PVector(900, 500), new PVector(0, 0), 10));

        simulationPane.getChildren().addAll(physicalObjects);

        physicalObjects.forEach(PlanetSprite::display);

    }


    private void mainLoop() {
        // Do simulation stuff
        //for(Iterator<BouncingSprite> iterator = physicalObjects.iterator(); iterator.hasNext(); ) {

        physicalObjects.forEach(s -> {
            s.update();
            s.display();
            System.out.println(s.getAcceleration());
            //System.out.println("energia totale: " + (s.getVelocity().y*s.getVelocity().y + simulationPane.getHeight() - s.getLocation().y));
        });
        applyGravitationalForce(physicalObjects);



    }

    private void applyGravitationalForce(List<PlanetSprite> sprites) {

        PVector totalForce = new PVector(0, 0);
        for(PlanetSprite first : sprites) {

            totalForce.set(0, 0, 0);
            for(PlanetSprite second : sprites) {
                totalForce = PVector.add(totalForce, first.computeGravitationalForce(second));
                System.out.println("grav force: " + first.computeGravitationalForce(second));
                System.out.println("total force: " + totalForce);
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

        /*
        physicalObjects.clear();
        for (BouncingSprite bouncingSprite : initialObjectConfiguration) {
            physicalObjects.add(new BouncingSprite(bouncingSprite));

        }

         */



    }

    @FXML
    void onSimulate() {

        if (this.isSimulating) {
            stopSimulation();
        } else {
            resumeSimulation();
        }

    }


    @FXML
    public void onAddNewObject() {
        /*

        try {
            stopSimulation();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add-object-view.fxml"));
            DialogPane view = loader.load();
            AddObjectController controller = loader.getController();

            // Initialize the object inside the dialog pane
            controller.initialize(curveCanvas);

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("New Object");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);



            // Show the dialog and wait until the user closes it
            Optional<ButtonType> clickedButton = dialog.showAndWait();

            // Add new object to the Lists
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                //if(controller.getNewObject().isPresent()) {
                //BouncingSprite.generateDefaultPhysicalObject(simulationPane, physicalObjects, controller.getNewObject().get());
                generateDefaultPhysicalObject(controller.getNewObject());
                initialObjectConfiguration.add(controller.getNewObject());
                //}

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

         */
    }


    @FXML
    public void onAddNewSeries() {
        /*

        try {
            stopSimulation();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add-series-view.fxml"));
            DialogPane view = loader.load();
            AddSeriesController controller = loader.getController();

            // Initialize the object inside the dialog pane
            controller.initialize(curveCanvas);

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("New Object");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);


            // Show the dialog and wait until the user closes it
            Optional<ButtonType> clickedButton = dialog.showAndWait();

            // Add new object to the Lists
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                //if(controller.getNewObject().isPresent()) {
                //BouncingSprite.generateDefaultPhysicalObject(simulationPane, physicalObjects, controller.getNewObject().get());
                //generateDefaultPhysicalObjectSeries(simulationPane, physicalObjects, initialObjectConfiguration, controller.getNewSeries());
                controller.getNewSeries().forEach(this::generateDefaultPhysicalObject);
                initialObjectConfiguration.addAll(controller.getNewSeries());
                //initialObjectConfiguration.forEach(s -> System.out.println(s));
                //}

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

         */
    }




    public void generateDefaultPhysicalObject(PlanetSprite sprite) {
        physicalObjects.add(sprite);
        simulationPane.getChildren().add(sprite);
        sprite.display();
    }

    public void restoreObjects() {
        /*
        physicalObjects.clear();
        simulationPane.getChildren().clear();

        physicalObjects.addAll(initialObjectConfiguration);
        simulationPane.getChildren().addAll(initialObjectConfiguration);

        physicalObjects.forEach(Sprite::display);

         */
        List<PlanetSprite> newLink = new ArrayList<>();
        //newLink.add(new BouncingSprite());

        for(PlanetSprite b : initialObjectConfiguration) {
            newLink.add(new PlanetSprite(b));
        }

        //System.out.println("newLink: " + newLink.get(0).hashCode());

        physicalObjects.clear();
        simulationPane.getChildren().clear();
        simulationPane.getChildren().add(drawCanvas);

        physicalObjects.addAll(newLink);
        simulationPane.getChildren().addAll(newLink);

        //System.out.println("physicalObjects: " + physicalObjects.get(0).hashCode());
        //System.out.println("simulationPane: " + simulationPane.getChildren().get(1).hashCode());
        physicalObjects.forEach(s -> s.display());
    }

}