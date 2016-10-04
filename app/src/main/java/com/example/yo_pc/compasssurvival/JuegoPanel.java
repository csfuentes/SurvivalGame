package com.example.yo_pc.compasssurvival;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.Random;

public class JuegoPanel extends SurfaceView implements SurfaceHolder.Callback{
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private long powerUpTime;
    private long enemigoStartTime;
    private MainThread thread;
    private Fondo bg, bg2;
    private Jugador jugador;
    //private ArrayList<Humo> smoke;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<BordeSuperior> bordeSuperior;
    private ArrayList<BordeInferior> bordeInferior;
    private ModoFuria modoFuria;
    private Random rand = new Random();
    private int tamBordes;
    private boolean isReady;
    private Context mContext;


    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;

    private SoundPool sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    int soundId = sp.load(getContext(), R.raw.explosion, 1);
    int soundId2 = sp.load(getContext(), R.raw.explosionfail, 1);
    int soundId3 = sp.load(getContext(), R.raw.powerup, 1);

    private SoundPool sp2 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    int soundId4 = sp2.load(getContext(), R.raw.tot2, 1);


    public JuegoPanel(Context context){
        super(context);
        this.mContext = context;
        
        getHolder().addCallback(this);
        
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter < 1000){
            counter++;
            try{
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.yo_pc.compasssurvival", 0);

        if(spp.getInt("mapa", 1) == 1){
            bg = new Fondo(BitmapFactory.decodeResource(getResources(), R.drawable.fondo1));
            bg.setVector(-1);
            bg2 = new Fondo(BitmapFactory.decodeResource(getResources(), R.drawable.fondo2));
            bg2.setVector(-5);
        }

        if(spp.getInt("mapa", 1) == 2){
            bg = new Fondo(BitmapFactory.decodeResource(getResources(), R.drawable.fondo3));
            bg.setVector(-1);
            bg2 = new Fondo(BitmapFactory.decodeResource(getResources(), R.drawable.fondo4));
            bg2.setVector(-5);
        }

        if(spp.getInt("mapa", 1) == 3){
            bg = new Fondo(BitmapFactory.decodeResource(getResources(), R.drawable.fondo5));
            bg.setVector(-1);
            bg2 = new Fondo(BitmapFactory.decodeResource(getResources(), R.drawable.fondo6));
            bg2.setVector(-5);
        }

        Log.d("Valor de personaje: ", Integer.toString(spp.getInt("personaje", 1)));

        if(spp.getInt("personaje", 1) == 1){
            jugador = new Jugador(BitmapFactory.decodeResource(getResources(), R.drawable.jugador1), 66, 40, 3);
        }

        if(spp.getInt("personaje", 1) == 2){
            jugador = new Jugador(BitmapFactory.decodeResource(getResources(), R.drawable.jugador2), 66, 40, 3);
        }

        if(spp.getInt("personaje", 1) == 3){
            jugador = new Jugador(BitmapFactory.decodeResource(getResources(), R.drawable.jugador3), 66, 40, 3);
        }
        if((spp.getInt("personaje", 1) != 1) && (spp.getInt("personaje", 1) != 2) && (spp.getInt("personaje", 1) != 3)){
            jugador = new Jugador(BitmapFactory.decodeResource(getResources(), R.drawable.jugador1), 66, 40, 3);
        }

        enemigos = new ArrayList<Enemigo>();
        bordeSuperior = new ArrayList<BordeSuperior>();
        bordeInferior = new ArrayList<BordeInferior>();
        powerUpTime = System.nanoTime();
        enemigoStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);

        //inciamos el loop ahora que es seguro
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!jugador.getPlaying() && isReady && reset){
                jugador.setPlaying(true);
                jugador.setUp(true);
            }
            if(jugador.getPlaying()){

                if (!started) started = true;
                reset = false;
                jugador.setUp(true);
            }
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            jugador.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        if(jugador.getPlaying()){

            if(bordeInferior.isEmpty()){
                jugador.setPlaying(false);
                return;
            }
            if(bordeSuperior.isEmpty()){
                jugador.setPlaying(false);
                return;
            }

            bg.update();
            bg2.update();
            jugador.update();

            // hay colision en el borde inferior?
            for(int i = 0; i < bordeInferior.size(); i++){
                if((collision(bordeInferior.get(i), jugador) || playerOutOfScreen()) && !jugador.getModoFuriaOn()){
                    actualizarRecord();
                    soundExplosion();
                    vibrate(500);
                    soundTot();
                    jugador.setPlaying(false);
                }
            }

            // hay colision en el borde superior?
            for(int i = 0; i < bordeSuperior.size(); i++){
                if((collision(bordeSuperior.get(i), jugador) || playerOutOfScreen()) && !jugador.getModoFuriaOn()){
                    actualizarRecord();
                    soundExplosion();
                    vibrate(500);
                    soundTot();
                    jugador.setPlaying(false);
                }
            }


            this.actualizarBordeSuperior();

            this.actualizarBordeInferior();

            //añadir enemigos
            long enemigoElapsed = (System.nanoTime() - enemigoStartTime) / 1000000;
            if(enemigoElapsed > (2000 - jugador.getScore() / 4)){

                // el primer misil siempre empieza en el mismo sitio
                if(enemigos.size() == 0) {
                    enemigos.add(new Enemigo(BitmapFactory.decodeResource(getResources(), R.drawable.zombie),
                            WIDTH + 10, HEIGHT / 2, 45, 15, jugador.getScore(), 13));
                }
                else{
                    enemigos.add(new Enemigo(BitmapFactory.decodeResource(getResources(), R.drawable.zombie),
                            WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - (tamBordes * 2)) + tamBordes), 45, 15, jugador.getScore(), 13));
                }

                //reiniciar timer
                enemigoStartTime = System.nanoTime();
            }
            // colision con un enemigo?
            for(int i = 0; i < enemigos.size(); i++){
                //update enemigos
                enemigos.get(i).update();

                if(collision(enemigos.get(i), jugador)){
                    enemigos.remove(i);

                    if(!jugador.getModoFuriaOn()){
                        actualizarRecord();
                        soundExplosion();
                        vibrate(500);
                        soundTot();
                        jugador.setPlaying(false);
                    }
                    else{
                        soundFailedExplosion();
                        vibrate(100);
                        jugador.addScore(50);
                    }
                    break;
                }


                //eliminar enemigo si aparece fuera de pantalla
                if(enemigos.get(i).getX() < -100){
                    enemigos.remove(i);
                    break;
                }


                // añadir rayos para modo furia
                long elapsed = (System.nanoTime() - powerUpTime) / 1000000;
                if (elapsed > 120){
                    if (modoFuria == null){
                        modoFuria = new ModoFuria(BitmapFactory.decodeResource(getResources(), R.drawable.rayo),
                                WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - (tamBordes * 2)) + tamBordes), 45, 15, jugador.getScore(), 13);

                    }
                    if (modoFuria != null){
                        //update modoFuria
                        modoFuria.update();

                        //comprueba que haya colision mientras tenemos modo furia para matar al enemigo
                        if (collision(modoFuria, jugador)){
                            modoFuria = null;
                            soundPowerUp();
                            jugador.addScore(25);
                            //jugador.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.jugadormodofuria1));

                            if(spp.getInt("personaje", 1) == 1){
                                jugador.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.jugadormodofuria1));
                            }

                            if(spp.getInt("personaje", 1) == 2){
                                jugador.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.jugadormodofuria2));
                            }

                            if(spp.getInt("personaje", 1) == 3){
                                jugador.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.jugadormodofuria3));
                            }
                            if((spp.getInt("personaje", 1) != 1) && (spp.getInt("personaje", 1) != 2) && (spp.getInt("personaje", 1) != 3)){
                                jugador.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.jugadormodofuria1));
                            }

                        }

                        //eliminar rayo si aparece fuera de pantalla
                        else if(modoFuria.getX() < -100){
                            modoFuria = null;
                        }
                    }
                }
            }
        }
        else{
            jugador.resetDY();
            if (!reset){
                isReady = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), jugador.getX(),
                        jugador.getY() - 30, 100, 100, 25);
            }

            explosion.update();
            long resetElapsed = (System.nanoTime() - startReset) / 1000000;

            if (resetElapsed > 2500 && !isReady){
                newGame();
            }
        }
    }

    private void actualizarRecord(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.yo_pc.compasssurvival", 0);

        if(jugador.getScore() > spp.getInt("record", 0)){
            spp.edit().putInt("record", jugador.getScore()).commit();
        }



        Puntuacion p = new Puntuacion(jugador.getScore(), "Javi");


        p.llenarRanking();

    }

    private void vibrate(int time){
        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.yo_pc.compasssurvival", 0);

        if(spp.getInt("vibracionEnabled",1)==1) {
            v.vibrate(time);
        }
    }

    private void soundExplosion(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        if(spp.getInt("efectosEnabled", 1) == 1)
            sp.play(soundId, .4f, .4f, 0, 0, 1);
    }

    private void soundFailedExplosion(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        if(spp.getInt("efectosEnabled", 1) == 1)
            sp.play(soundId2, .9f, .9f, 0, 0, 1);
    }

    private void soundPowerUp(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        if(spp.getInt("efectosEnabled", 1) == 1)
            sp.play(soundId3, .5f, .5f, 0, 0, 1);
    }

    private void soundTot(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        if(spp.getInt("efectosEnabled", 1) == 1)
            sp2.play(soundId4, .9f, .9f, 0, 0, 1);
    }

    public boolean playerOutOfScreen(){
        if(jugador.getY() < 0 || jugador.getY() > HEIGHT) return true;
        return false;
    }

    public boolean collision(JuegoObjeto a, JuegoObjeto b){
        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas){
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if(canvas != null){
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            bg2.draw(canvas);

            if(!dissapear){
                jugador.draw(canvas);
            }

            //dibujar rayo
            if(modoFuria != null) modoFuria.draw(canvas);

            //dibujar enemigos
            for(Enemigo m : enemigos){
                m.draw(canvas);
            }


            //dibujar bordeSuperior
            for(BordeSuperior tb : bordeSuperior){
                tb.draw(canvas);
            }

            //dibujar bordeInferior
            for(BordeInferior bb : bordeInferior){
                bb.draw(canvas);
            }
            //dibujar explosion
            if(started){
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void actualizarBordeSuperior(){

        //actualizar borde superior
        for(int i = 0; i < bordeSuperior.size(); i++){
            bordeSuperior.get(i).update();
            if (bordeSuperior.get(i).getX() < -30){
                //elimina elemento del arraylist y mete uno nuevo

                bordeSuperior.remove(i);

                bordeSuperior.add(new BordeSuperior(BitmapFactory.decodeResource(getResources(),
                        R.drawable.pinchosuperior), bordeSuperior.get(bordeSuperior.size() - 1).getX() + 30,
                        0, bordeSuperior.get(bordeSuperior.size() - 1).getHeight()));
            }
        }

    }

    public void actualizarBordeInferior(){

        //actualizar borde inferior
        for(int i = 0; i < bordeInferior.size(); i++){

            //elimina elemento del arraylist y mete uno nuevo

            bordeInferior.get(i).update();


            if(bordeInferior.get(i).getX() < -30){
                bordeInferior.remove(i);

                bordeInferior.add(new BordeInferior(BitmapFactory.decodeResource(getResources(), R.drawable.pinchoinferior
                ), bordeInferior.get(bordeInferior.size() - 1).getX() + 30, bordeInferior.get(bordeInferior.size() - 1
                ).getY()));
            }
        }
    }

    public void newGame(){
        dissapear = false;

        bordeInferior.clear();
        bordeSuperior.clear();

        enemigos.clear();

        tamBordes = 30;

        jugador.resetDY();
        jugador.resetScore();
        jugador.setY(HEIGHT / 2);
        modoFuria = null;


        for(int i = 0; i * 30 < WIDTH + 40; i++){
            // crea primer borde superior

            bordeSuperior.add(new BordeSuperior(BitmapFactory.decodeResource(getResources(), R.drawable.pinchosuperior
            ), i * 30, 0, 30));
        }

        for(int i = 0; i * 30 < WIDTH + 40; i++){
            // crea primer borde inferior

            bordeInferior.add(new BordeInferior(BitmapFactory.decodeResource(getResources(), R.drawable.pinchoinferior)
                    , i * 30, HEIGHT - 30));
        }
        isReady = true;
    }

    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Puntuacion: " + (jugador.getScore()), 10, HEIGHT - 10, paint);
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        int best = spp.getInt("record", 0);
        canvas.drawText("Record: " + best, WIDTH - 215, HEIGHT - 10, paint);

        if(!jugador.getPlaying() && isReady && reset){
            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Toque para iniciar", WIDTH / 2 - 60, HEIGHT / 2, paint1);
        }
    }
}