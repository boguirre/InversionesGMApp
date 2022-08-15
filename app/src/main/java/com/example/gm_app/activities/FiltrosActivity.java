package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gm_app.R;

public class FiltrosActivity extends AppCompatActivity {

    CardView cardcelulares;
    CardView cardcovers;
    CardView cardaudifonos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
        cardaudifonos = findViewById(R.id.cardAudifonos);
        cardcelulares = findViewById(R.id.cardCelulares);
        cardcovers = findViewById(R.id.cardCovers);

        cardcovers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilters("COVERS");
            }
        });

        cardcelulares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilters("CELULARES");
            }
        });

        cardaudifonos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilters("EQUIPOS DE AUDIO");
            }
        });

    }

    private void goToFilters(String categoria){
        Intent intent = new Intent(this, CategoryFiltersActivity.class );
        intent.putExtra("tipo", categoria);
        startActivity(intent);
    }
}