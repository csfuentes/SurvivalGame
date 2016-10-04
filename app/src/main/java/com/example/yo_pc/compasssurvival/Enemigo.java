package com.example.yo_pc.compasssurvival;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Enemigo extends JuegoObjeto{
    private int score;
    private int speed;
    private Random rand = new Random();
    private Animacion animacion = new Animacion();
    private Bitmap spritesheet;

    public Enemigo(Bitmap res, int x, int y, int w, int h, int s, int numFrames){
        super.x = x;
        super.y = y;
        width = 48;
        height = 48;
        score = s;

        speed = 7 + (int) (rand.nextDouble()*score/30);

        if(speed>40)speed = 40;

        Bitmap[] image = new Bitmap[1];

        spritesheet = res;

        image[0] = Bitmap.createBitmap(spritesheet, 0, 0, width, height);


        animacion.setFrames(image);
        animacion.setDelay(100-speed);

    }

    public void update(){
        x-=speed;
        animacion.update();
    }

    public void draw(Canvas canvas){

        try{
            canvas.drawBitmap(animacion.getImage(),x,y,null);
        }catch(Exception e){}
    }

    @Override
    public int getWidth(){
        //offset slightly for more realistic collision detection
        return width-10;
    }

}