package com.example.yo_pc.compasssurvival;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class Juego extends Activity{

    private MediaPlayer mp;
    private int mpCurrPos;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SharedPreferences spp = this.getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        //desactivar titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //poner fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        mp = MediaPlayer.create(this, R.raw.background);
        mp.setLooping(true);
        mp.setVolume(.5f, .5f);

        //Log.d("Musica valor", Integer.toString(spp.getInt("musicaEnabled", 1)));

        if(spp.getInt("musicaEnabled", 1) == 1){
            mp.start();
        }
        else mp.stop();

        setContentView(new JuegoPanel(this));
    }

    @Override
    protected void onPause(){
        super.onPause();
        mpCurrPos = mp.getCurrentPosition();
        mp.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mp.seekTo(mpCurrPos);
        mp.start();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mp.stop();
        mp.release();
        finish();
    }


}
