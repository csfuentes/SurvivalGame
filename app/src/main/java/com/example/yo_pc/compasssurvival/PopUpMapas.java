package com.example.yo_pc.compasssurvival;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Yo-PC on 11/12/2015.
 */
public class PopUpMapas extends Activity{

    RadioButton rbMapa1, rbMapa2, rbMapa3;
    RadioGroup rgPopUp;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.7), (int)(height*0.7));

        SharedPreferences sp = this.getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        rbMapa1 = (RadioButton) findViewById(R.id.rbMapa1);
        rbMapa2 = (RadioButton) findViewById(R.id.rbMapa2);
        rbMapa3 = (RadioButton) findViewById(R.id.rbMapa3);
        rgPopUp = (RadioGroup) findViewById(R.id.rgPopUp);



        if(sp.getInt("mapa", 1) == 1) rbMapa1.setChecked(true);
        else rbMapa1.setChecked(false);

        if(sp.getInt("mapa", 1) == 2) rbMapa2.setChecked(true);
        else rbMapa2.setChecked(false);

        if(sp.getInt("mapa", 1) == 3) rbMapa3.setChecked(true);
        else rbMapa3.setChecked(false);


    }

    public void seleccionarMapa(View v){
        SharedPreferences sp = this.getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        if(rbMapa1.isChecked()) sp.edit().putInt("mapa", 1).commit();
        else if(rbMapa2.isChecked()) sp.edit().putInt("mapa", 2).commit();
        else if (rbMapa3.isChecked()) sp.edit().putInt("mapa", 3).commit();

        Intent intent = new Intent(PopUpMapas.this, Juego.class);
        startActivity(intent);
    }

    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
