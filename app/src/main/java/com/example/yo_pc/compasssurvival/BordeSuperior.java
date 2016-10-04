package com.example.yo_pc.compasssurvival;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BordeSuperior extends JuegoObjeto{
    private Bitmap image;

    public BordeSuperior(Bitmap res, int x, int y, int h){
        height = 30;
        width = 30;

        this.x = x;
        this.y = y;

        dx = JuegoPanel.MOVESPEED;
        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update(){
        x += dx;
    }

    public void draw(Canvas canvas){
        try {
            canvas.drawBitmap(image, x, y, null);
        } catch (Exception e) {
        }
    }

}