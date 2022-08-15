package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.R;
import com.example.gm_app.adapters.ProveedorAdapter;
import com.example.gm_app.modelo.Proveedor;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class ListProveedorActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener{

    FloatingActionButton mfabregister;
    MaterialSearchBar msearchabar;
    RecyclerView mrecyclerview;
    ProveedorDAO daoproveedor;
    ProveedorAdapter proveedorAdapter;
    ProveedorAdapter proveedorAdapterSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_proveedor);

        mfabregister = findViewById(R.id.fabregister);
        msearchabar = findViewById(R.id.searchBarProveedor);

        daoproveedor = new ProveedorDAO();

        msearchabar.setOnSearchActionListener(this);

        mrecyclerview = findViewById(R.id.recyclervProveedor);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(linearLayoutManager);

        mfabregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegistrarProveedor();
            }
        });
    }

    private void searchByTitle(String name){
        Query query = daoproveedor.getProveedorByName(name);
        FirestoreRecyclerOptions<Proveedor> options = new FirestoreRecyclerOptions.Builder<Proveedor>()
                .setQuery(query, Proveedor.class)
                .build();
        proveedorAdapterSearch = new ProveedorAdapter(options, this);
        proveedorAdapterSearch.notifyDataSetChanged();
        mrecyclerview.setAdapter(proveedorAdapterSearch);
        proveedorAdapterSearch.startListening();
    }

    private void getAllProveedor(){
        Query query = daoproveedor.getAll();
        FirestoreRecyclerOptions<Proveedor> options = new FirestoreRecyclerOptions.Builder<Proveedor>()
                .setQuery(query, Proveedor.class)
                .build();
        proveedorAdapter = new ProveedorAdapter(options, this);
        proveedorAdapter.notifyDataSetChanged();
        mrecyclerview.setAdapter(proveedorAdapter);
        proveedorAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllProveedor();

    }

    @Override
    protected void onStop() {
        super.onStop();
        proveedorAdapter.startListening();
        if (proveedorAdapterSearch != null){
            proveedorAdapterSearch.stopListening();
        }
    }

    private void goRegistrarProveedor() {
        Intent intent = new Intent(ListProveedorActivity.this, RegistrarProveedorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getAllProveedor();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByTitle(text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}