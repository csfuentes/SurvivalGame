package com.example.yo_pc.compasssurvival;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Jugador extends JuegoObjeto {
    private Bitmap spritesheet, powerUpSheet;
    private int score;

    private boolean up;
    private boolean playing;
    private Animacion animacion = new Animacion();
    private long startTime;

    private boolean modoFuriaOn = false;
    long PowerUpTime = 0;

    public Jugador(Bitmap res, int w, int h, int numFrames) {

        x = 100;
        y = JuegoPanel.HEIGHT / 2;
        dy = 0;
        score = 0;
        height = 40;
        width = 70;

        spritesheet = res;

        setAnimation(res, numFrames);

        startTime = System.nanoTime();

    }

    private void setAnimation(Bitmap res, int numFrames) {
        Bitmap[] image = new Bitmap[1];
        Bitmap spritesheet = res;

        image[0] = Bitmap.createBitmap(spritesheet, 0, 0, width, height);

        animacion.setFrames(image);
        animacion.setDelay(10);
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }
        animacion.update();

        if (up) {
            dy -= 1;

        } else {
            dy += 1;
        }

        if (dy > 6) dy = 6;
        if (dy < -6) dy = -6;

        y += dy * 2;


        if (modoFuriaOn) {
            long elapsedPowerUp = (System.nanoTime() - PowerUpTime) / 1000000;
            if (elapsedPowerUp > 2500) {
                setAnimation(spritesheet, 3);
                modoFuriaOn = false;
            }
        }

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animacion.getImage(), x, y, null);
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDY() {
        dy = 0;
    }

    public void resetScore() {
        score = 0;
    }

    public boolean getModoFuriaOn() {
        return this.modoFuriaOn;
    }

    public void PowerUpOn(Bitmap res) {

        powerUpSheet = res;
        setAnimation(powerUpSheet, 3);

        PowerUpTime = System.nanoTime();
        this.modoFuriaOn = true;

    }
}