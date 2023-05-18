package com.example.gravitysimulator.objects;

public class BallsSettings {
    public static int SPRITE_COUNT = 30;
    public static double SPRITE_DRAG_COEFFICIENT = -0.05;
    public static double SPRITE_MAX_SPEED = 100;
    public static double SPRITE_DEFAULT_MASS = 10;
    public static PVector FORCE_WIND = new PVector(0.0, 0.0);
    public static PVector FORCE_GRAVITY = new PVector(0.0, 5);
    public static PVector ACCELERATION = new PVector(0.0, 0.1);
    public static double SPRITE_RADIUS = 5;
}
