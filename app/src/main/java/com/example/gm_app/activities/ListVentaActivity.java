package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.Dao.VentaDAO;
import com.example.gm_app.R;
import com.example.gm_app.adapters.ProveedorAdapter;
import com.example.gm_app.adapters.VentaAdapter;
import com.example.gm_app.modelo.Proveedor;
import com.example.gm_app.modelo.Venta;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class ListVentaActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener{

    FloatingActionButton mfabregisterventa;
    MaterialSearchBar msearchabar;
    RecyclerView mrecyclerview;
    VentaDAO ventaDAO;
    VentaAdapter ventaAdapter;
    VentaAdapter ventaAdapterSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_venta);

        mfabregisterventa = findViewById(R.id.fabregisterVenta);
        ventaDAO = new VentaDAO();

        msearchabar = findViewById(R.id.searchBarVenta);
        msearchabar.setOnSearchActionListener(this);

        mrecyclerview = findViewById(R.id.recyclervVenta);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(linearLayoutManager);


        mfabregisterventa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegistrarVenta();
            }
        });
    }

    private void searchByFecha(String fecha){
        Query query = ventaDAO.getVentaByFecha(fecha);
        FirestoreRecyclerOptions<Venta> options = new FirestoreRecyclerOptions.Builder<Venta>()
                .setQuery(query, Venta.class)
                .build();
        ventaAdapterSearch = new VentaAdapter(options, this);
        ventaAdapterSearch.notifyDataSetChanged();
        mrecyclerview.setAdapter(ventaAdapterSearch);
        ventaAdapterSearch.startListening();
    }

    private void getAllVenta(){
        Query query = ventaDAO.getAll();
        FirestoreRecyclerOptions<Venta> options = new FirestoreRecyclerOptions.Builder<Venta>()
                .setQuery(query, Venta.class)
                .build();
        ventaAdapter = new VentaAdapter(options, this);
        ventaAdapter.notifyDataSetChanged();
        mrecyclerview.setAdapter(ventaAdapter);
        ventaAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllVenta();

    }

    @Override
    protected void onStop() {
        super.onStop();
        ventaAdapter.startListening();
        if (ventaAdapterSearch != null){
            ventaAdapterSearch.stopListening();
        }
    }

    private void goRegistrarVenta() {
        Intent intent = new Intent(ListVentaActivity.this, ListProductVentaActivity.class);
        startActivity(intent);

    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getAllVenta();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByFecha(text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}