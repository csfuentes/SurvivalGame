package com.example.yo_pc.compasssurvival;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TestActivity extends AppCompatActivity {

    EditText resultado1, resultado2, resultado3, resultado4, resultado5;
    int probabilidad = 0;
    int resultadoint1 = 0, resultadoint2 = 0, resultadoint3 = 0, resultadoint4 = 0, resultadoint5 = 0;
    String resultadostring1, resultadostring2, resultadostring3, resultadostring4, resultadostring5;
    boolean datosIntroducidos = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void enviarResultado(View view){
        resultado1 = (EditText) findViewById(R.id.editText1);
        resultado2 = (EditText) findViewById(R.id.editText2);
        resultado3 = (EditText) findViewById(R.id.editText3);
        resultado4 = (EditText) findViewById(R.id.editText4);
        resultado5 = (EditText) findViewById(R.id.editText5);

        resultadostring1 = resultado1.getText().toString();
        if(resultadostring1 == null || resultadostring1.equals("")) datosIntroducidos=false;
        resultadostring2 = resultado2.getText().toString();
        if(resultadostring2 == null || resultadostring2.equals("")) datosIntroducidos=false;
        resultadostring3 = resultado3.getText().toString();
        if(resultadostring3 == null || resultadostring3.equals("")) datosIntroducidos=false;
        resultadostring4 = resultado4.getText().toString();
        if(resultadostring4 == null || resultadostring4.equals("")) datosIntroducidos=false;
        resultadostring5 = resultado5.getText().toString();
        if(resultadostring5 == null || resultadostring5.equals("")) datosIntroducidos=false;

        // Debug
        /*
        Log.d("Texto1: ", resultado1.getText().toString());
        Log.d("Texto2: ", resultado2.getText().toString());
        Log.d("Texto3: ", resultado3.getText().toString());
        Log.d("Texto4: ", resultado4.getText().toString());
        Log.d("Texto5: ", resultado5.getText().toString());
        */

        //Comprobamos que los datos se han introducido correctamente para evitar el crasheo de la aplicacion
        if(datosIntroducidos){
            // Con integer.parseInt() convierto de String a Int
            resultadoint1 = Integer.parseInt(resultado1.getText().toString());
            resultadoint2 = Integer.parseInt(resultado2.getText().toString());
            resultadoint3 = Integer.parseInt(resultado3.getText().toString());
            resultadoint4 = Integer.parseInt(resultado4.getText().toString());
            resultadoint5 = Integer.parseInt(resultado5.getText().toString());

            if(resultadoint1 != 42) probabilidad++;
            if(resultadoint2 != 73) probabilidad++;
            if(resultadoint3 != 74) probabilidad++;
            if(resultadoint4 != 26) probabilidad++;
            if(resultadoint5 != 5) probabilidad++;

            probabilidad = probabilidad * 20; // Convierte a porcentaje 100%
            //Log.d("Probabilidad: ", Integer.toString(probabilidad));

            AlertDialog.Builder resultadoAlert = new AlertDialog.Builder(this);
            if(probabilidad == 0){
                resultadoAlert.setMessage("No se ha detectado ninguna anomalia en su visión.")
                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener(){
                            // Codigo a ejecutar cuando pulsemos "Continuar"
                            public void onClick(DialogInterface dialog, int which){
                                //dialog.dismiss();
                                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .create();
                resultadoAlert.show();

            }
            // Si tenemos >0% de daltonismo
            else{
                resultadoAlert.setMessage("Se ha detectado un " + probabilidad + "% de daltonismo. ¿Desea ir a los ajustes de accesibilidad?")
                        .setPositiveButton("Ajustes...", new DialogInterface.OnClickListener(){
                            // Codigo a ejecutar cuando pulsemos "Ajustes..."
                            public void onClick(DialogInterface dialog, int which){
                                //dialog.dismiss();
                                //Intent intent = new Intent(TestActivity.this, AccesibilidadActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            // Codigo a ejecutar cuando pulsemos "Continuar"
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.dismiss();
                                //Intent intent = new Intent(TestActivity.this, MainActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                        })
                        .create();
                resultadoAlert.show();
            }
        }
        //Si los datos no han sido correctamente introducidos...
        else{
            AlertDialog.Builder datosNoIntroducidos = new AlertDialog.Builder(this);
            datosNoIntroducidos.setMessage("No se han introducido correctamente los resultados")
                        .setPositiveButton("Reintentar", new DialogInterface.OnClickListener(){
                            // Codigo a ejecutar cuando pulsemos "Reintentar"
                            public void onClick(DialogInterface dialog, int which){
                                datosIntroducidos=true;
                                dialog.dismiss();
                            }
                        })
                        .create();
            datosNoIntroducidos.show();
        }
    }
}
