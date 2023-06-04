package com.example.gravitysimulator.objects;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class PlanetSprite extends Sprite {

    final double GRAV_CONSTANT = 1;

    boolean isTracing;


    /**
     * Default constructor
     */
    public PlanetSprite() {
        super(  "Planet#",
                new Circle(5, Color.WHITE),
                new PVector(400, 400),
                new PVector(0, 0),
                new PVector(0, 0),
                10);
    }



    /**
     * Constructs a new PlanetSprite object given a name, a color, a radius, a location, a velocity and a mass.
     * @param name the name of the planet
     * @param color the color of the planet
     * @param radius the radius of the planet
     * @param location the location for the new BouncingSprite object
     * @param velocity the velocity for the new BouncingSprite object
     * @param mass the mass of the planet
     */
    public PlanetSprite(String name, Color color, double radius, PVector location, PVector velocity, double mass) {
        super(  name,
                new Circle(radius, color),
                location,
                velocity,
                new PVector(0, 0),
                mass);
    }

    /**
     * Constructs a new PlanetSprite object from the one given as parameter.
     * @param bs the PlanetSprite object to copy
     */
    public PlanetSprite(PlanetSprite bs) {
        super(  bs.getId(),
                new Circle(((Circle)bs.getView()).getRadius(), ((Circle)bs.getView()).getFill()),
                new PVector(bs.location.x, bs.location.y),
                new PVector(bs.velocity.x, bs.velocity.y),
                new PVector(0, 0),
                bs.mass);
    }

    /**
     * Returns the Node associated to the object.
     * By default, every PlanetSprite object can contain only one Node
     */
    public Node getView() {
        return getChildren().get(0);
    }

    /**
     * Returns the radius of the PlanetSprite object
     */
    public double getRadius() {
        return ( (Circle) getView() ).getRadius();
    }

    /**
     * Sets the radius of the PlanetSprite object to the value
     * passed as parameter
     * @param radius final radius of the object
     */
    public void setRadius(double radius) {
        ((Circle)getView()).setRadius(radius);
    }

    /**
     * Determines the acceleration of the PlanetSprite object given
     * the total force applied on it.
     * This method overrides the Sprite class method.
     * @param totalForce the total force applied to the object
     */
    @Override
    public void applyImpulseForce(PVector totalForce) {
        this.acceleration = totalForce.multiply(1.0/mass);
    }


    /**
     * Updates the velocity and location of the PlanetSprite object.
     * Also, this method is responsible for the drawing of the planet orbits
     * on the canvas passed as parameter
     * @param canvas the canvas to draw on
     */
    public void update(Canvas canvas) {
        double x = location.x;
        double y = location.y;

        velocity = velocity.add(acceleration);
        velocity = velocity.limit(BallsSettings.SPRITE_MAX_SPEED);
        location = location.add(velocity);

        if(isTracing) {
            canvas.getGraphicsContext2D().setStroke(((Circle)getView()).getFill());
            canvas.getGraphicsContext2D().strokeLine(x, y, location.x, location.y);
        }
    }

    /**
     * Toggles the orbit tracing of the PlanetSprite object
     */
    public void toggleTrace() {
        this.isTracing = !this.isTracing;
    }

    /**
     * Displays the PlanetSprite object on the Pane
     */
    public void display() {
        // update position on parent component (Region)
        setTranslateX(location.x);
        setTranslateY(location.y);
    }

    /**
     * Computes the force applied between this PlanetSprite object and the one
     * passed as parameter using the Newton's gravitation law.
     * @param sprite the other PlanetSprite
     * @return a PVector representing the force between the two PlanetSprites
     */
    public PVector computeGravitationalForce(PlanetSprite sprite) {


        if(location.distance(sprite.location) <= 5) {//getRadius() + sprite.getRadius()) {
            return new PVector(0, 0);
        }


        // Magnitude of the force
        double forceMagnitude = GRAV_CONSTANT * this.mass * sprite.mass / Math.pow(location.distance(sprite.location), 2);


        return sprite.location.add(this.location.multiply(-1))
                .normalize()
                .multiply(forceMagnitude);
    }
}
