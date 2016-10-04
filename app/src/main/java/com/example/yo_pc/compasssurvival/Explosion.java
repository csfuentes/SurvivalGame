package com.example.yo_pc.compasssurvival;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Explosion{
    private int x;
    private int y;
    private int width;
    private int height;
    private int row;
    private Animacion animacion = new Animacion();
    private Bitmap spritesheet;

    public Explosion(Bitmap res, int x, int y, int w, int h, int numFrames){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length; i++){
            if(i%5==0&&i>0)row++;
            image[i] = Bitmap.createBitmap(spritesheet, (i-(5*row))*width, row*height, width, height);
        }
        animacion.setFrames(image);
        animacion.setDelay(20);



    }
    public void draw(Canvas canvas){
        if(!animacion.playedOnce()){
            canvas.drawBitmap(animacion.getImage(),x,y,null);
        }

    }
    public void update()
    {
        if(!animacion.playedOnce()){
            animacion.update();
        }
    }
    public int getHeight(){return height;}
}