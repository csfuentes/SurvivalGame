package com.example.yo_pc.compasssurvival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RankingActivity extends AppCompatActivity {


    static ArrayList<String> ranking = new ArrayList<String>();
    static ArrayList<Puntuacion> p = new ArrayList<Puntuacion>();
    Puntuacion puntuacion = new Puntuacion(0, "");

    public RankingActivity(){

    }
    //ArrayAdapter<String> mRankingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);



        ranking=puntuacion.actualizarRanking();

        //ranking = new ArrayList<String>(Arrays.asList(data));
        ListView listView = (ListView) findViewById(R.id.listview_ranking);


        ArrayAdapter<String> mRankingAdapter;
        mRankingAdapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.list_item_ranking, // The name of the layout ID.
                        ranking);

        listView.setAdapter(mRankingAdapter);


    }


}