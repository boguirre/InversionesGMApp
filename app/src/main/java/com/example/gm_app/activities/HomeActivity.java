package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.gm_app.R;

public class HomeActivity extends AppCompatActivity {

    ImageView btnProveedor;
    ImageView btnProducto;
    ImageView btnIngresos;
    ImageView btnFiltros;
    ImageView btnCliente;
    ImageView btnVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnProveedor = findViewById(R.id.btnProveedorVentana);
        btnProducto = findViewById(R.id.btnProductoVentana);
        btnIngresos = findViewById(R.id.IngresosVentana);
        btnFiltros = findViewById(R.id.FiltrosActivity);
        btnCliente = findViewById(R.id.ClienteActivity);
        btnVenta = findViewById(R.id.ModuloVenta);

        btnProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListProveedorActivity.class);
                startActivity(intent);
            }
        });

        btnProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListProductoActivity.class);
                startActivity(intent);
            }
        });

        btnIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, IngresosActivity.class);
                startActivity(intent);
            }
        });

        btnFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FiltrosActivity.class);
                startActivity(intent);
            }
        });

        btnCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListClienteActivity.class);
                startActivity(intent);
            }
        });

        btnVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListVentaActivity.class);
                startActivity(intent);
            }
        });
    }


}