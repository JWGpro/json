package com.nps.json;

public class Checkpoint {
    private static long lastTime = System.currentTimeMillis();
    private static long totalTime = 0;

    public static void mark(String name) {
        long ms = System.currentTimeMillis();
        long delta = ms - lastTime;
        totalTime += delta;

        System.out.println(String.format("Execution time of '%s': %d ms (total %d ms)\n",
                name, delta, totalTime));

        lastTime = ms;
    }

}
