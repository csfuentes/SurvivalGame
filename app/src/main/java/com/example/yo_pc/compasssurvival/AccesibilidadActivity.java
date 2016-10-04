package com.example.yo_pc.compasssurvival;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class AccesibilidadActivity extends AppCompatActivity implements OnClickListener {

    CheckBox vibracion, efectos, musica;
    Context mContext;
    Button botonEscanear;

    TextView tvFormat, tvContent;

    public void AccesibilidadActivity(Context context){
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences spp = this.getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesibilidad);

        vibracion = (CheckBox) findViewById(R.id.checkBox);
        efectos = (CheckBox) findViewById(R.id.checkBox2);
        musica = (CheckBox) findViewById(R.id.checkBox3);

        //sp.edit().putInt("vibracionEnabled", 1);

        if(spp.getInt("vibracionEnabled", 1) == 1) vibracion.setChecked(true);
        else vibracion.setChecked(false);

        if(spp.getInt("efectosEnabled", 1) == 1) efectos.setChecked(true);
        else efectos.setChecked(false);

        if(spp.getInt("musicaEnabled", 1) == 1) musica.setChecked(true);
        else musica.setChecked(false);


        botonEscanear =(Button) findViewById(R.id.buttonEscanear);
        botonEscanear.setOnClickListener(this);


    }

    public void ejecutarTest(View v){
        Intent intent = new Intent(AccesibilidadActivity.this, TestActivity.class);
        startActivity(intent);
    }

    public void aplicarAjustes(View v){

        SharedPreferences spp = this.getSharedPreferences("com.example.yo_pc.compasssurvival", 0);



        vibracion = (CheckBox) findViewById(R.id.checkBox);
        efectos = (CheckBox) findViewById(R.id.checkBox2);
        musica = (CheckBox) findViewById(R.id.checkBox3);

        if(vibracion.isChecked()){
            spp.edit().putInt("vibracionEnabled", 1).commit();
        }
        else spp.edit().putInt("vibracionEnabled", 0).commit();

        if(efectos.isChecked()){
            spp.edit().putInt("efectosEnabled", 1).commit();
        }
        else spp.edit().putInt("efectosEnabled", 0).commit();

        if(musica.isChecked()){
            spp.edit().putInt("musicaEnabled", 1).commit();
        }
        else spp.edit().putInt("musicaEnabled", 0).commit();

        //Log.d("checkbox: ", Integer.toString(spp.getInt("vibracionEnabled", 1)));

        AlertDialog.Builder ajustesAlert = new AlertDialog.Builder(this);
        ajustesAlert.setMessage("Ajustes aplicados")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    // Codigo a ejecutar cuando pulsemos "Ajustes..."
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        //finish();
                    }
                })
                .create();
        ajustesAlert.show();

        tvFormat = (TextView) findViewById(R.id.tvFormat);
        tvContent = (TextView) findViewById(R.id.tvContent);


    }

    public void onClick(View v){
        if(v.getId() == R.id.buttonEscanear) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        SharedPreferences spp = this.getSharedPreferences("com.example.yo_pc.compasssurvival", 0);

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null){
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            //tvFormat.setText("FORMAT: " + scanFormat);
            //tvContent.setText("CONTENT: " + scanContent);
            if(scanContent.equals("Personaje1")){
                //Log.d("ENTROOOOOOO AL 1", "SIIIII");
                spp.edit().putInt("personaje", 1).commit();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Fijado " + scanContent, Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(scanContent.equals("Personaje2")){
                //Log.d("ENTROOOOOOO AL 2", "SIIIII");
                spp.edit().putInt("personaje", 2).commit();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Fijado " + scanContent, Toast.LENGTH_SHORT);
                toast.show();
            }
                //Log.d("ENTROOOOOOO AL 2", "SIIIII");

            else if(scanContent.equals("Personaje3")){
                spp.edit().putInt("personaje", 3).commit();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Fijado " + scanContent, Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "El codigo no corresponde con ningun jugador ", Toast.LENGTH_SHORT);
                toast.show();
            }
            /*Toast toast = Toast.makeText(getApplicationContext(),
                    "Fijado " + scanContent, Toast.LENGTH_SHORT);
            toast.show();*/
            Log.d("FORMAT: ", scanFormat);
            Log.d("CONTENT: ", scanContent);
            Log.d("Valor pj accesibili: ", Integer.toString(spp.getInt("personaje", 1)));
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Error al leer", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accesibilidad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
