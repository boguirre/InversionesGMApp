package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.R;
import com.example.gm_app.adapters.ProductoAdapter;
import com.example.gm_app.adapters.ProveedorAdapter;
import com.example.gm_app.modelo.Producto;
import com.example.gm_app.modelo.Proveedor;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class ListProductoActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    FloatingActionButton mfabregisterprod;
    MaterialSearchBar msearchabar;
    RecyclerView mrecyclerview;
    ProductoDAO daoproducto;
    ProductoAdapter productoAdapterr;
    ProductoAdapter productoAdapterSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_producto);

        mfabregisterprod = findViewById(R.id.fabregisterProducto);
        msearchabar = findViewById(R.id.searchBar);
        daoproducto = new ProductoDAO();

        msearchabar.setOnSearchActionListener(this);


        mrecyclerview = findViewById(R.id.recyclervProducto);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(linearLayoutManager);

        mfabregisterprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegistrarProducto();
            }
        });

    }

    private void searchByTitle(String title){
        Query query = daoproducto.getProductByName(title);
        FirestoreRecyclerOptions<Producto> options = new FirestoreRecyclerOptions.Builder<Producto>()
                .setQuery(query, Producto.class)
                .build();
        productoAdapterSearch = new ProductoAdapter(options, this);
        productoAdapterSearch.notifyDataSetChanged();
        mrecyclerview.setAdapter(productoAdapterSearch);
        productoAdapterSearch.startListening();
    }

    private void getAllProduct(){
        Query query = daoproducto.getAll();
        FirestoreRecyclerOptions<Producto> options = new FirestoreRecyclerOptions.Builder<Producto>()
                .setQuery(query, Producto.class)
                .build();
        productoAdapterr = new ProductoAdapter(options, this);
        productoAdapterr.notifyDataSetChanged();
        mrecyclerview.setAdapter(productoAdapterr);
        productoAdapterr.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllProduct();

    }


    @Override
    protected void onStop() {
        super.onStop();
        productoAdapterr.startListening();
        if (productoAdapterSearch != null){
            productoAdapterSearch.stopListening();
        }
    }

    private void goRegistrarProducto() {
        Intent intent = new Intent(ListProductoActivity.this, RegistrarProductoActivity.class);
        startActivity(intent);

    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getAllProduct();
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