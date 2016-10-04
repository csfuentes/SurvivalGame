package com.example.yo_pc.compasssurvival;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    String nombreUsuario;
    TextView tvNombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tvNombreUsuario = (TextView) findViewById(R.id.tvNombreUsuario);

        SharedPreferences sp = this.getSharedPreferences("com.example.yo_pc.compasssurvival", 0);

        //tvNombreUsuario.setText("pepito");
        if(!sp.getString("nombreusuario", "---").equals("---")){
            tvNombreUsuario.setText("Bienvenido " + sp.getString("nombreusuario", "---"));
        }
        else{
            tvNombreUsuario.setText("Nombre de usuario no establecido");
        }

        if(sp.getInt("popupInicial", 0) == 0){
            sp.edit().putInt("popupInicial", 1).commit();
            AlertDialog.Builder ajustesAlert = new AlertDialog.Builder(this);
            ajustesAlert.setMessage("¿Desea configurar los ajustes de accesibilidad antes de comenzar?")
                    .setPositiveButton("Ajustes...", new DialogInterface.OnClickListener(){
                        // Codigo a ejecutar cuando pulsemos "Ajustes..."
                        public void onClick(DialogInterface dialog, int which){
                            Intent intent = new Intent(MainActivity.this, AccesibilidadActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No, ¡dejame en paz!", new DialogInterface.OnClickListener() {
                        // Codigo a ejecutar cuando pulsemos "Continuar"
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            ajustesAlert.show();

        }

    }

    public void startGame(View v){
        SharedPreferences sp = this.getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        if(!sp.getString("nombreusuario", "---").equals("---")){
            Intent intent = new Intent(MainActivity.this, PopUpMapas.class);
            startActivity(intent);
        }
        else{
            showToastMessage("¡Espera, quiero saber como te llamas!");
        }

    }

    public void startSettings(View v){
        Intent intent = new Intent(MainActivity.this, AccesibilidadActivity.class);
        startActivity(intent);
    }

    public void buttonRanking(View v){
        Intent intent = new Intent(MainActivity.this, RankingActivity.class);
        startActivity(intent);
    }

    public void buttonNombreUsuario(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga su nombre");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);


        startActivityForResult(intent, 1001);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sp = this.getSharedPreferences("com.example.yo_pc.compasssurvival", 0);
        if (resultCode == RESULT_OK) {
            tvNombreUsuario = (TextView) findViewById(R.id.tvNombreUsuario);
            ArrayList<String> textMatchlist = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            sp.edit().putString("nombreusuario", textMatchlist.get(0).toString()).commit();
            tvNombreUsuario.setText("Bienvenido " + textMatchlist.get(0).toString());


            //Log.d("matchlist: ", textMatchlist.get(0).toString());

            //super.onActivityResult(requestCode, resultCode, data);
        }

    }

    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
