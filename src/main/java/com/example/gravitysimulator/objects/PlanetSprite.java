package com.example.gravitysimulator.objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class PlanetSprite extends Sprite {

    final double GRAV_CONSTANT = 100;

    /**
     * Constant used to apply very small increments during the operations
     * on the coordinates.
     */
    final double dX_INCREMENT = 0.0005;


    /**
     * Constructs a new BouncingSprite object given a color, a location and a velocity.
     * By default, a BouncingSprite object is a Sprite object with circular shape, fixed radius
     * and fixed acceleration (managed by BallsSettings class).
     * @param color the color to be applied to the new BouncingSprite object
     * @param location the location for the new BouncingSprite object
     * @param velocity the velocity for the new BouncingSprite object
     */
    public PlanetSprite(Color color, PVector location, PVector velocity, double mass) {
        super(new Circle(BallsSettings.SPRITE_RADIUS, color),
                location,
                velocity,
                new PVector(0, 0),
                mass);
    }

    /**
     * Constructs a new BouncingSprite object from the one given as parameter.
     * @param bs the BouncingSprite object to copy
     */
    public PlanetSprite(PlanetSprite bs) {
        super(  bs.getView(),
                new PVector(bs.location.x, bs.location.y),
                new PVector(bs.velocity.x, bs.velocity.y),
                new PVector(0, 0),
                bs.mass);
    }

    @Override
    public void applyImpulseForce(PVector totalForce) {
        this.acceleration = totalForce.multiply(1.0/mass);
    }

    public PVector computeGravitationalForce(PlanetSprite sprite) {

        if(location.distance(sprite.location) == 0) {
            return new PVector(0, 0);
        }

        // Magnitude of the force
        double forceMagnitude = GRAV_CONSTANT * this.mass * sprite.mass / Math.pow(location.distance(sprite.location), 2);

        // Versor of the force
        PVector direction = new PVector();
        direction.set(sprite.location.add(PVector.multiply(this.location, -1)));

        direction.set(direction.normalize());

        return direction.multiply(forceMagnitude);
    }
}
