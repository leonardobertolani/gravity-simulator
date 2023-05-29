package com.example.gravitysimulator.objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


public class PlanetSprite extends Sprite {

    final double GRAV_CONSTANT = 4;

    String name;
    boolean isVirtual;
    boolean isTracing;


    /**
     * Default constructor
     */
    public PlanetSprite() {
        super(new Circle(5, Color.WHITE),
                new PVector(400, 400),
                new PVector(0, 0),
                new PVector(0, 0),
                10);
        this.name = "Planet#";
        this.isVirtual = false;
    }



    /**
     * Constructs a new BouncingSprite object given a color, a location and a velocity.
     * By default, a BouncingSprite object is a Sprite object with circular shape, fixed radius
     * and fixed acceleration (managed by BallsSettings class).
     * @param color the color to be applied to the new BouncingSprite object
     * @param location the location for the new BouncingSprite object
     * @param velocity the velocity for the new BouncingSprite object
     */
    public PlanetSprite(String name, Color color, double radius, PVector location, PVector velocity, double mass) {
        super(new Circle(radius, color),
                location,
                velocity,
                new PVector(0, 0),
                mass);
        this.name = new String(name);
        this.isVirtual = false;
    }

    /**
     * Constructs a new BouncingSprite object from the one given as parameter.
     * @param bs the BouncingSprite object to copy
     */
    public PlanetSprite(PlanetSprite bs) {
        super(  new Circle(((Circle)bs.getView()).getRadius(), ((Circle)bs.getView()).getFill()),
                new PVector(bs.location.x, bs.location.y),
                new PVector(bs.velocity.x, bs.velocity.y),
                new PVector(0, 0),
                bs.mass);
        this.name = new String(bs.name);
        this.isVirtual = bs.isVirtual;
    }

    public String getName() {
        return name;
    }

    public double getRadius() {
        return ( (Circle) getView() ).getRadius();
    }

    public void setRadius(double radius) {
        ((Circle)this.view).setRadius(radius);
    }

    public boolean isTracing() { return this.isTracing; }


    public void setPlanet(PlanetSprite sprite) {
        this.name           = new String(sprite.getName());
        this.view           = new Circle(((Circle)sprite.getView()).getRadius(), ((Circle)sprite.getView()).getFill());
        this.location       = new PVector(sprite.location.x, sprite.location.y);
        this.velocity       = new PVector(sprite.velocity.x, sprite.velocity.y);
        this.acceleration   = new PVector(0, 0);
        this.mass           = sprite.mass;
    }

    @Override
    public void applyImpulseForce(PVector totalForce) {
        this.acceleration = totalForce.multiply(1.0/mass);
    }

    public PVector computeGravitationalForce(PlanetSprite sprite) {

        if(location.distance(sprite.location) <= getRadius() + sprite.getRadius()) {
            return new PVector(0, 0);
        }

        // Magnitude of the force
        double forceMagnitude = GRAV_CONSTANT * this.mass * sprite.mass / Math.pow(location.distance(sprite.location), 2);

        /*
        // Versor of the force
        PVector direction = new PVector();
        direction.set(sprite.location.add(PVector.multiply(this.location, -1)));

        direction.set(direction.normalize());

        return direction.multiply(forceMagnitude);

         */
        return sprite.location.add(this.location.multiply(-1))
                .normalize()
                .multiply(forceMagnitude);
    }
}
