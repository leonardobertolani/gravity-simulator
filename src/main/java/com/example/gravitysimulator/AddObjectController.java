package com.example.gravitysimulator;


import com.example.gravitysimulator.objects.PVector;
import com.example.gravitysimulator.objects.PlanetSprite;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

/*
TO DO

- Metti a posto l'eccezione che non genera l'alert
 */

public class AddObjectController {

    @FXML private TextField nameField;
    @FXML private ColorPicker objectColorPicker;
    @FXML private TextField radiusField;
    @FXML private TextField xPositionField;
    @FXML private TextField xVelocityField;
    @FXML private TextField yPositionField;
    @FXML private TextField yVelocityField;
    @FXML private TextField massField;
    @FXML private Canvas viewCanvas;


    PlanetSprite sprite;
    Canvas canvas;

    public void setPlanet(PlanetSprite sprite) {

        this.sprite = new PlanetSprite(sprite);
    }

    public void initialize(Canvas canvas) {

        nameField.setText(sprite.getId());
        radiusField.setText(String.valueOf(sprite.getRadius()));
        xPositionField.setText(String.valueOf(sprite.getLocation().x));
        yPositionField.setText(String.valueOf(sprite.getLocation().y));
        xVelocityField.setText(String.valueOf(sprite.getVelocity().x));
        yVelocityField.setText(String.valueOf(sprite.getVelocity().y));
        massField.setText(String.valueOf(sprite.getMass()));

        viewCanvas.getGraphicsContext2D().setFill(objectColorPicker.getValue());
        viewCanvas.getGraphicsContext2D().fillOval(50, 50, 100, 100);
        objectColorPicker.setOnAction((event) -> {
            viewCanvas.getGraphicsContext2D().setFill(objectColorPicker.getValue());
            viewCanvas.getGraphicsContext2D().fillOval(50, 50, 100, 100);
        });

        this.canvas = canvas;

    }

    public PlanetSprite getNewObject() {

        try {

            sprite =  new PlanetSprite(
                    nameField.getText(),
                    objectColorPicker.getValue(),
                    Double.parseDouble(radiusField.getText()),
                    new PVector(Double.parseDouble(xPositionField.getText()), this.canvas.getHeight() - Double.parseDouble(yPositionField.getText())),
                    new PVector(Double.parseDouble(xVelocityField.getText()), -Double.parseDouble(yVelocityField.getText())),
                    Double.parseDouble(massField.getText())
            );
        }
        catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Hai inserito un valore non valido!").showAndWait();
        }

        return sprite;

    }

}
