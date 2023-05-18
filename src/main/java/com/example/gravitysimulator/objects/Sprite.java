package com.example.gravitysimulator.objects;


import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class Sprite extends Region {
    PVector location;
    PVector velocity;
    PVector acceleration;
    double mass = BallsSettings.SPRITE_DEFAULT_MASS;
    Node view;

    public Sprite(Node view) {
        this.view = view;
        this.location = new PVector(0, 0);
        this.velocity = new PVector(0, 0);
        this.acceleration = new PVector(0, 0);
        getChildren().add(view);
    }

    public Sprite(Node view, PVector location) {
        this.view = view;
        this.location = location;
        this.velocity = new PVector(0, 0);
        this.acceleration = new PVector(0, 0);
        getChildren().add(view);
    }

    public Sprite(Node view, PVector location, PVector velocity) {
        this.view = view;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = new PVector(0, 0);
        getChildren().add(view);
    }

    public Sprite(Node view, PVector location, PVector velocity, PVector acceleration) {
        this.view = view;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
        getChildren().add(view);
    }

    public Sprite(Node view, PVector location, PVector velocity, PVector acceleration, double mass) {
        this.view = view;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.mass = mass;
        getChildren().add(view);
    }

    public PVector getLocation() {
        return location;
    }


    public PVector getVelocity() {
        return velocity;
    }

    public PVector getAcceleration() {
        return acceleration;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    public void applyImpulseForce(PVector force) {
        velocity = velocity.add(force.multiply(1 / mass));
    }

    public void update() {
        velocity = velocity.add(acceleration);
        velocity = velocity.limit(BallsSettings.SPRITE_MAX_SPEED);
        location = location.add(velocity);
    }

    public boolean intersects(Sprite other) {
        return getBoundsInParent().intersects(other.getBoundsInParent());
    }

    public boolean contains(Point2D point) {
        return getBoundsInParent().contains(point);
    }

    public void display() {
        setTranslateX(location.x);
        setTranslateY(location.y);
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "location=" + location +
                ", velocity=" + velocity +
                ", acceleration=" + acceleration +
                ", mass=" + mass +
                ", view=" + view +
                '}';
    }
}
