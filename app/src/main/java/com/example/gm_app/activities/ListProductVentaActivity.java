package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.R;
import com.example.gm_app.adapters.ProductVentaAdapter;
import com.example.gm_app.adapters.ProductoAdapter;
import com.example.gm_app.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class ListProductVentaActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener{

    MaterialSearchBar msearchabar;
    RecyclerView mrecyclerview;
    ProductoDAO daoproducto;
    ProductVentaAdapter productVentaAdapter;
    ProductVentaAdapter productoAdapterSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_venta);

        msearchabar = findViewById(R.id.searchBarProductVenta);
        daoproducto = new ProductoDAO();

        msearchabar.setOnSearchActionListener(this);


        mrecyclerview = findViewById(R.id.recyclervProductoVenta);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(linearLayoutManager);
    }

    private void searchByTitle(String title){
        Query query = daoproducto.getProductByName(title);
        FirestoreRecyclerOptions<Producto> options = new FirestoreRecyclerOptions.Builder<Producto>()
                .setQuery(query, Producto.class)
                .build();
        productoAdapterSearch = new ProductVentaAdapter(options, this);
        productoAdapterSearch.notifyDataSetChanged();
        mrecyclerview.setAdapter(productoAdapterSearch);
        productoAdapterSearch.startListening();
    }

    private void getAllProduct(){
        Query query = daoproducto.getAll();
        FirestoreRecyclerOptions<Producto> options = new FirestoreRecyclerOptions.Builder<Producto>()
                .setQuery(query, Producto.class)
                .build();
        productVentaAdapter = new ProductVentaAdapter(options, this);
        productVentaAdapter.notifyDataSetChanged();
        mrecyclerview.setAdapter(productVentaAdapter);
        productVentaAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllProduct();

    }


    @Override
    protected void onStop() {
        super.onStop();
        productVentaAdapter.startListening();
        if (productoAdapterSearch != null){
            productoAdapterSearch.stopListening();
        }
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