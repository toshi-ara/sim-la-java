package io.github.toshiara.simla;


public class Constants {
    private Constants() {}

    // threshold of probability not to respond
    public static final double ProbThreshold = 0.05;

    // Timer
    public static final int TIMER_INTERVAL = 20;

    // Response duration (msec) of animation
    public static final int ACTIVE_DURATION = 300;


    public static final double[][] CIRCLES = {
        {272.0, 122.0}, {342.0, 122.0}, {412.0, 122.0},
        {272.0, 178.0}, {342.0, 178.0}, {412.0, 178.0}
    };

    public static final double RADIUS = 18.0;

    public static final String[] drugName = {
        "Saline",
        "Pro",
        "Lid",
        "Mep",
        "Bup",
        "Lid + Adr"
    };
}

