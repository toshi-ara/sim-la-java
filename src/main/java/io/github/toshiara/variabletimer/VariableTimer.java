package io.github.toshiara.variabletimer;

public class VariableTimer {

    private boolean running = false;
    private long lastTime;
    private long elapsedTime;
    private int speed = 1;

    public void start() {
        if (!running) {
            lastTime = System.nanoTime();
            running = true;
        }
    }

    public void pause() {
        if (running) {
            update();
            running = false;
        }
    }

    public void reset() {
        running = false;
        elapsedTime = 0;
        speed = 1;
    }

    public void update() {
        if (!running) return;

        long now = System.nanoTime();
        long delta = now - lastTime;
        lastTime = now;

        elapsedTime += (long)(delta * speed);
    }

    public void changeSpeed(int newSpeed) {
        if (running) {
            update();
            // lastTime = System.nanoTime(); // done in update()
        }
        speed = newSpeed;
    }

    // public long getMillis() {
    //     return elapsedTime / 1_000_000;
    // }

    public long getSecond() {
        return elapsedTime / 1_000_000_000;
    }

    public boolean isRunning() {
        return running;
    }
}

