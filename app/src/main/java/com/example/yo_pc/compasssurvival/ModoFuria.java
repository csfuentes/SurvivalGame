package com.example.yo_pc.compasssurvival;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class ModoFuria extends JuegoObjeto {

    private static ModoFuria modoFuriaObj;

    private int score;
    private int speed;
    private Random rand = new Random();
    private Animacion animacion = new Animacion();
    private Bitmap spritesheet;

    public ModoFuria(Bitmap res, int x, int y, int w, int h, int s, int numFrames) {
        super.x = x;
        super.y = y;
        width = 53;
        height = 30;
        score = s;

        speed = 7 + (int) (rand.nextDouble() * score / 30);

        //limite velocidad rayo
        if (speed > 40) speed = 40;

        Bitmap[] image = new Bitmap[1];

        spritesheet = res;

        image[0] = Bitmap.createBitmap(spritesheet, 0, 0, width, height);

        animacion.setFrames(image);
        animacion.setDelay(100 - speed);

    }

    public ModoFuria getPowerUp(Bitmap res, int x, int y, int w, int h, int s, int numFrames) {
        if (this.modoFuriaObj == null)
            return new ModoFuria(res, x, y, w, h, s, numFrames);
        else
            return this.modoFuriaObj;
    }

    public void update() {
        x -= speed;
        animacion.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(animacion.getImage(), x, y, null);
        } catch (Exception e) {
        }
    }

    @Override
    public int getWidth() {
        // Reducir para aumentar realismo
        return width - 10;
    }


}
