package com.example.yo_pc.compasssurvival;

import android.content.Context;
import android.location.Geocoder;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Locale;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;



public class Puntuacion {
    Context mContext;
    static ArrayList<String> ranking = new ArrayList<String>();
    String nombre;
    int puntuacion;
    String localizacion;
    static ArrayList<Puntuacion> p = new ArrayList<Puntuacion>();
    //MiPosicion posicion = new MiPosicion();
    //RankingActivity r = new RankingActivity();

    public Puntuacion (int puntuacion, String nombre){
        this.puntuacion = puntuacion;
        this.nombre = nombre;
        //getposition();
        //Geocoder geocoder = new Geocoder(this.mContext, Locale.getDefault());
        //MiPosicion posicion = new MiPosicion();
        //this.puntuacion=geocoder.getFromLocation();
    }


    public void llenarRanking(){

        if (!p.isEmpty()){
            for (int i = 0; i <p.size() ; i++) {

                if(p.get(i).puntuacion<this.puntuacion) {

                    ranking.add(i, this.nombre + " | " + this.puntuacion + " | " + this.localizacion);
                }
            }
        }
        else {
            p.add(this);
            ranking.add( this.nombre + " | " + this.puntuacion + " | " + this.localizacion);

        }

        //r(RankingActivity).actu(ranking, p);
        //r.actu(ranking, p);

    }

    public ArrayList<String> actualizarRanking(){
        return ranking;

    }
    public void getposition(){
        //this.localizacion=posicion.enviarPosicion();
    }


}